package proxy.bussiness;

/**
 * Title: proxy<br>
 * Description: <br>
 * Copyright: Copyright (c) 2016<br>

 *
 * @author lili 2016/6/9
 */
public class RealBusinessImpl implements BusinessInterface{
    @Override
    public void doSomthing() {
        System.out.println("RealBusinessImpl do somthing");
    }
}
