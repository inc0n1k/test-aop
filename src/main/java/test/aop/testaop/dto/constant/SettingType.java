package test.aop.testaop.dto.constant;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public enum SettingType {

    STRING(String.class, "Example string", ".*"),
    BOOLEAN(Boolean.class, "true/false", "true|false"),
    LOCALDATETIME(LocalDateTime.class, "1999-09-09 17:18:19", "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"),
    LOCALDATE(LocalDate.class, "1995-01-01", "\\d{4}-\\d{2}-\\d{2}"),
    BIGDECIMAL(BigDecimal.class, "123.321","\\d+\\.\\d+"),
    LONG(Long.class, "123","\\d+"),
    INTEGER(Integer.class, "123","\\d+"),
    DOUBLE(Double.class, "123.321","\\d+\\.\\d+");

    private final Class aClass;
    private final String example;
    private final String pattern;

    SettingType(Class aClass, String example, String pattern) {
        this.aClass = aClass;
        this.example = example;
        this.pattern = pattern;
    }

}