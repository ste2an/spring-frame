package Bean;

import annotation.Provider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class AnnotationApplicationContext implements ApplicationContext{


    private Map<Class, Object> beanFactory = new HashMap<>();
    private String rootPath;
    @Override
    public Object getBean(Class clazz) {
        return beanFactory.get(clazz);
    }

    public AnnotationApplicationContext() {
    }

    // 根据传入的路径，扫描package，寻找@Provider 注解，有的话通过反射创建对象并放入beanFactory
    public AnnotationApplicationContext(String basePackage) throws Exception {
        // 把 '.'  替换成 '\'
        String packagePath = basePackage.replaceAll("\\.", "/");

        // 根据路径获得a list of URLs
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packagePath);

        while(urls.hasMoreElements()) {
            // 扫描所有子包，把含有 @Provider 的class都通过反射创建实例
            URL url = urls.nextElement();
            String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
            rootPath = filePath.substring(0, filePath.length() - basePackage.length());
            try {
                loadBean(new File(filePath));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void loadBean(File file) throws Exception {
        // add instance into the map
        if(file.isDirectory()) {
            // check if the root path is a directory
            File[] subFiles = file.listFiles();

            // if no child or the children are empty, return
            if(subFiles == null || subFiles.length == 0) return;

            for( File child : subFiles) {
                if(child.isDirectory()) {
                    // if the child is directory, recursively loadBean()
                    loadBean(child);
                } else {
                    // the child is not a directory, get the relative file path
                    String pathWithClass = child.getAbsolutePath().substring(rootPath.length());
                    // check if the file is a .class file
                    if(pathWithClass.contains(".class")) {
                        String fileName = pathWithClass.replaceAll("/", "\\.").replace(".class", "");
                        Class<?> aClass = Class.forName(fileName);

                        // skip the interface, create instance only for implementation class
                        if(!aClass.isInterface()) {
                            // check whether the file contains @Provider
                           Provider provider = (Provider) aClass.getAnnotation(Provider.class);
                           if (provider != null) {
                               // if there is a @Provider annotation, create instance using Reflect
                               Object o = aClass.getDeclaredConstructor().newInstance();
                               // if the class is a implementation class of a interface, use the interface as key,
                               if (aClass.getInterfaces().length > 0) {
                                   beanFactory.put(aClass.getInterfaces()[0], o);
                               } else {
                                   // else use the current class as the key
                                   beanFactory.put(aClass, o);
                               }
                           }
                        }

                    }
                }
            }

        }
    }
}
