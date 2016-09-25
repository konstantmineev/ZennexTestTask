package ru.zennex.zennextesttask.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.FileNotFoundException;

import ru.zennex.zennextesttask.R;

import static ru.zennex.zennextesttask.fragments.PictureFragment.PICTURE_FRAGMENT_TAG;

/**
 * Created by Kostez on 24.09.2016.
 */

public class ScalingFragment extends Fragment {

    public static final String SCALING_FRAGMENT_TAG = "scaling_fragment_tag";
    public static final int SCALING_FRAGMENT_ID = 102;


    private static final int IMAGE_CAMERA = 0;
    private static final int IMAGE_GALLERY = 1;

    private ImageButton cameraButton;
    private ImageButton galleryButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scaling, container, false);

        cameraButton = (ImageButton) view.findViewById(R.id.button_camera);
        galleryButton = (ImageButton) view.findViewById(R.id.button_gallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromCamera();
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });

        return view;
    }

    public void pickImageFromGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, IMAGE_GALLERY);
    }

    public void pickImageFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, IMAGE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("--- resulcode " + resultCode);
        System.out.println("--- requestcode " + requestCode);

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_GALLERY) {

            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {

                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));

                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                PictureFragment pictureFragment = new PictureFragment(bitmap);

                fragmentTransaction.replace(R.id.content_main, pictureFragment, PICTURE_FRAGMENT_TAG);
                fragmentTransaction.commit();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println();
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            PictureFragment pictureFragment = new PictureFragment(bitmap);


            fragmentTransaction.replace(R.id.content_main, pictureFragment, PICTURE_FRAGMENT_TAG);
            fragmentTransaction.commit();



        }


    }
}
