package test.aop.testaop.dto.dictionary;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import test.aop.testaop.dto.BaseDictionaryTypeAndValueDto;

import java.util.Map;

@Getter
@SuperBuilder
public class FullDictionaryValueDto extends BaseDictionaryTypeAndValueDto {

    private String dictionaryTypeCode;

    private String parentCode;

    private Map<String, SettingDto> settings;

}