package test.aop.testaop.dto.dictionary;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import test.aop.testaop.dto.constant.SettingType;

@Setter
@Getter
@Builder
public class ConfigDto {

    private String code;

    private SettingType type;

    private String example;

}