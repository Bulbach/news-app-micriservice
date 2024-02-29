package by.alex.newsappmicriservice.cache.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Pointcut который соответствует всем репозиториям, службам и конечным точкам Web REST..
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    /**
     * Pointcut, который сопоставляет все Spring beans в основных пакетах приложения.
     */
    @Pointcut("within(by.alex..*)" +
            " || within(by.alex.newsappmicriservice.service..*)" +
            " || within(by.alex.newsappmicriservice.controller..*)")
    public void applicationPackagePointcut() {
    }

    /**
     * Совет, который регистрирует методы, выбрасывающие исключения.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }

    /**
     * Совет, который регистрирует вход и выход из метода.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }

    /**
     * Pointcut который соответствует всем контроллерам Spring MVC.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }

    /**
     * Совет, который регистрирует входящие запросы к контроллерам.
     *
     * @param joinPoint join point for advice
     */
    @Before("controllerPointcut()")
    public void logIncomingRequest(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        log.info("Incoming request to {} from IP: {}", wrappedRequest.getRequestURI(), wrappedRequest.getRemoteAddr());

        log.info("Request method: {}", wrappedRequest.getMethod());

        log.info("Request headers:");
        Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("{}: {}", headerName, wrappedRequest.getHeader(headerName));
        }

        // Log parameters
        log.info("Request parameters:");
        wrappedRequest.getParameterMap().forEach((key, value) -> log.info("{}: {}", key, value));

        if (!wrappedRequest.getMethod().equals("GET")) {
            try {
                byte[] content = wrappedRequest.getContentAsByteArray();
                String body = new String(content, wrappedRequest.getCharacterEncoding());
                log.info("Request body: {}", body);
            } catch (IOException e) {
                log.error("Error reading request body", e);
            }
        }
    }
}
