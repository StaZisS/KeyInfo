package org.example.key_info.core.client.validation;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.repository.ClientRepository;
import org.example.key_info.public_interface.client.ClientCreateDto;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ClientValidationService {
    private static final List<String> AVAILABLE_GENDER = List.of("MALE", "FEMALE", "UNSPECIFIED");
    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private final ClientRepository clientRepository;

    public void validateCreateClient(ClientCreateDto dto) {
        checkGender(dto.gender());
        checkEmail(dto.email());
    }

    private void checkGender(String gender) {
        if (AVAILABLE_GENDER.contains(gender)) return;
        throw new ExceptionInApplication("Указан неподдерживаемый гендер", ExceptionType.INVALID);
    }

    private void checkEmail(String email) {
        checkEmailIsCorrect(email);
        checkClientExistsByEmail(email);
    }

    private void checkClientExistsByEmail(String email) {
        var client = clientRepository.getClientByEmail(email);
        if (client.isEmpty()) return;
        throw new ExceptionInApplication("Пользователь с такой почтой уже существует", ExceptionType.ALREADY_EXISTS);
    }

    private void checkEmailIsCorrect(String email) {
        var isCorrect = Pattern.compile(EMAIL_PATTERN)
                .matcher(email)
                .matches();

        if (isCorrect) return;
        throw new ExceptionInApplication("Указана неккоректная почта", ExceptionType.INVALID);
    }

}
