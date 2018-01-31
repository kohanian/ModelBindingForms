package xyz.kyleohanian.modelformsbinding.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelBindingAttributes {
    String createTitle() default "";
    String updateTitle() default "";
    int titleSize() default 20;
    int padding() default 0;
}