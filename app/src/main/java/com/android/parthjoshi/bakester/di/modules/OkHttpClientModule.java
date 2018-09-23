package com.android.parthjoshi.bakester.di.modules;

import android.content.Context;

import com.android.parthjoshi.bakester.di.appcontroller.ApplicationContext;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public abstract class OkHttpClientModule {

    @Provides
    @Singleton
    static Cache provideCache(File cacheFile){
        return new Cache(cacheFile, 10 * 1000 * 1000); // 10 MB
    }

    @Provides
    @Singleton
    static File provideFile(@ApplicationContext Context context){
        return new File(context.getCacheDir(), "HttpCache");
    }

    @Provides
    @Singleton
    static HttpLoggingInterceptor provideHttpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(Cache cache,
                                            HttpLoggingInterceptor httpLoggingInterceptor){
        return new OkHttpClient()
                .newBuilder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }
}
