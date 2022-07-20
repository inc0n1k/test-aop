package test.aop.testaop.utils.mess;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Locale {
    RU("Русский"),
    KK("Казахский"),
    EN("Английский");

    private final String name;
}