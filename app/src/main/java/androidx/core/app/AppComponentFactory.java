package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.lang.reflect.InvocationTargetException;

@RequiresApi(28)
/* loaded from: classes2.dex */
public class AppComponentFactory extends android.app.AppComponentFactory {
    @Override // android.app.AppComponentFactory
    public final Activity instantiateActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Activity) CoreComponentFactory.checkCompatWrapper(instantiateActivityCompat(cl, className, intent));
    }

    @Override // android.app.AppComponentFactory
    public final Application instantiateApplication(ClassLoader cl, String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Application) CoreComponentFactory.checkCompatWrapper(instantiateApplicationCompat(cl, className));
    }

    @Override // android.app.AppComponentFactory
    public final BroadcastReceiver instantiateReceiver(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (BroadcastReceiver) CoreComponentFactory.checkCompatWrapper(instantiateReceiverCompat(cl, className, intent));
    }

    @Override // android.app.AppComponentFactory
    public final ContentProvider instantiateProvider(ClassLoader cl, String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (ContentProvider) CoreComponentFactory.checkCompatWrapper(instantiateProviderCompat(cl, className));
    }

    @Override // android.app.AppComponentFactory
    public final Service instantiateService(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Service) CoreComponentFactory.checkCompatWrapper(instantiateServiceCompat(cl, className, intent));
    }

    @NonNull
    public Application instantiateApplicationCompat(@NonNull ClassLoader cl, @NonNull String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (Application) Class.forName(className, false, cl).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }

    @NonNull
    public Activity instantiateActivityCompat(@NonNull ClassLoader cl, @NonNull String className, @Nullable Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (Activity) Class.forName(className, false, cl).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }

    @NonNull
    public BroadcastReceiver instantiateReceiverCompat(@NonNull ClassLoader cl, @NonNull String className, @Nullable Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (BroadcastReceiver) Class.forName(className, false, cl).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }

    @NonNull
    public Service instantiateServiceCompat(@NonNull ClassLoader cl, @NonNull String className, @Nullable Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (Service) Class.forName(className, false, cl).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }

    @NonNull
    public ContentProvider instantiateProviderCompat(@NonNull ClassLoader cl, @NonNull String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (ContentProvider) Class.forName(className, false, cl).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }
}
