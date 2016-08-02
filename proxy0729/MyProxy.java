package proxy.proxy0729;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lili 2016/7/30
 */
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

}
