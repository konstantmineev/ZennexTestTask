package ru.zennex.zennextesttask.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.Formatter;
import java.util.List;

import ru.zennex.zennextesttask.R;
import ru.zennex.zennextesttask.applications.MyApplication;

/**
 * Created by Kostez on 24.09.2016.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationButtonClickListener {

    public static final String MAP_FRAGMENT_TAG = "scaling_fragment_tag";
    public static final int MAP_FRAGMENT_ID = 104;

    private static final int REQUEST_LOCATION = 2;
    private LocationManager locManager;
    private MapView mapView;
    private GoogleMap map;
    private String LOG_TAG = "--- MapFragment";
    private TextView ctvCoordinates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        ctvCoordinates = (TextView) v.findViewById(R.id.tv_coordinates);

        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mapView = (MapView) v.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        locManager.removeUpdates(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(LOG_TAG, "onMapReady");
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
//        onMyLocationButtonClick();
    }

    private LatLng getMyLocation() {
        Location location = getLastKnownLocation();
        LatLng myLocation = null;
        if (location != null) {
            double myLatitude = location.getLatitude();
            double myLongitude = location.getLongitude();

            Formatter formatter = new Formatter();
            formatter.format("%f\n%f", myLatitude, myLongitude);

            ctvCoordinates.setText(formatter.toString());

            myLocation = new LatLng(myLatitude, myLongitude);
        }
        locManager.removeUpdates(this);
        return myLocation;
    }

    private Location getLastKnownLocation() {
        locManager = (LocationManager) MyApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(getMyLocation(), 18);
        map.animateCamera(cameraUpdate);
        return true;
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(MyApplication.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.getInstance(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }
}
