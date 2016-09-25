package ru.zennex.zennextesttask.services;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.zennex.zennextesttask.volley.Config;
import ru.zennex.zennextesttask.parse.Quote;
import ru.zennex.zennextesttask.applications.MyApplication;

import static ru.zennex.zennextesttask.applications.MyApplication.hasConnection;
import static ru.zennex.zennextesttask.fragments.ParsingFragment.DOWNLOAD_PENDING_PARAM;

/**
 * Created by Kostez on 23.09.2016.
 */

public class DownloadService extends Service {

    private String LOG_TAG = "DownloadService";
    public static final int ERROR_CONNECTION = 156;
    public static final int ERROR_RESPONSE = 157;

    private PendingIntent pendingIntent;
    private List<Quote> quotes = new ArrayList<>();
    protected Binder binder;

    public class LocalBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
        binder = new LocalBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pendingIntent = intent.getParcelableExtra(DOWNLOAD_PENDING_PARAM);
        Log.d(LOG_TAG, "onStartCommand");
        download();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void download() {
        this.getData();
    }

    private void getData() {
        if (hasConnection(MyApplication.getInstance().getApplicationContext())) {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, Config.DATA_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                parseData(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, "onErrorResponse");
                            try {
                                pendingIntent.send(ERROR_RESPONSE);
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance().getApplicationContext());
            requestQueue.add(jsObjRequest);
        } else {
            Log.e(LOG_TAG, "No connection");
            try {
                pendingIntent.send(ERROR_CONNECTION);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseData(JSONObject jsObjRequest) throws JSONException {

        int total = jsObjRequest.getInt(Config.TAG_TOTAL);
        String last = jsObjRequest.getString(Config.TAG_LAST);
        JSONArray jsonArrayQuotes = jsObjRequest.getJSONArray(Config.TAG_QUOTES);

        for (int i = 0; i < jsonArrayQuotes.length(); i++) {
            JSONObject json = jsonArrayQuotes.getJSONObject(i);
            int id = json.getInt(Config.TAG_ID);
            String description = json.getString(Config.TAG_DESCRIPTION);
            String time = json.getString(Config.TAG_TIME);
            int rating = json.getInt(Config.TAG_RATING);
            Quote quote = new Quote(id, description, time, rating);

            quotes.add(quote);
        }

        if (!quotes.isEmpty()) {
            try {
                pendingIntent.send(Activity.RESULT_OK);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }
}
