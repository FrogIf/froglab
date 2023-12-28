package sch.frog.kit.win.extfun;

import sch.frog.kit.lang.ext.fun.FunctionController;
import sch.frog.kit.lang.ext.fun.FunctionDefine;
import sch.frog.kit.lang.fun.AbstractGeneralFunction;
import sch.frog.kit.lang.fun.FunctionInfo;
import sch.frog.kit.lang.fun.GeneralFunctionWrapper;
import sch.frog.kit.lang.fun.IFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

public class ExternalFunctionLoadUtil {

    public static final String PATH_SPLITTER = "/";

    public static List<FunctionPackage> load(String jarPath) throws Exception {
        HashMap<String, List<IFunction>> funMap = new HashMap<>();
        ExternalFunctionClassLoader classLoader = new ExternalFunctionClassLoader(new File(jarPath).toURI().toURL(), ExternalFunctionLoadUtil.class.getClassLoader());
        List<String> classList = findFileFromClasspath(classLoader, "sch/frog/kit/ext", ".*\\.class");
        for (String classPath : classList) {
            String classRef = pathToPackage(classPath);
            classRef = classRef.substring(0, classRef.length() - ".class".length());
            Class<?> clazz = classLoader.loadClass(classRef);
            FunctionController anno = clazz.getAnnotation(FunctionController.class);
            if(anno != null){
                String name = anno.packageName();
                List<IFunction> list = funMap.computeIfAbsent(name, k -> new ArrayList<>());
                List<AbstractGeneralFunction> funList = load(clazz.getConstructor().newInstance());
                list.addAll(funList);
            }
        }

        ArrayList<FunctionPackage> functionPackageList = new ArrayList<>(funMap.size());
        for (Map.Entry<String, List<IFunction>> entry : funMap.entrySet()) {
            FunctionPackage pak = new FunctionPackage();
            pak.setName(entry.getKey());
            pak.setFunctions(entry.getValue());
            functionPackageList.add(pak);
        }
        return functionPackageList;
    }

    public static List<AbstractGeneralFunction> load(Object instance){
        ArrayList<AbstractGeneralFunction> list = new ArrayList<>();
        Class<?> clazz = instance.getClass();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            FunctionDefine funDefine = m.getAnnotation(FunctionDefine.class);
            if(funDefine != null){
                FunctionInfo functionInfo = new FunctionInfo(funDefine.name(), funDefine.description());
                list.add(new GeneralFunctionWrapper(functionInfo, instance, m));
            }
        }
        return list;
    }

    private static String pathToPackage(String path){
        StringBuilder sb = new StringBuilder();
        for(int i = 0, len = path.length(); i < len; i++){
            char ch = path.charAt(i);
            if(ch == '/'){
                sb.append('.');
            }else{
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 包扫描, 遍历获取classpath下的文件
     * @param classLoader 加载包使用的类加载器
     * @param path 扫描的包内路径
     * @param regex 过滤匹配的正则
     * @return 返回扫描到的文件
     */
    private static List<String> findFileFromClasspath(ClassLoader classLoader, String path, String regex) throws Exception {
        Pattern pattern = (regex == null || "".equals(regex.trim())) ? Pattern.compile(".*") : Pattern.compile(regex);
        URL url = classLoader.getResource(path);
        ArrayList<URL> urlList = new ArrayList<>();
        if(url == null){
            if(classLoader instanceof URLClassLoader){
                URL[] urLs = ((URLClassLoader) classLoader).getURLs();
                String suffix = path.startsWith("/") ? path : ("/" + path);
                for (URL u : urLs) {
                    URL uu;
                    if("jar".equals(u.getProtocol())){
                        uu = new URL(u + "!" + suffix);
                    }else{
                        uu = new URL("jar:" + u + "!" + suffix);
                    }
                    urlList.add(uu);
                }
            }
            if(urlList.isEmpty()){
                throw new IllegalArgumentException("no path find for path : " + path);
            }
        } else{
            urlList.add(url);
        }
        ArrayList<String> resources = new ArrayList<>();
        for (URL u : urlList) {
            loadResources(u, pattern, resources);
        }
        ArrayList<String> results = new ArrayList<>();
        if(!path.isEmpty() && !path.endsWith("/")){
            path += "/";
        }
        for (String resource : resources) {
            results.add(path + resource);
        }
        return results;
    }

    private static void loadResources(URL url, Pattern pattern, ArrayList<String> resources) throws Exception {
        String scheme = url.toURI().getScheme();
        if(scheme.equals("file")){
            File file = new File(url.toURI());
            if(!file.exists()){ return; }

            if(file.isDirectory()){
                resources.addAll(retrieveFile(file, pattern));
            }else if(file.getName().endsWith(".jar")){
                try(
                        JarFile jarFile = new JarFile(file)
                ){
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while(entries.hasMoreElements()){
                        JarEntry jarEntry = entries.nextElement();
                        if(!pattern.matcher(jarEntry.getName()).matches()){ continue; }
                        resources.add(jarEntry.getName());
                    }
                }
            }else if(pattern.matcher(file.getName()).matches()){
                resources.add(file.getName());
            }
        }else if(scheme.equals("jar")){
            try{
                List<String> fileList = retrieveJarFile(url, pattern);
                resources.addAll(fileList);
            }catch (FileNotFoundException e){
                // do nothing
            }
        }
    }

    private static List<String> retrieveFile(File dir, Pattern pattern){
        ArrayList<String> files = new ArrayList<>();
        String rootPath = dir.getAbsolutePath();
        if(!rootPath.endsWith(File.separator)){
            rootPath = rootPath + File.separator;
        }
        doRetrieveFile(rootPath, dir, pattern, files);
        return files;
    }

    private static void doRetrieveFile(String rootPath, File dir, Pattern pattern, List<String> fileList){
        File[] files = dir.listFiles();
        if(files == null){ return; }
        for (File file : files) {
            if(file.isDirectory()){
                doRetrieveFile(rootPath, file, pattern, fileList);
            }else{
                if(pattern.matcher(file.getName()).matches()){
                    fileList.add(fixPath(file.getAbsolutePath().substring(rootPath.length())));
                }
            }
        }
    }

    private static String fixPath(String path){
        StringBuilder sb = new StringBuilder();
        for(int i = 0, len = path.length(); i < len; i++){
            char ch = path.charAt(i);
            if(ch == File.separatorChar){
                sb.append(PATH_SPLITTER);
            }else{
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private static List<String> retrieveJarFile(URL rootDirURL, Pattern pattern) throws IOException {
        URLConnection con = rootDirURL.openConnection();
        con.setUseCaches(false);
        JarFile jarFile;
        String jarFileUrl;
        String rootEntryPath;
        if (con instanceof JarURLConnection) {
            JarURLConnection jarCon = (JarURLConnection)con;
            jarFile = jarCon.getJarFile();
            JarEntry jarEntry = jarCon.getJarEntry();
            rootEntryPath = jarEntry != null ? jarEntry.getName() : "";
        } else {
            String urlFile = rootDirURL.getFile();

            try {
                int separatorIndex = urlFile.indexOf("*/");
                if (separatorIndex == -1) {
                    separatorIndex = urlFile.indexOf("!/");
                }

                if (separatorIndex != -1) {
                    jarFileUrl = urlFile.substring(0, separatorIndex);
                    rootEntryPath = urlFile.substring(separatorIndex + 2);
                    jarFile = new JarFile(jarFileUrl);
                } else {
                    jarFile = new JarFile(urlFile);
                    rootEntryPath = "";
                }

            } catch (ZipException var17) {
                return Collections.emptyList();
            }
        }

        try {
            if (!rootEntryPath.isEmpty() && !rootEntryPath.endsWith(PATH_SPLITTER)) {
                rootEntryPath = rootEntryPath + PATH_SPLITTER;
            }

            ArrayList<String> result = new ArrayList<>();
            Enumeration<JarEntry> entries = jarFile.entries();

            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if(entry.isDirectory()){ continue; }
                String entryPath = entry.getName();
                if (entryPath.startsWith(rootEntryPath)) {
                    String relativePath = entryPath.substring(rootEntryPath.length());
                    if(!pattern.matcher(relativePath).matches()){ continue; }
                    result.add(relativePath);
                }
            }

            return result;
        } finally {
            jarFile.close();
        }
    }

}
