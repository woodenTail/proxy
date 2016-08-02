package proxy.bussiness;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Title: proxy<br>
 * Description: <br>
 * Copyright: Copyright (c) 2016<br>

 *
 * @author lili 2016/6/10
 */
public class BusinessInvocationHandler implements InvocationHandler {
    private BusinessInterface businessInterface;

    public BusinessInvocationHandler(BusinessInterface businessInterface) {
        this.businessInterface = businessInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }
}
