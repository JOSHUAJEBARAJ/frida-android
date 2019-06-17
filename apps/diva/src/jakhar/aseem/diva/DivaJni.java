/*
 * Decompiled with CFR 0_121.
 */
package jakhar.aseem.diva;

public class DivaJni {
    private static final String soName = "divajni";

    static {
        System.loadLibrary("divajni");
    }

    public native int access(String var1);

    public native int initiateLaunchSequence(String var1);
}

