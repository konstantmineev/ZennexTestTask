package ru.zennex.zennextesttask.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.zennex.zennextesttask.volley.OnLoadMoreListener;
import ru.zennex.zennextesttask.parse.Quote;
import ru.zennex.zennextesttask.R;
import ru.zennex.zennextesttask.adapters.ParsingRecyclerViewAdapter;
import ru.zennex.zennextesttask.applications.MyApplication;
import ru.zennex.zennextesttask.dialogs.ErrorDialog;
import ru.zennex.zennextesttask.services.DownloadService;

import static ru.zennex.zennextesttask.services.DownloadService.ERROR_CONNECTION;
import static ru.zennex.zennextesttask.services.DownloadService.ERROR_RESPONSE;

/**
 * Created by Kostez on 23.09.2016.
 */

public class ParsingFragment extends Fragment {

    public static final String PARSING_FRAGMENT_TAG = "parsing_fragment_tag";
    public static final int PARSING_FRAGMENT_ID = 103;
    public static final int DOWNLOAD_CODE = 178;
    public final static String DOWNLOAD_PENDING_PARAM = "download_pending_param";

    public static final String ERROR_DIALOG_TAG = "error_dialog_tag";

    public final static int ERROR_TIME_OUT = 158;

    private String LOG_TAG = "ParsingFragment";

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private ParsingRecyclerViewAdapter adapter;
    private List<Quote> quotes = new ArrayList<>();
    private Intent intent;
    private ServiceConnection downloadServiceConnection;
    private DownloadService downloadService;
    private OnLoadMoreListener onLoadMoreListener;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler();
    private boolean isLoaded;
    private boolean isAllarmDialogShow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parsing, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_recycler_view);
        layoutManager = new LinearLayoutManager(MyApplication.getInstance().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ParsingRecyclerViewAdapter(quotes);
        recyclerView.setAdapter(adapter);

        isLoaded = false;
        isAllarmDialogShow = false;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intent = new Intent(MyApplication.getInstance().getApplicationContext(), DownloadService.class);

        downloadServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                downloadService = ((DownloadService.LocalBinder) iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        initViews();
    }

    private void initViews() {

        onLoadMoreListener = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                progressDialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!(isLoaded || isAllarmDialogShow)) {
                            Log.e(LOG_TAG, "timeOut");
                            System.out.println(isAllarmDialogShow);
                            showDialog(ERROR_TIME_OUT);
                        }
                    }
                }, 5000);

                PendingIntent pi = getActivity().createPendingResult(DOWNLOAD_CODE, intent, 0);
                Intent pendIntent = new Intent(getActivity(), DownloadService.class).putExtra(DOWNLOAD_PENDING_PARAM, pi);
                getActivity().startService(pendIntent);
            }
        };

        if (quotes.isEmpty()) {
            onLoadMoreListener.onLoadMore();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().stopService(new Intent(getActivity(), DownloadService.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().bindService(intent, downloadServiceConnection, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println();

        switch (resultCode) {
            case Activity.RESULT_OK:
                isLoaded = true;

                progressDialog.dismiss();

                for (int i = 0; i < downloadService.getQuotes().size(); i++) {
                    Quote quote = downloadService.getQuotes().get(i);
                    adapter.addItem(quote);
                    adapter.notifyItemInserted(quotes.size() - 1);
                }
                break;
            case ERROR_CONNECTION:
                showDialog(ERROR_CONNECTION);
                break;
            case ERROR_RESPONSE:
                showDialog(ERROR_RESPONSE);
                break;
        }
    }

    public void startDownload() {
        downloadService.download();
    }

    private void showDialog(int error) {
        System.out.println(error);
        isAllarmDialogShow = true;
        progressDialog.dismiss();
        ErrorDialog errorDialog = new ErrorDialog();
        String errorText = "";
        switch (error) {
            case ERROR_TIME_OUT:
                errorText = getResources().getString(R.string.timeout);
                break;
            case ERROR_CONNECTION:
                errorText = getResources().getString(R.string.no_internet_connection);
                break;
            case ERROR_RESPONSE:
                errorText = getResources().getString(R.string.no_response);
                break;
        }
        errorDialog.setMessage(errorText + ".\n" + getResources().getString(R.string.refresh_question));
        errorDialog.show(getFragmentManager(), ERROR_DIALOG_TAG);
    }
}
