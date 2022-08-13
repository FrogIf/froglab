package sch.frog.kit.util;

import sch.frog.kit.common.ExternalViewStruct;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginUtil {

    private static final String PATH = "plugins";

    public static List<ExternalViewStruct> loadExternalViewStruct() throws Exception {
        File[] files = new File(PATH).listFiles((d, name) -> name.endsWith(".jar"));
        ArrayList<ExternalViewStruct> structs = new ArrayList<>();
        if(files != null){
            URL[] urls = new URL[files.length];
            for (int i = 0; i < files.length; i++) {
                urls[i] = new URL("file:" + files[i].getAbsolutePath());
            }
            try(
                    URLClassLoader classLoader = new URLClassLoader(urls);
                    ){
                for (File file : files) {
                    JarFile jarFile = new JarFile(file);
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while(entries.hasMoreElements()){
                        JarEntry jarEntry = entries.nextElement();
                        if(jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")){
                            continue;
                        }
                        String clazzName = jarEntry.getName();
                        clazzName = clazzName.substring(0, clazzName.length() - 6);
                        clazzName = clazzName.replace("/", ".");
                        Class<?> clazz = classLoader.loadClass(clazzName);
                        if(ExternalViewStruct.class.isAssignableFrom(clazz)){
                            @SuppressWarnings("unchecked")
                            Constructor<ExternalViewStruct> constructor = (Constructor<ExternalViewStruct>) clazz.getConstructor();
                            ExternalViewStruct externalViewStruct = constructor.newInstance();
                            structs.add(externalViewStruct);
                        }
                    }
                }
            }
        }
        return structs;
    }

}
