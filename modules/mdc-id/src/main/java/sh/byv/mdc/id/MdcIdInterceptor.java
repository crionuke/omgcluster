package sh.byv.mdc.id;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.logging.MDC;

import java.util.UUID;

@WithMdcId
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
class MdcIdInterceptor {

    static final String MDC_ID = "mdcId";

    @AroundInvoke
    Object intercept(final InvocationContext context) throws Exception {
        MDC.put(MDC_ID, UUID.randomUUID().toString().substring(0, 8));
        try {
            return context.proceed();
        } finally {
            MDC.remove(MDC_ID);
        }
    }
}
