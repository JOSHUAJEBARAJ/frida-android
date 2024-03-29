/*
 * Decompiled with CFR 0_121.
 */
package android.support.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.FIELD})
public @interface Size {
    public long max() default Long.MAX_VALUE;

    public long min() default Long.MIN_VALUE;

    public long multiple() default 1;

    public long value() default -1;
}

