package test.aop.testaop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import test.aop.testaop.dto.RequestDto;
import test.aop.testaop.exception.TestAopException;
import test.aop.testaop.utils.mess.MessageUtils;

import java.util.stream.IntStream;

@Service
public class TestService {

    private final MessageUtils messageUtils;
    private final ObjectMapper objectMapper;

    public TestService(
            MessageUtils messageUtils,
            ObjectMapper objectMapper
    ) {
        this.messageUtils = messageUtils;
        this.objectMapper = objectMapper;
    }

    public Object testService(RequestDto request) throws Exception {
        System.out.println(objectMapper.writeValueAsString(request));
        switch (request.getBranch()) {
            case 1 -> branch1();
            case 2 -> branch2();
            case 3 -> branch3();
        }
        return request;
    }

    public void voidTestService(RequestDto request) throws Exception {
        System.out.println(objectMapper.writeValueAsString(request));
        switch (request.getBranch()) {
            case 1 -> branch1();
            case 2 -> branch2();
            case 3 -> branch3();
        }
    }

    private void branch1() throws TestAopException {
        if (true)
            throw new TestAopException("test.error");
    }

    private void branch2() {
        int i = 6 / 0;
    }

    private void branch3() {
        var arr = new int[5];
        IntStream.range(1, 6).forEachOrdered(e -> arr[e - 1] = e);
        for (int i = 0; i < 10; i++) {
            System.out.println(arr[i]);
        }
    }

}