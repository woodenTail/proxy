package proxy.proxy0729;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

public class $Proxy0 implements proxy.proxy0729.BusinessService {
    private InvocationHandler h;

    public $Proxy0(InvocationHandler h) {
        this.h = h;
    }

    @Override
    public void doSomthing() {
        try {
            Method md = proxy.proxy0729.BusinessService.class.getMethod("doSomthing");
            h.invoke(this, md, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}