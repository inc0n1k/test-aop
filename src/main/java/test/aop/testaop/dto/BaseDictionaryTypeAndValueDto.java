package test.aop.testaop.dto;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseDictionaryTypeAndValueDto {

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String code;

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String nameRu;

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String nameKk;

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String nameEn;

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String shortNameRu;

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String shortNameKk;

    @NotBlank(message = "Не может быть пустым или состоять из пробелов")
    private String shortNameEn;

}