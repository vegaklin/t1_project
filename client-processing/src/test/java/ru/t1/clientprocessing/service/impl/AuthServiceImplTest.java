package ru.t1.clientprocessing.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.t1.clientprocessing.dto.LoginRequest;
import ru.t1.clientprocessing.dto.LoginResponse;
import ru.t1.clientprocessing.util.JwtUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserDetailsImpl userDetails;

    private static String TEST_JWT_TOKEN = "jwt-token";
    private static String TEST_USERNAME = "username";
    private static String TEST_EMAIL = "test@mail.ru";
    private static String TEST_PASSWORD = "password";
    private static String TEST_AUTHORITIES = "CURRENT_CLIENT";

    @BeforeEach
    void setUp() {
        userDetails = new UserDetailsImpl(
                1L,
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_PASSWORD,
                List.of((GrantedAuthority) () -> TEST_AUTHORITIES)
        );
    }

    @Test
    void checkAuthenticate_loginResponse_credentialsValid() {
        // given
        LoginRequest request = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(TEST_JWT_TOKEN);

        // when
        LoginResponse response = authService.authenticate(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo(TEST_JWT_TOKEN);
        assertThat(response.login()).isEqualTo(TEST_USERNAME);
        assertThat(response.email()).isEqualTo(TEST_EMAIL);
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.roles()).containsExactly(TEST_AUTHORITIES);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(authentication);
    }

    @Test
    void checkAuthenticate_throwException_credentialsInvalid() {
        // given
        LoginRequest request = new LoginRequest("wrong_user", "wrong_password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Incorrect data"));

        // when-then
        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Incorrect data");

        verify(jwtUtils, never()).generateJwtToken(any());
    }
}
