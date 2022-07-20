package test.aop.testaop.dto.dictionary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import test.aop.testaop.dto.constant.SettingType;
@Getter
public class AddConfigDto {

    @NotBlank(message = "ne blank")
    private String typeCode;

    @NotNull(message = "ne null")
    private SettingType type;

    @NotBlank(message = "ne blank")
    private String code;
}
