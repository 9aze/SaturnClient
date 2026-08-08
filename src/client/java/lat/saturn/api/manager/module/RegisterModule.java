package lat.saturn.api.manager.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterModule {
    String name();
    String description() default "no description available";
    Category category();
    int bind() default -1;
    boolean alwaysActive() default false;
    boolean toggled() default false;
    boolean toggleInScreens() default false;
    Module.BindMode bindMode() default Module.BindMode.TOGGLE;
}
