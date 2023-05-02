package Bean;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class AnnotationApplicationContext implements ApplicationContext{


    private final Map<Class, Object> beanFactory = new HashMap<>();
    private String rootPath;
    @Override
    public Object getBean(Class clazz) {
        return null;
    }

    public AnnotationApplicationContext() {
    }

    // 根据传入的路径，扫描package，寻找@Provider 注解，有的话通过反射创建对象并放入beanFactory
    public AnnotationApplicationContext(String basePackage) throws IOException {
        // 把 '.'  替换成 '\'
        String packagePath = basePackage.replaceAll("\\.", "\\\\");

        // 根据路径获得a list of URLs
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packagePath);

        while(urls.hasMoreElements()) {
            // 扫描所有子包，把含有 @Provider 的class都通过反射创建实例
            URL url = urls.nextElement();
            String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
            rootPath = filePath.substring(0, filePath.length() - basePackage.length());
        }

    }

    private void loadBean(File file) {

    }

    public static void main(String[] args) {
        try {
            AnnotationApplicationContext test = new AnnotationApplicationContext("dao");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
