package ru.t1.clientprocessing.service;

import ru.t1.clientprocessing.dto.LoginRequest;
import ru.t1.clientprocessing.dto.LoginResponse;

public interface AuthService {
    LoginResponse authenticate(LoginRequest loginRequest);
}
