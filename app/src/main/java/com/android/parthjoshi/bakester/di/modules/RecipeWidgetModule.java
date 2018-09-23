package com.android.parthjoshi.bakester.di.modules;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.di.appcontroller.ApplicationContext;
import com.android.parthjoshi.bakester.view.MenuActivity;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class RecipeWidgetModule {

    @Provides
    static RemoteViews provideRemoteViews(@ApplicationContext Context context){
        return new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
    }

    @Provides
    static Intent provideMenuIntent(@ApplicationContext Context context){
        return new Intent(context, MenuActivity.class);
    }

    @Provides
    static PendingIntent provideMenuActivityPendingIntent(@ApplicationContext Context context, Intent intent){
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}
