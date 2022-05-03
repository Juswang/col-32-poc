package com.google.android.play.core.internal;

import java.io.PrintStream;

/* loaded from: classes2.dex */
public final class ci {
    static final cc a;

    static {
        cc ccVar;
        Integer num = null;
        try {
            try {
                num = (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
            } catch (Exception e) {
                System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
                e.printStackTrace(System.err);
            }
            ccVar = (num == null || num.intValue() < 19) ? !Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic") ? new cf() : new cg() : new ch();
        } catch (Throwable th) {
            PrintStream printStream = System.err;
            String name = cg.class.getName();
            StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 133);
            sb.append("An error has occurred when initializing the try-with-resources desuguring strategy. The default strategy ");
            sb.append(name);
            sb.append("will be used. The error is: ");
            printStream.println(sb.toString());
            th.printStackTrace(System.err);
            ccVar = new cg();
        }
        a = ccVar;
        if (num != null) {
            num.intValue();
        }
    }

    public static void a(Throwable th, Throwable th2) {
        a.a(th, th2);
    }
}
