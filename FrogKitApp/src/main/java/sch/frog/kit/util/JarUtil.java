package sch.frog.kit.util;

import sch.frog.kit.common.ExternalViewStruct;
import sch.frog.kit.common.LogKit;

import java.io.File;
import java.io.FilenameFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {

    private static final String PATH = "plugins";

    private static void loadJarFile(){
        File dir = new File(PATH);
        String[] jarFiles = dir.list((dir1, name) -> name.endsWith(".jar"));
        if(jarFiles != null){
            for (String jarFile : jarFiles) {
                System.out.println(jarFile);
            }
        }
    }

    public static void load() throws Exception {
        loadJarFile();
//        URLClassLoader urlClassLoader = null;
//        try {
//            URL url = new URL(PATH);
//            urlClassLoader = new URLClassLoader(new URL[]{ url });
//            JarURLConnection connection = (JarURLConnection) url.openConnection();
//
//            JarFile jarFile = connection.getJarFile();
//            Enumeration<JarEntry> entries = jarFile.entries();
//            while(entries.hasMoreElements()){
//                JarEntry jarEntry = entries.nextElement();
//                if(jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")){
//                    continue;
//                }
//                String clazzName = jarEntry.getName();
//                clazzName = clazzName.substring(0, clazzName.length() - 6);
//                clazzName = clazzName.replace("/", ".");
//                Class<?> clazz = urlClassLoader.loadClass(clazzName);
//                if(clazz.isAssignableFrom(ExternalViewStruct.class)){
//                    System.out.println("find it : " + clazzName);
//                }
//            }
//        } finally {
//            if(urlClassLoader != null){
//                urlClassLoader.close();
//            }
//        }
    }

}
