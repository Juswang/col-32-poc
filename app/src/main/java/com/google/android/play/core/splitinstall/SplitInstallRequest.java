package com.google.android.play.core.splitinstall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class SplitInstallRequest {
    private final List<String> a;
    private final List<Locale> b;

    /* loaded from: classes2.dex */
    public static class Builder {
        private final List<String> a = new ArrayList();
        private final List<Locale> b = new ArrayList();

        private Builder() {
        }

        /* synthetic */ Builder(byte[] bArr) {
        }

        @NonNull
        public Builder addLanguage(@Nullable Locale locale) {
            this.b.add(locale);
            return this;
        }

        public Builder addModule(String str) {
            this.a.add(str);
            return this;
        }

        @NonNull
        public SplitInstallRequest build() {
            return new SplitInstallRequest(this);
        }
    }

    /* synthetic */ SplitInstallRequest(Builder builder) {
        this.a = new ArrayList(builder.a);
        this.b = new ArrayList(builder.b);
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder(null);
    }

    public List<Locale> getLanguages() {
        return this.b;
    }

    public List<String> getModuleNames() {
        return this.a;
    }

    public String toString() {
        return String.format("SplitInstallRequest{modulesNames=%s,languages=%s}", this.a, this.b);
    }
}
