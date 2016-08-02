package proxy.proxy0729;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Title: proxy<br>
 * Description: <br>
 * Copyright: Copyright (c) 2016<br>

 *
 * @author lili 2016/6/10
 */
public class ProxyInvocationHandler implements InvocationHandler {

    private Object target;//被代理的目标对象

    public ProxyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            System.out.println("start invoke");
            return method.invoke(target, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
