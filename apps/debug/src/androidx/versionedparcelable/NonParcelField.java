/*
 * Decompiled with CFR 0_121.
 */
package androidx.versionedparcelable;

import android.support.annotation.RestrictTo;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.SOURCE)
@Target(value={ElementType.FIELD})
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public @interface NonParcelField {
}

