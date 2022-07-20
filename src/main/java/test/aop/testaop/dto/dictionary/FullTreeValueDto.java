package test.aop.testaop.dto.dictionary;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import test.aop.testaop.dto.BaseDictionaryTypeAndValueDto;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
public class FullTreeValueDto extends BaseDictionaryTypeAndValueDto {

    private String dictionaryTypeCode;

    private String parentCode;

    private Map<String, SettingDto> settings;

    private List<FullTreeValueDto> child;

}