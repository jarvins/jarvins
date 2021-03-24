package dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvocationHandlerImpl implements InvocationHandler {

    Object proxyClass;

    public InvocationHandlerImpl(Object proxyClass) {
        this.proxyClass = proxyClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before method executed...");
        Object invoke = method.invoke(proxyClass, args);
        System.out.println("after method executed...");
        return invoke;
    }
}
