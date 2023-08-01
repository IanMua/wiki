package com.ianmu.wiki.aspect;

import com.ianmu.wiki.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 定义切点
     */
    @Pointcut("execution(public * com.ianmu.*.controller..*Controller.*(..))")
    public void controllerPointcut() {
    }

    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {

    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        //开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(attributes)) {
            LOG.error("请求获取失败");
            return proceedingJoinPoint.proceed();
        }
        HttpServletRequest request = attributes.getRequest();
        Signature signature = proceedingJoinPoint.getSignature();
        String name = signature.getName();

        //打印请求信息
        LOG.info("---------- 开始 ----------");
        LOG.info("请求地址 : {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("类名方法 : {}.{}", signature.getDeclaringTypeName(), name);
        LOG.info("远程地址 : {}", request.getRemoteAddr());

        //打印请求参数
        Object[] args = proceedingJoinPoint.getArgs();
        LOG.info("请求参数 : {}", JsonUtil.toJson(args));

        Object result = proceedingJoinPoint.proceed();

        //打印响应数据
        LOG.info("返回结果 : {}", JsonUtil.toJson(result));
        LOG.info("---------- 结束 耗时 : {} ms ----------", System.currentTimeMillis() - startTime);

        return result;
    }
}
