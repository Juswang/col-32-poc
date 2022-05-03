package com.google.android.play.core.internal;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/* loaded from: classes2.dex */
public final class f extends g {
    private final byte[] a;

    public f(X509Certificate x509Certificate, byte[] bArr) {
        super(x509Certificate);
        this.a = bArr;
    }

    @Override // com.google.android.play.core.internal.g, java.security.cert.Certificate
    public final byte[] getEncoded() throws CertificateEncodingException {
        return this.a;
    }
}
