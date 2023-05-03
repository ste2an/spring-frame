package Test;

import Bean.AnnotationApplicationContext;
import Bean.ApplicationContext;
import org.junit.jupiter.api.Test;
import service.UserService;
import service.impl.UserServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class TestUser {

    @Test
    public void testProvider() throws IOException {
        try {
            ApplicationContext app = new AnnotationApplicationContext("service.impl");
            UserServiceImpl u = (UserServiceImpl) app.getBean(UserService.class);
            u.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
