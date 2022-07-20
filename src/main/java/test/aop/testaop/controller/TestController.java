package test.aop.testaop.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import test.aop.testaop.annotation.AspectUsing;
import test.aop.testaop.dto.RequestDto;
import test.aop.testaop.service.TestService;
import test.aop.testaop.utils.ApiResponse;
import test.aop.testaop.utils.BaseController;


@RestController
public class TestController extends BaseController {

    private final TestService testService;

    public TestController(
            TestService testService
    ) {
        this.testService = testService;
    }

    //    @AspectUsing
    @PostMapping("/test")
    public ResponseEntity<ApiResponse> test(
            @Valid @RequestBody RequestDto request
    ) throws Exception {
        return ok(testService.testService(request));
    }

    @PostMapping("/void-test")
    public void voidTest(
            @Valid @RequestBody RequestDto request
    ) throws Exception {
        testService.voidTestService(request);
       // return ok();
    }

}