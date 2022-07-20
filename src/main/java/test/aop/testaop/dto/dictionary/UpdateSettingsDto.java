package test.aop.testaop.dto.dictionary;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Map;

@Getter
public class UpdateSettingsDto {

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String typeCode;

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String valueCode;

    private Map<String, String> settings;

}