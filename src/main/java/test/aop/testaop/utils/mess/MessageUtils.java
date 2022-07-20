package test.aop.testaop.utils.mess;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageUtils {

    private final MessageSource messageSource;

    public MessageUtils(
            MessageSource messageSource
    ) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Locale locale) {
        return switch (locale) {
            case RU:
                yield messageSource.getMessage(key, new Object[0], new java.util.Locale("ru", "RU"));
            case KK:
                yield messageSource.getMessage(key, new Object[0], new java.util.Locale("kk", "KK"));
            case EN:
                yield messageSource.getMessage(key, new Object[0], java.util.Locale.US);
        };
    }

    public LocalizedText getMessage(String key) {
        return LocalizedText.builder()
                .ru(messageSource.getMessage(key, new Object[0], new java.util.Locale("ru", "RU")))
                .kk(messageSource.getMessage(key, new Object[0], new java.util.Locale("kk", "KK")))
                .en(messageSource.getMessage(key, new Object[0], java.util.Locale.US))
                .build();
    }

}