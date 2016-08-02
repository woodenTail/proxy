# proxy
静态代理和模拟JDK动态代理的实现
## 代理定义
简单来说,代理就是为一个对象提供一个替身或者占位符来控制对这个对象的访问,用一个对象来代替另一个对象

## 应用场景
① 系统日志记录
② 权限控制（符合一定条件才执行某方法）
③ 事务控制（方法执行之前开启事务，方法执行之后提交或回滚事务）

下述例子中,假设接口前后都要输出日志
```java
    public interface BusinessInterface {
        public void doSomthing();
    }
    
    public class RealBusinessImpl implements BusinessInterface{
        @Override
        public void doSomthing() {
            System.out.println("RealBusinessImpl do somthing");
        }
    }
```

```java
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
```
可以看出,上述静态代理只能代理特定的接口,我们想实现一个代理类能为所有接口代理,于是引出动态代理

## 实现原理
![图片描述](https://dn-raysnote.qbox.me/p%2Fnotes%2F4b06c53d3f4e0c2 "图片标题")

几要素:

 - 代理类的生成(是代理接口的实现类)
 
 - 代理对象的生成
   通过反射机制获得动态代理类的构造函数，其唯一参数类型是调用处理器类接口类型,
   通过构造函数创建动态代理类实例，构造时调用处理器对象作为参数被传入
 - 代理目标对象(真正的接口实现类)
 - 调用处理器[根据传入的代理目标对象及方法签名,反射调用方法]

代理类是在运行时生成的接口实现类,创建代理对象时,根据接口类型,生成相应的代理类
``` java
 BusinessServiceImpl service = new BusinessServiceImpl();
 BusinessService businessService =
                (BusinessService)     MyProxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces()[0], new  ProxyInvocationHandler(service));

businessService.doSomthing();
```

```java
public class MyProxy {
    public static <T> T newProxyInstance(ClassLoader loader, Class interfaces, InvocationHandler h){

        String srcStr = "";    // 代理类$Proxy0的源码, 字符串形式
        String methodStr = "";    // 代理类$Proxy0的所有代理方法, 字符串形式
        String rt  = "\r\n";    // Windows平台下的换行符

        Method[] method = interfaces.getMethods();
        // 动态生成代理类$Proxy0的所有代理方法
        for(Method m : method){
            methodStr+="@Override"+rt+
                    "public void "+m.getName()+"()"+rt+"{" + rt +
                    "   try {"+rt+
                    "         Method md="+interfaces.getName()+".class.getMethod(\""+m.getName()+"\");"+rt+
                    "            h.invoke(this,md,null);"+rt+
                    "  } catch(Throwable e){e.printStackTrace();}"+rt+

                    "}";
        }

        // 拼接代理类$Proxy0的源码
        srcStr +=
                "package proxy.proxy0729;" + rt +
                        "import java.lang.reflect.Method;" + rt +
                        "import java.lang.reflect.InvocationHandler;" + rt +
                        "public class $Proxy0 implements " + interfaces.getName() + "{" + rt +
                        "	private InvocationHandler h;" + rt +
                        "	public $Proxy0(InvocationHandler h) {" + rt +
                        "		this.h = h;" + rt +
                        "	}" + rt +
                        "  "+methodStr + rt +
                        "}";

        String fileName = "D:/workspace/li-res/src/main/java/proxy/proxy0729/$Proxy0.java";
        File srcFile = new File(fileName);
        try {
            FileWriter fileWriter = new FileWriter(srcFile);
            fileWriter.write(srcStr);
            fileWriter.flush();
            fileWriter.close();
            //获取java编译器
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            //获取文件管理者
            StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);
            //获取文件
            Iterable<? extends JavaFileObject> compilationUnits  = fileMgr.getJavaFileObjects(srcFile);
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileMgr, null, null, null, compilationUnits);
            task.call(); //编译
            //获取类加载器
            ClassLoader classLoader = MyProxy.class.getClassLoader();
            //加载代理类
            Class<?> clazz = classLoader.loadClass("proxy.proxy0729.$Proxy0");
            Constructor<?> constructor = clazz.getConstructor(InvocationHandler.class);
            T proxy = (T) constructor.newInstance(h);
            return  proxy;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
```
当程序运行后,生成的动态实现类如下:
```java
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
```
代理类有接口的全部方法实现,每个实现都是使用调度器去处理真正的逻辑,调度器中必须有一个代理的目标对象,一般是真正的接口实现类,然后使用反射的方式进行方法调用
```java
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
```



