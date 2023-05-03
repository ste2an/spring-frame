package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)  // 在哪里能用该注解
@Retention(RetentionPolicy.RUNTIME) // 在什么时候能用该注解
public @interface Provider {
}
