package com.android.parthjoshi.bakester.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.Repository;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class WidgetUpdateService extends IntentService {

    @Inject Repository repository;

    private static final String ACTION_UPDATE_WIDGETS = "com.android.parthjoshi.bakester.widget" +
            ".UPDATE_WIDGETS";
    private static final String ACTION_RESET_WIDGETS = "com.android.parthjoshi.bakester.widget" +
            "REFRESH_WIDGETS";


    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent != null){
            if(ACTION_UPDATE_WIDGETS.equalsIgnoreCase(intent.getAction()))
                handleWidgetUpdate();
            else if (ACTION_RESET_WIDGETS.equalsIgnoreCase(intent.getAction()))
                handleWidgetReset();
        }
    }

    private void handleWidgetReset() {
        repository.clearWidgetData();

        handleWidgetUpdate();
    }

    private void handleWidgetUpdate() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateAppWidgets(getApplicationContext(), appWidgetManager, widgetIds, repository);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widget_ingredient_list);
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    public static void widgetUpdate(Context context){
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_WIDGETS);
        context.startService(intent);
    }

    public static void widgetReset(Context context){
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_RESET_WIDGETS);
        context.startService(intent);
    }
}
