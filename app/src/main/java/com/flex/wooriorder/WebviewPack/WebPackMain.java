package com.flex.wooriorder.WebviewPack;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.flex.wooriorder.WebviewPack.CustomJavascript.WebviewJavascriptInterface;
import com.flex.wooriorder.WebviewPack.EtcUtils.BackPressHandler;
import com.flex.wooriorder.WebviewPack.EtcUtils.MyGeoLocaion;
import com.flex.wooriorder.WebviewPack.EtcUtils.SetWebviewPermission;
import com.flex.wooriorder.WebviewPack.Variables.Variables;
/*
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
*/

import com.flex.wooriorder.MainActivity;
import com.flex.wooriorder.R;

import java.util.HashMap;
import java.util.Map;

public class WebPackMain extends Activity implements WebviewJavascriptInterface {

    MainActivity mMainActivity;

    public WebView getmWebview() {
        return mWebview;
    }

    public void setmWebview(WebView mWebview) {
        this.mWebview = mWebview;
    }

    private WebView mWebview;
    private WebView childWebview;

    private String url;
    private BackPressHandler backPressCloseHandler;

    private int REQUEST_PERMISSION_CODE = 998;

    private static final int RC_FILE_CHOOSE = 2833;
    private ValueCallback<Uri[]> mUploadMsg = null;
    private Uri outputFileUri;

    //private CallbackManager callbackManager;

    private String FCM_TOKEN = "";

    public WebPackMain(MainActivity mainActivity ) {
        //this.currentActivity = activity;
        this.mMainActivity = mainActivity;

    }


    private ConstraintLayout mConstraintLayout;
    private CustomWebviewSettings settings;

    private SetWebviewPermission setPermission;

    private PackageManager mPackageManager;

    public WebPackMain init(String url, ConstraintLayout constraintLayout, PackageManager pm) {
        this.url = url;
        this.mConstraintLayout = constraintLayout;

        backPressCloseHandler = new BackPressHandler(mMainActivity); //뒤로가기 버튼 종료 클래스
        setPermission = new SetWebviewPermission(mMainActivity, REQUEST_PERMISSION_CODE);

        mWebview = new WebView(this.mMainActivity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        mWebview.setLayoutParams(params);

        this.mConstraintLayout.addView(mWebview);
        settings = new CustomWebviewSettings(this.mWebview,this, mMainActivity);//웹뷰 설정 세팅
        settings.doWebviewSetting();

        this.mPackageManager = this.mMainActivity.getPackageManager();
        //getFCMToken();// FCM토큰 받아오기
        //callbackManager = CallbackManager.Factory.create();

        return this;
    }

    public void redirectURL(String url) {
        this.mWebview.loadUrl( UrlQuerySanitizer.getUrlAndSpaceLegal().sanitize(url) );
    }

    public WebPackMain addPermissionList(String permissionList) {
        setPermission.addPermission(permissionList);
        return this;
    }

    public WebPackMain requestPermissionList() {
        setPermission.checkPermissions();
        return this;
    }

    public WebPackMain setHeader(Map<String, String> header) {
        Variables.headers = header;
        return this;
    }

    public WebPackMain kakaoSetting(boolean setBool) {
        if (setBool) {
            kakaoSetting(this.mConstraintLayout);
        }
        return this;
    }

    public WebPackMain facebookSetting(boolean setBool) {
        if (setBool) {
            facebookSetting();
        }
        return this;
    }

    private void kakaoSetting(ConstraintLayout constraintLayout) {
    }

    private void facebookSetting() {

    }


    public void startLoadingWebview() {
        Map<String, String> headers = new HashMap<>();
        headers.put("app-platform", "Android");
        String userAgent = mWebview.getSettings().getUserAgentString();
        mWebview.getSettings().setUserAgentString(userAgent+" / android-app");

        String finalUrl = UrlQuerySanitizer.getUrlAndSpaceLegal().sanitize(url);
        mWebview.loadUrl(finalUrl,headers);//웹뷰 로드
        //FirebaseApp.initializeApp(mMainActivity);
    }


    public void backPressClose() {

        if(childWebview!= null) {

            childWebview.destroy();
            ConstraintLayout mConstraintLayout = (ConstraintLayout) mMainActivity.findViewById(R.id.main_layout);
            mConstraintLayout.removeViewAt(2);
            childWebview=(null);
        }else {
            if (mWebview.canGoBack()) {
                if (mWebview.getUrl().equals(mMainActivity.getString(R.string.webview_url))) {
                    backPressCloseHandler.onBackPressed();
                } else {
                    mWebview.goBack();
                }
            } else {
                backPressCloseHandler.onBackPressed();
            }
        }
    }

    public void requestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if ( !setPermission.checkPermission() ) {

                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(mMainActivity.getApplicationContext(),mMainActivity.getString(R.string.app_dismiss),Toast.LENGTH_SHORT).show();
                        //mMainActivity.finish();
                    }
                });

            } else {

            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        /*
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
         */
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_FILE_CHOOSE && mUploadMsg != null) {
            //파일 선택후 처리
            Uri[]result = null;
            if ( data != null || resultCode == RESULT_OK ) {
                result = new Uri[1];
                if (data != null) {
                    result[0] = data.getData();
                } else {
                    result[0] = outputFileUri;
                }
                mUploadMsg.onReceiveValue(result);
                mUploadMsg = null;
                return;
            }else {
                mUploadMsg.onReceiveValue(result);
                mUploadMsg = null;
                return;
            }
        }else if(requestCode == Variables.RC_SIGN_IN) {

        }else {

        }

    }





    public void setmUploadMsg(ValueCallback<Uri[]> mUploadMsg) {
        this.mUploadMsg = mUploadMsg;
    }

    public void setOutputFileUri(Uri outputFileUri) {
        this.outputFileUri = outputFileUri;
    }

    public static int getRcFileChoose() {
        return RC_FILE_CHOOSE;
    }

    public ValueCallback<Uri[]> getmUploadMsg() {
        return mUploadMsg;
    }

    public Uri getOutputFileUri() {
        return outputFileUri;
    }

    @Override
    public void fcmGo() {
/*
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                        Log.e("token:::", token);
                        mWebview.loadUrl("javascript:set_fcm_token(\""+token+"\");");

                    }
                });

 */
    }

    @Override
    public void getLocation() {
        MyGeoLocaion.getInstance().getLocation(mMainActivity);
        double latitude = MyGeoLocaion.getMyLatitude();
        double longitude = MyGeoLocaion.getMyLongitude();
        Log.e("geo location result: ", "latitude: "+latitude+" longitude: "+longitude);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebview.loadUrl("javascript:currentLocationResult(\""+latitude+","+longitude+"\");");
            }
        });

    }

    @Override
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(mMainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSION_CODE);
    }

    @Override
    public void getLocationAuthStatus() {
        int permissionStatus = ContextCompat.checkSelfPermission(mMainActivity, Manifest.permission.ACCESS_FINE_LOCATION );
        System.out.println("permission status: "+ (String.valueOf(permissionStatus)));
        String statusResult = "";
        switch (permissionStatus) {
            case 0 :
                statusResult = "GRANTED";
                break;
            case 1:
                statusResult = "DENIED";
                break;
            default:
                statusResult = "";
                break;
        }
        String finalStatusResult = statusResult;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebview.loadUrl("javascript:setLocationAuthStatus(\""+ finalStatusResult +"\");");
            }
        });
    }

    @Override
    public void isAppInstalled(String appName) {
        try {
            PackageInfo packageInfo = mMainActivity.getPackageManager().getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebview.loadUrl("javascript:setAppInstalled(\""+appName+"\",true);");
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebview.loadUrl("javascript:setAppInstalled(\""+appName+"\",false);");
                }
            });
        }
    }

    public WebView getChildWebview() {
        return childWebview;
    }

    public void setChildWebview(WebView childWebview) {
        this.childWebview = childWebview;
    }
}
