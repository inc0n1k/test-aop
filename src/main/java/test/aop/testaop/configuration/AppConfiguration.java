package test.aop.testaop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class AppConfiguration {

    @Bean
    public ResourceBundleMessageSource messageSource() {
        var source = new ResourceBundleMessageSource();
        source.setBasenames("message");
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

//        ObjectMapper om = new ObjectMapper();
//        om.registerModule(new JavaTimeModule());
//        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        om.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
//        om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

}