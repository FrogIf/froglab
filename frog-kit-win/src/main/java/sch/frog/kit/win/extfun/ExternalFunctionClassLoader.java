package sch.frog.kit.win.extfun;

import java.net.URL;
import java.net.URLClassLoader;

public class ExternalFunctionClassLoader extends URLClassLoader {

    public ExternalFunctionClassLoader(URL url, ClassLoader parent) {
        super(new URL[]{ url }, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz = null;  // 破坏双亲委派, 先从本地加载, 加载不到再走双亲委派
        try{
            clazz = this.findClass(name);
        }catch (ClassNotFoundException classNotFoundException){
            // do nothing
        }
        if(clazz == null){
            clazz = super.loadClass(name);
        }
        return clazz;
    }
}
