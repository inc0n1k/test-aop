package test.aop.testaop.dto.dictionary;

import lombok.Getter;
import test.aop.testaop.dto.BasePaginationRequestDto;

@Getter
public class DictionaryTypePageRequestDto extends BasePaginationRequestDto {

    private String sort = "code";

}