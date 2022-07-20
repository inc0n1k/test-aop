package test.aop.testaop.dto.dictionary;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettingDto {

    private String code;

    private Class aClass;

    private String value;

}
