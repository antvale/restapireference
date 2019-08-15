package net.av.code.references.restapi.aop;


import java.lang.reflect.Field;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.av.code.references.restapi.aop.annotations.AuditItem;

@Aspect
@Component
public class AuditAspect {

    private static Logger LOG = LoggerFactory.getLogger(AuditAspect.class);
    
    private ThreadLocal<String> auditTraceInfo = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return new String("");
        }
    };

    //pointcut for controllers
    @Pointcut("execution(* net.av.code.references.restapi.controllers.**.*(..))")
    public void controllerMethods() {
    }

    //pointcut for services
    @Pointcut("@annotation(net.av.code.references.restapi.aop.annotations.Auditable)")
    public void auditMethods() {
    }

    @AfterReturning("auditMethods()")
    public void addExtraInfo(JoinPoint jp){
        final Field[] fields = jp.getClass().getDeclaredFields();
        for (final Field field : fields) {
            AuditItem a=field.getAnnotation(AuditItem.class);
            if (a!=null){
                LOG.info("Audit::: Field: "+field.getName());
                try {
                LOG.info("Audit::: Value: "+field.get(jp.getThis()).toString());
            } catch (IllegalAccessException e){
                LOG.error("Error while attempting to access the value of the field", e);
            }
            }
        }
        auditTraceInfo.set("traceInfo:pippo");
        LOG.info("Auditable Activated: "+auditTraceInfo.get().toString());
    }

    @AfterReturning("controllerMethods()")
    public void makeAudit(){
        LOG.info("Audit::: SAVE AUDIT WITH EXTRA PARAMETERS: "+auditTraceInfo.get().toString());

        //remove threadlocal data before exit
        auditTraceInfo.remove();
        LOG.info("Audit::: Threadlocal removed!");
    }

}