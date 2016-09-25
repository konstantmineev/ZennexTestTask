package ru.zennex.zennextesttask.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import ru.zennex.zennextesttask.R;
import ru.zennex.zennextesttask.views.TouchImageView;

/**
 * Created by Kostez on 24.09.2016.
 */

public class PictureFragment extends Fragment {

    public static final String PICTURE_FRAGMENT_TAG = "picture_fragment_tag";
    private Bitmap bitmap;
    private Toolbar toolbar;
    private TouchImageView touchImageView;

    public PictureFragment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        touchImageView = (TouchImageView) getActivity().findViewById(R.id.picture_fragment_iv);
        touchImageView.setImageBitmap(bitmap);
        FloatingActionButton fabZoomIn = (FloatingActionButton) getActivity().findViewById(R.id.fab_zoom_in);
        FloatingActionButton fabZoomOut = (FloatingActionButton) getActivity().findViewById(R.id.fab_zoom_out);

        fabZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touchImageView.setZoom(touchImageView.getCurrentZoom()+0.1f);
            }
        });

        fabZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touchImageView.setZoom(touchImageView.getCurrentZoom()-0.1f);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        setFullscreen();
    }

    public void setFullscreen() {
        setFullscreen(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbar.setVisibility(View.VISIBLE);
        exitFullscreen(getActivity());
    }

    public void setFullscreen(Activity activity) {

        if (Build.VERSION.SDK_INT > 10) {
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;

            if (isImmersiveAvailable()) {
                flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        } else {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void exitFullscreen(Activity activity) {
        if (Build.VERSION.SDK_INT > 10) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    public static boolean isImmersiveAvailable() {
        return android.os.Build.VERSION.SDK_INT >= 19;
    }

}
