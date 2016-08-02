package proxy.bussiness;

/**
 * Title: proxy<br>
 * Description: <br>
 * Copyright: Copyright (c) 2016<br>

 *
 * @author lili 2016/6/9
 */
public class ProxyBusiness implements BusinessInterface {
    private BusinessInterface realBusiness;

    public ProxyBusiness(BusinessInterface realBusiness) {
        this.realBusiness = realBusiness;
    }

    @Override
    public void doSomthing() {
        System.out.println("服务开始");

        realBusiness.doSomthing();

        System.out.println("服务结束");

    }
}
