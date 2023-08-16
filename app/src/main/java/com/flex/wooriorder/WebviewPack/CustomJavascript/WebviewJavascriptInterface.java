package com.flex.wooriorder.WebviewPack.CustomJavascript;

/**
 * Created by Moon on 2018. 4. 6..
 */

public interface WebviewJavascriptInterface {

    void fcmGo();
    void getLocation();
    void requestLocationPermission();
    void getLocationAuthStatus();
    void isAppInstalled(String appName);
    /*
    void kakaoShare(String text, String url, String idx);
    void fbShare(String text, String url, String idx);
    void fbLogin();
    void kakaoLogin();
    void googleLogin();
    */
}
