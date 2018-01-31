package xyz.kyleohanian.modelformsbinding.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RadioIntegerAttributes {
    String[] viewValues() default {};
    int[] internalValues() default {};
}