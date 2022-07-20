package test.aop.testaop.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import test.aop.testaop.exception.TestAopException;
import test.aop.testaop.exception.VoidException;
import test.aop.testaop.utils.ApiResponse;
import test.aop.testaop.utils.mess.Locale;
import test.aop.testaop.utils.mess.MessageUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Objects.nonNull;


@Slf4j
@Aspect
@Component
public class LogAspect {

    private final JdbcTemplate jdbcTemplate;
    private final MessageUtils messageUtils;
//    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    public LogAspect(
            JdbcTemplate jdbcTemplate,
            MessageUtils messageUtils,
//            ObjectMapper objectMapper,
            HttpServletRequest request
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.messageUtils = messageUtils;
//        this.objectMapper = objectMapper;
        this.request = request;
    }

    //    @Pointcut("@annotation(test.aop.testaop.utils.AspectUsing)")
    @Pointcut("execution(!void test.aop.testaop.controller.*Controller.*(..))")
    public void notVoidControllerAspect() {
        //
    }

    @Pointcut("execution(void test.aop.testaop.controller.*Controller.*(..))")
    public void voidControllerAspect() {
        //
    }

    @Pointcut("execution(* test.aop.testaop.controller.*Controller.*(..))")
    public void allControllerAspect() {
        //
    }

    /*@After("controllerAspect()")
    public void after(JoinPoint joinPoint) {
        try {
            log.info("\nRequestURI={},Method={},\nParameterMap={},Cost={}",
                    // Запросить URI пути
                    request.getRequestURI(),
                    // Имя метода запроса
                    request.getMethod(),
                    // Получаем массив параметров
                    objectMapper.writeValueAsString(Arrays.toString(joinPoint.getArgs())),
                    System.currentTimeMillis() - beginTimeThreadLocal.get().getTime());
            // Выполняем операции хранения журнала

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }*/

    @AfterThrowing(value = "allControllerAspect()", throwing = "exception")//при ошибке
    public void afterThrow(JoinPoint joinPoint, Exception exception) {
//        String str = String.format("Метод - %s, класса- %s, был аварийно завершен с исключением - %s\n",
//                joinPoint.getSignature().getName(),
//                joinPoint.getSourceLocation().getWithinType().getName(),
//                exception);
        var ldt = LocalDateTime.now();
        jdbcTemplate.update("insert into errors.errors (module,date,date_time,method,url,error,stack_trace) values(?,?,?,?,?,?,?)",
                "test-aop",
                ldt.toLocalDate(),
                ldt,
                joinPoint.getSignature().getName(),
                request.getRequestURI(),
                messageUtils.getMessage(getLastCause(exception).getMessage(), Locale.RU),
                Arrays.toString(exception.getStackTrace()).replaceAll(",", ",\n")
        );
    }

    private Throwable getLastCause(Throwable in) {
        if (nonNull(in.getCause())) {
            return getLastCause(in.getCause());
        }
        return in;
    }

    @Around("notVoidControllerAspect()")//до и после + обработка ошибок
    public Object around(ProceedingJoinPoint pjp) {
        Object object;
        try {
            object = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            String error = getLastCause(e).getMessage();
            if (e instanceof TestAopException)
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .httpStatus(HttpStatus.BAD_REQUEST)
                                .errors(
                                        Collections.singletonList(messageUtils.getMessage(error))
                                ).build()
                );
            else
                return ResponseEntity.internalServerError().body(
                        ApiResponse.builder()
                                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                .errors(
                                        Collections.singletonList(messageUtils.getMessage(error))
                                ).build()
                );
        }
        return object;
    }

    @Around("voidControllerAspect()")//до и после + обработка ошибок
    public void aroundVoid(ProceedingJoinPoint pjp) throws VoidException {
        try {
            pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            String error = getLastCause(e).getMessage();
            throw new VoidException(error, !(e instanceof TestAopException));
        }
    }

}