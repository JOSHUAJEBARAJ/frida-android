/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  android.app.Application
 */
package android.arch.lifecycle;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ViewModelProvider {
    private static final String DEFAULT_KEY = "android.arch.lifecycle.ViewModelProvider.DefaultKey";
    private final Factory mFactory;
    private final ViewModelStore mViewModelStore;

    public ViewModelProvider(@NonNull ViewModelStore viewModelStore, @NonNull Factory factory) {
        this.mFactory = factory;
        this.mViewModelStore = viewModelStore;
    }

    public ViewModelProvider(@NonNull ViewModelStoreOwner viewModelStoreOwner, @NonNull Factory factory) {
        this(viewModelStoreOwner.getViewModelStore(), factory);
    }

    @MainThread
    @NonNull
    public <T extends ViewModel> T get(@NonNull Class<T> class_) {
        String string2 = class_.getCanonicalName();
        if (string2 != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("android.arch.lifecycle.ViewModelProvider.DefaultKey:");
            stringBuilder.append(string2);
            return this.get(stringBuilder.toString(), class_);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    @MainThread
    @NonNull
    public <T extends ViewModel> T get(@NonNull String string2, @NonNull Class<T> class_) {
        ViewModel viewModel = this.mViewModelStore.get(string2);
        if (class_.isInstance(viewModel)) {
            return (T)viewModel;
        }
        class_ = this.mFactory.create(class_);
        this.mViewModelStore.put(string2, (ViewModel)((Object)class_));
        return (T)class_;
    }

    public static class AndroidViewModelFactory
    extends NewInstanceFactory {
        private static AndroidViewModelFactory sInstance;
        private Application mApplication;

        public AndroidViewModelFactory(@NonNull Application application) {
            this.mApplication = application;
        }

        @NonNull
        public static AndroidViewModelFactory getInstance(@NonNull Application application) {
            if (sInstance == null) {
                sInstance = new AndroidViewModelFactory(application);
            }
            return sInstance;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> class_) {
            if (AndroidViewModel.class.isAssignableFrom(class_)) {
                ViewModel viewModel;
                try {
                    viewModel = (ViewModel)class_.getConstructor(Application.class).newInstance(new Object[]{this.mApplication});
                }
                catch (InvocationTargetException invocationTargetException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot create an instance of ");
                    stringBuilder.append(class_);
                    throw new RuntimeException(stringBuilder.toString(), invocationTargetException);
                }
                catch (InstantiationException instantiationException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot create an instance of ");
                    stringBuilder.append(class_);
                    throw new RuntimeException(stringBuilder.toString(), instantiationException);
                }
                catch (IllegalAccessException illegalAccessException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot create an instance of ");
                    stringBuilder.append(class_);
                    throw new RuntimeException(stringBuilder.toString(), illegalAccessException);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot create an instance of ");
                    stringBuilder.append(class_);
                    throw new RuntimeException(stringBuilder.toString(), noSuchMethodException);
                }
                return (T)viewModel;
            }
            return super.create(class_);
        }
    }

    public static interface Factory {
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> var1);
    }

    public static class NewInstanceFactory
    implements Factory {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> class_) {
            ViewModel viewModel;
            try {
                viewModel = (ViewModel)class_.newInstance();
            }
            catch (IllegalAccessException illegalAccessException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot create an instance of ");
                stringBuilder.append(class_);
                throw new RuntimeException(stringBuilder.toString(), illegalAccessException);
            }
            catch (InstantiationException instantiationException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cannot create an instance of ");
                stringBuilder.append(class_);
                throw new RuntimeException(stringBuilder.toString(), instantiationException);
            }
            return (T)viewModel;
        }
    }

}

