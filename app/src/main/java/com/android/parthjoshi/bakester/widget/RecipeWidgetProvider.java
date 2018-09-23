package com.android.parthjoshi.bakester.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.Repository;
import com.android.parthjoshi.bakester.view.MenuActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    @Inject Repository repository;

    private static final String TAG = RecipeWidgetProvider.class.getSimpleName();

    private static final String ACTION_WIDGET_REFRESH = "com.android.parthjoshi.bakester.widget.REFRESH";

    private static final int REQUEST_CODE_REFRESH = 0;
    private static final int REQUEST_CODE_RECIPE = 1;
    private static final int REQUEST_CODE_BLANK = 2;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Repository repository) {

        try {
            // Construct the RemoteViews object
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

            String appName = context.getString(R.string.app_name);

            //int recipeId = repository.getRecipeId();
            String recipeName = repository.getRecipeName();

            Intent intent = new Intent(context, RecipeWidgetService.class);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            if(recipeName == null){
                remoteViews.setTextViewText(R.id.recipe_name, appName);

                // Launch app when empty widget is clicked
                Intent emptyWidgetIntent = new Intent(context, MenuActivity.class);
                PendingIntent emptyWidgetPI = PendingIntent.getActivity(context, REQUEST_CODE_BLANK,
                        emptyWidgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.recipe_name, emptyWidgetPI);

                remoteViews.setViewVisibility(R.id.widget_ingredient_list, View.GONE);

            } else {
                remoteViews.setViewVisibility(R.id.widget_ingredient_list, View.VISIBLE);
                remoteViews.setTextViewText(R.id.recipe_name, recipeName);

                Intent widgetRefreshIntent = new Intent(context, RecipeWidgetProvider.class);
                widgetRefreshIntent.setAction(ACTION_WIDGET_REFRESH);
                PendingIntent widgetRefreshPI = PendingIntent.getBroadcast(context, REQUEST_CODE_REFRESH,
                        widgetRefreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.widget_refresh, widgetRefreshPI);

                Intent recipeIntent = new Intent(context, MenuActivity.class);
                //recipeIntent.putExtra(MenuActivity.EXTRA_RECIPE_ID, recipeId);
                //recipeIntent.putExtra(MenuActivity.EXTRA_RECIPE_NAME, recipeName);
                PendingIntent recipePI = PendingIntent.getActivity(context, REQUEST_CODE_RECIPE,
                        recipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.recipe_name, recipePI);

                remoteViews.setRemoteAdapter(R.id.widget_ingredient_list, intent);
            }

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredients_list);
        } catch (Exception e) {
            Log.d(TAG, "Error in UpdateAppWidget: " + e.toString());
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                         int[] appWidgetIds, Repository repository){
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, repository);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAppWidgets(context, appWidgetManager, appWidgetIds, repository);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AndroidInjection.inject(this, context);

        super.onReceive(context, intent);

        if(intent != null) {
            if (ACTION_WIDGET_REFRESH.equalsIgnoreCase(intent.getAction())){
                WidgetUpdateService.widgetReset(context);
            }
        }
    }

    public static void updateWidgets(Context context){
        WidgetUpdateService.widgetUpdate(context);
    }
}

