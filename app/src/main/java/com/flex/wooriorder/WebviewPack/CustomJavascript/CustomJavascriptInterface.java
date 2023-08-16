package com.flex.wooriorder.WebviewPack.CustomJavascript;

import android.webkit.JavascriptInterface;

/**
 * Created by Moon on 2018. 4. 6..
 */

public class CustomJavascriptInterface {

    private WebviewJavascriptInterface mInterface;

    public CustomJavascriptInterface(WebviewJavascriptInterface mInterface) {
        this.mInterface = mInterface;
    }

    @JavascriptInterface
    public void get_fcm_token() {
        mInterface.fcmGo();
    }

    @JavascriptInterface
    public void getCurrentPosition() {
        mInterface.getLocation();
    }

    @JavascriptInterface
    public void requestLocationPermission() {
        mInterface.requestLocationPermission();
    }

    @JavascriptInterface
    public void getLocationAuthStatus() {
        mInterface.getLocationAuthStatus();
    }

    @JavascriptInterface
    public void isAppInstalled(String appName){mInterface.isAppInstalled(appName);}
/*
    @JavascriptInterface
    public void kakao_share(String text, String url, String idx) {
        mInterface.kakaoShare(text, url, idx);
    }
    @JavascriptInterface
    public void kakao_login() {
        mInterface.kakaoLogin();
    }

    @JavascriptInterface
    public void fb_login() {
        mInterface.fbLogin();
    }

    @JavascriptInterface
    public void fb_share(String text, String url, String idx) {
        mInterface.fbShare(text, url, idx);
    }

    @JavascriptInterface
    public void google_login() {
        mInterface.googleLogin();
    }
*/
}
