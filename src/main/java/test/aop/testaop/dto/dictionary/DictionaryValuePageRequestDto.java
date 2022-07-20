package test.aop.testaop.dto.dictionary;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import test.aop.testaop.dto.BasePaginationRequestDto;
import test.aop.testaop.dto.constant.DisplayType;

@Getter
@Setter
public class DictionaryValuePageRequestDto extends BasePaginationRequestDto {

    @NotBlank(message = "ne moget bit pustim")
    private String typeCode;

    private DisplayType displayType = DisplayType.SHORT;

}