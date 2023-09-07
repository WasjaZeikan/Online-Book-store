package mate.academy.intro.service;

import mate.academy.intro.dto.user.UserLoginRequestDto;
import mate.academy.intro.dto.user.UserLoginResponseDto;
import mate.academy.intro.dto.user.UserRegistrationRequestDto;
import mate.academy.intro.dto.user.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto loginRequestDto);

    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;
}
