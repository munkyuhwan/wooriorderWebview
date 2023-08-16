package com.flex.wooriorder.WebviewPack.EtcUtils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.flex.wooriorder.MainActivity;

public class MyGeoLocaion {
    private static final MyGeoLocaion ourInstance = new MyGeoLocaion();
    private static double myLatitude = 0;
    private static double myLongitude = 0;

    public static MyGeoLocaion getInstance() {
        return ourInstance;
    }

    private MyGeoLocaion() {
    }

    public static double getMyLatitude() {
        return myLatitude;
    }

    public static double getMyLongitude() {
        return myLongitude;
    }

    public MyGeoLocaion getLocation(MainActivity mActivity) {

        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        double latitude=0;
        double longitude=0;

        if (isGPSEnable && isNetworkEnable) {
            if (ActivityCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                myLatitude = latitude;
                myLongitude = longitude;
                Log.i("디버깅","값확인좀 = "+latitude+"/"+longitude);
            }else{
                Location locations = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(locations!=null){
                    latitude = locations.getLatitude();
                    longitude = locations.getLongitude();
                    myLatitude = latitude;
                    myLongitude = longitude;
                    Log.i("디버깅","값확인좀 = "+latitude+"/"+longitude);
                }
            }
        }
        return this;
    }
}
