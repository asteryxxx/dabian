package net.sppan.base.config.aop;

import net.sppan.base.dao.ILogDao;
import net.sppan.base.entity.Logs;
import net.sppan.base.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Aspect
@Component
public class WebControllerAop {
    /**
     * 指定切点
     * 匹配 net.sppan.base.controller包及其子包下的所有类的所有方法
     */
    @Pointcut("execution(* net.sppan.base.controller..*.*(..))")
    public void webLog() {

    }

    @Autowired
    ILogDao logDao;
    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 获取HttpServletRequest
        ServletRequestAttributes srvletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = srvletRequestAttributes.getRequest();
        Map<String, String[]> map = request.getParameterMap();

        //    System.out.println(request.getParameterMap()+"----");
       // System.out.println(Arrays.toString(joinPoint.getArgs())+"----");

        Logs log=new Logs();
        Subject sb = SecurityUtils.getSubject();
        User user = (User) sb.getPrincipal();
        if(user!=null){
            log.setUserId(user.getId());
            log.setUserName(user.getUserName());
        }else{
            log.setUserId(-1);
            log.setUserName("");
        }
        System.out.println("我是前置通知!!!");
        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        Enumeration<String> num = request.getParameterNames();
        StringBuffer str = new StringBuffer();
        while(num.hasMoreElements()){
            String  paramName=(String)num.nextElement();
            String[]  values=request.getParameterValues(paramName);
            for(int  i=0;i<values.length;i++){

                 str .append(paramName+"=");
                str.append(values[i]+",");
            }
        }
        log.setParams(str.toString());

        Map<String, String> heades = getHeadersInfo(request);
        for (Map.Entry<String, String> entry : heades.entrySet()) {
            if(entry.getKey().equals("user-agent")){
                log.setUserAgent(entry.getValue());//设置浏览器
            }
        }
        //代理的是哪一个方法
    //    System.out.println("方法：" + signature.getName());

        log.setTitle(signature.getName());
        log.setLastTime(new Date());
        //AOP代理类的名字
      //  System.out.println("方法所在包:" + signature.getDeclaringTypeName());
        //AOP代理类的类（class）信息
        signature.getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
      //  log.setParams(request.getQueryString());
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        // 记录下请求内容
        log.setRequestUri( req.getRequestURL().toString());
    //    System.out.println("HTTP_METHOD : " + req.getMethod());
        log.setMethod(req.getMethod());
        boolean addlog=true;
        if(signature.getName().equals("initBinder")||signature.getName().equals("index")||signature.getName().equals("welcome")){
            addlog=false;
        }
            if(addlog) {
                logDao.save(log);
            }
    }
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 处理完请求返回内容
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        System.out.println("方法的返回值 : " + ret);
    }
    /**
     * 后置异常通知
     * @param jp
     */
    @AfterThrowing(pointcut = "webLog()",throwing = "e")
    public void throwss(JoinPoint jp,Exception e){
        if (!(e instanceof ServiceException)) {
            System.out.println("捕获到了！！！！"+e.getMessage());
        }
        System.out.println("方法异常时执行.....");
    }
    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     * @param jp
     */
    @After("webLog()")
    public void after(JoinPoint jp){

    }

    /**
     * 环绕通知,环绕增强，相当于MethodInterceptor
     * @param pjp
     * @return
     */
    @Around("webLog()")
    public Object arround(ProceedingJoinPoint pjp) {
        try {
            Object o =  pjp.proceed();
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

}
