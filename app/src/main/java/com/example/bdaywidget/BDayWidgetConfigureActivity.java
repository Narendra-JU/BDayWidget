package com.example.bdaywidget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * The configuration screen for the {@link BDayWidget BDayWidget} AppWidget.
 */
public class BDayWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.example.bdaywidget.BDayWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    TextView bdw_bday_name_id;

    /*View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = BDayWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = bdw_bday_name_id.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            BDayWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
*/
    public BDayWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.b_day_widget_configure);
        setupButton();
        bdw_bday_name_id = (EditText) findViewById(R.id.bdw_bday_name_id);


        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        bdw_bday_name_id.setText(loadTitlePref(BDayWidgetConfigureActivity.this, mAppWidgetId));
    }
    private void setupButton()
    {
        Button b =
                (Button)this.findViewById(R.id.bdw_button_update_bday_widget);
        b.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v)
                    {
                        parentButtonClicked(v);
                    }
                });

    }

    private void parentButtonClicked(View v)
    {
        String name = this.getName();
        String date = this.getDate();
        if (Utils.validateDate(date) == false)
        {
            this.setDate("wrong date:" + date);
            return;
        }
        if (this.mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            Log.d("WidgetProject", "invalid app widget id");
            return;
        }
        updateAppWidgetLocal(name,date);
        Intent resultValue = new Intent();
        resultValue.putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
    private String getName()
    {
        EditText nameEdit =
                (EditText)this.findViewById(R.id.bdw_bday_name_id);
        String name = nameEdit.getText().toString();
        return name;
    }
    private String getDate()
    {
        EditText dateEdit =
                (EditText)this.findViewById(R.id.bdw_bday_date_id);
        String dateString = dateEdit.getText().toString();
        return dateString;
    }
    private void setDate(String errorDate)
    {
        EditText dateEdit =
                (EditText)this.findViewById(R.id.bdw_bday_date_id);
        dateEdit.setText("error");
        dateEdit.requestFocus();
    }

    private void updateAppWidgetLocal(String name, String dob) {
        BDayWidgetModel m = new BDayWidgetModel(this.mAppWidgetId, name, dob);
        updateAppWidget(this, AppWidgetManager.getInstance(this), m);
        m.savePreferences(this);
    }

    public static void updateAppWidget(Context context,
                                       AppWidgetManager appWidgetManager,
                                       BDayWidgetModel widgetModel)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.b_day_widget);

        views.setTextViewText(R.id.bdw_w_name
                , widgetModel.getName() + ":" + widgetModel.iid);

        views.setTextViewText(R.id.bdw_w_date
                , widgetModel.getBday());

        //update the name
        views.setTextViewText(
                R.id.bdw_w_days,Long.toString(widgetModel.howManyDays()));

        Intent defineIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.google.com"));
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context,
                        0 /* no requestCode */,
                        defineIntent,
                        0 /* no flags */);
        views.setOnClickPendingIntent(
                R.id.bdw_w_button_buy, pendingIntent);

        // Tell the widget manager
        appWidgetManager.updateAppWidget(widgetModel.iid, views);
    }


}

