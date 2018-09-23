package com.android.parthjoshi.bakester.di.appcontroller;

import android.app.Application;
import android.content.Context;

import com.android.parthjoshi.bakester.data.Repository;
import com.android.parthjoshi.bakester.data.db.RecipeDB;
import com.android.parthjoshi.bakester.di.modules.OkHttpClientModule;
import com.android.parthjoshi.bakester.data.networking.APIInterface;
import com.android.parthjoshi.bakester.data.networking.Path;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = OkHttpClientModule.class)
public abstract class AppModule {

    @Provides
    @Singleton
    @ApplicationContext
    static Context provideApplicationContext(Application application){
        return application;
    }

    @Provides
    @Singleton
    static APIInterface provideAPIInterface(Retrofit retrofit){
        return retrofit.create(APIInterface.class);
    }

    @Provides
    @Singleton
    static String baseUrl(){
        return Path.BASE_URL;
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(OkHttpClient okHttpClient, GsonConverterFactory factory, String baseUrl){
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .build();
    }

    @Provides
    @Singleton
    static GsonConverterFactory provideGsonConverterFactory(){
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    static RecipeDB provideRecipeDB(@ApplicationContext Context context){
        return RecipeDB.getInstance(context);
    }

    @Provides
    @Singleton
    static Repository provideRepository(@ApplicationContext Context context, APIInterface apiInterface, RecipeDB db){
        return new Repository(context, apiInterface, db);
    }
}
