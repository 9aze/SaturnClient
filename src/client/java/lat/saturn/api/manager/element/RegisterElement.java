package lat.saturn.api.manager.element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterElement {
    String name();
    double x() default 0.0;
    double y() default 0.0;
    String description() default "no description available";
    boolean toggled() default false;
}