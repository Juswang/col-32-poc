package com.google.android.play.core.splitinstall;

import android.util.Log;
import androidx.annotation.Nullable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class az {
    private final XmlPullParser a;
    private final g b = new g();

    public az(XmlPullParser xmlPullParser) {
        this.a = xmlPullParser;
    }

    @Nullable
    private final String a(String str) {
        for (int i = 0; i < this.a.getAttributeCount(); i++) {
            if (this.a.getAttributeName(i).equals(str)) {
                return this.a.getAttributeValue(i);
            }
        }
        return null;
    }

    private final void b() throws IOException, XmlPullParserException {
        int i = 1;
        while (i != 0) {
            int next = this.a.next();
            if (next == 2) {
                i++;
            } else if (next == 3) {
                i--;
            }
        }
    }

    @Nullable
    public final h a() {
        String a;
        while (this.a.next() != 1) {
            try {
                if (this.a.getEventType() == 2) {
                    if (this.a.getName().equals("splits")) {
                        while (this.a.next() != 3) {
                            if (this.a.getEventType() == 2) {
                                if (!this.a.getName().equals("module") || (a = a("name")) == null) {
                                    b();
                                } else {
                                    while (this.a.next() != 3) {
                                        if (this.a.getEventType() == 2) {
                                            if (this.a.getName().equals("language")) {
                                                while (this.a.next() != 3) {
                                                    if (this.a.getEventType() == 2) {
                                                        if (this.a.getName().equals("entry")) {
                                                            String a2 = a("key");
                                                            String a3 = a("split");
                                                            b();
                                                            if (!(a2 == null || a3 == null)) {
                                                                this.b.a(a, a2, a3);
                                                            }
                                                        } else {
                                                            b();
                                                        }
                                                    }
                                                }
                                            } else {
                                                b();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        b();
                    }
                }
            } catch (IOException | IllegalStateException | XmlPullParserException e) {
                Log.e("SplitInstall", "Error while parsing splits.xml", e);
                return null;
            }
        }
        return this.b.a();
    }
}
