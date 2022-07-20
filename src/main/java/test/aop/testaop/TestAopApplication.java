package test.aop.testaop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class TestAopApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAopApplication.class, args);
    }

}
