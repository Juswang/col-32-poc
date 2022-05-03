package androidx.core.os;

import androidx.annotation.Nullable;

/* loaded from: classes2.dex */
public class OperationCanceledException extends RuntimeException {
    public OperationCanceledException() {
        this(null);
    }

    public OperationCanceledException(@Nullable String message) {
        super(message != null ? message : "The operation has been canceled.");
    }
}
