package proxy.proxy0729;

/**
 * Title: proxy<br>
 * Description: <br>
 * Copyright: Copyright (c) 2016<br>

 *
 * @author lili 2016/6/9
 */
public class Main {
    public  static void main(String [] args){
        //代理包括三部分:
        //根据接口类型 生成代理对象
        //调度处理器:每次方法调用时都调用调度处理器,反射的方式去调用接口实现

        BusinessServiceImpl service = new BusinessServiceImpl();
        BusinessService businessService =
                (BusinessService) MyProxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces()[0], new ProxyInvocationHandler(service));

        businessService.doSomthing();
    }
}
