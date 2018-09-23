package com.android.parthjoshi.bakester.di.appcontroller;

import android.app.Application;

import com.android.parthjoshi.bakester.application.MyApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Component(modules = {AppModule.class, AndroidInjectionModule.class, ComponentBuilder.class})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(MyApp app);
}
