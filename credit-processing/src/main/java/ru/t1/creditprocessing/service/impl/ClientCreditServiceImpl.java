package ru.t1.creditprocessing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.creditprocessing.dto.ClientCreditProductDto;
import ru.t1.creditprocessing.dto.ClientInfoResponse;
import ru.t1.creditprocessing.entity.PaymentRegistry;
import ru.t1.creditprocessing.entity.ProductRegistry;
import ru.t1.creditprocessing.exception.CreditDenialException;
import ru.t1.creditprocessing.repository.PaymentRegistryRepository;
import ru.t1.creditprocessing.repository.ProductRegistryRepository;
import ru.t1.creditprocessing.service.ClientCreditService;
import ru.t1.creditprocessing.service.ClientInfoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientCreditServiceImpl implements ClientCreditService {

    private final ProductRegistryRepository productRegistryRepository;
    private final PaymentRegistryRepository paymentRegistryRepository;

    private final ClientInfoService clientInfoService;

    @Value("${credit.limit}")
    private BigDecimal creditLimit;

    @Value("${credit.interest-rate-percent}")
    private BigDecimal interestRatePercent;

    @Override
    @Transactional
    public void createCredit(ClientCreditProductDto clientCreditProductDto) {
        ClientInfoResponse clientInfo = clientInfoService.getClientInfo(clientCreditProductDto.clientId());

        validateCreditConditions(clientCreditProductDto);

        ProductRegistry product = createProduct(clientCreditProductDto);
        product = productRegistryRepository.save(product);

        List<PaymentRegistry> paymentSchedule = generatePaymentSchedule(product, clientCreditProductDto.amount());
        paymentSchedule.forEach(paymentRegistryRepository::save);
    }

    private void validateCreditConditions(ClientCreditProductDto clientCreditProductDto) {
        List<ProductRegistry> products = productRegistryRepository.findByClientId(clientCreditProductDto.clientId());

        BigDecimal totalDebt = products.stream()
                .map(product -> product.getPayments().stream()
                        .map(PaymentRegistry::getDebtAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalDebt.add(clientCreditProductDto.amount()).compareTo(creditLimit) > 0) {
            throw new CreditDenialException("Credit denial: limit exceeded");
        }

        boolean hasExpired = products.stream()
                .flatMap(product -> product.getPayments().stream())
                .anyMatch(PaymentRegistry::getExpired);
        if (hasExpired) {
            throw new CreditDenialException("Credit denial: there are delays");
        }
    }


    private ProductRegistry createProduct(ClientCreditProductDto clientCreditProductDto) {
        ProductRegistry product = new ProductRegistry();
        product.setClientId(clientCreditProductDto.clientId());
        product.setAccountId(1L); // TODO:
        product.setProductId(clientCreditProductDto.productId());
        product.setInterestRate(interestRatePercent.divide(BigDecimal.valueOf(100),  10, RoundingMode.HALF_UP));
        product.setOpenDate(LocalDate.now());
        product.setMonthCount(clientCreditProductDto.monthCount());
        return product;
    }

    private List<PaymentRegistry> generatePaymentSchedule(ProductRegistry product, BigDecimal amount) {
        int monthCount = product.getMonthCount();
        BigDecimal monthlyRate = product.getInterestRate()
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        // A = S × [i × (1 + i)^n] / [(1 + i)^n - 1]
        BigDecimal power = monthlyRate.add(BigDecimal.ONE).pow(monthCount);
        BigDecimal annuityPayment = amount.multiply(
                monthlyRate.multiply(power)
                        .divide(power.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP)
        );

        BigDecimal currentDebt = amount;
        List<PaymentRegistry> payments = new ArrayList<>();

        for (int month = 1; month <= monthCount; month++) {
            BigDecimal interestRateAmount = currentDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal debtAmount = annuityPayment.subtract(interestRateAmount).setScale(2, RoundingMode.HALF_UP);

            if (month == monthCount) {
                debtAmount = currentDebt;
                annuityPayment = debtAmount.add(interestRateAmount);
                currentDebt = BigDecimal.ZERO;
            } else {
                currentDebt = currentDebt.subtract(debtAmount);
            }

            PaymentRegistry payment = new PaymentRegistry();
            payment.setProductRegistry(product);
            payment.setPaymentDate(product.getOpenDate().plusMonths(month));
            payment.setAmount(annuityPayment);
            payment.setInterestRateAmount(interestRateAmount);
            payment.setDebtAmount(debtAmount);
            payment.setExpired(false);
            payment.setPaymentExpirationDate(product.getOpenDate().plusMonths(month));

            payments.add(payment);
        }

        return payments;
    }
}
