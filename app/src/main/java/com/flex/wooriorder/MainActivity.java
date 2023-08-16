package com.flex.wooriorder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.flex.wooriorder.WebviewPack.WebPackMain;

import com.flex.wooriorder.R;

public class MainActivity extends AppCompatActivity {
    private WebPackMain wp;
    private RelativeLayout splashLayout;
    Thread timeThread=null;
    Handler handler = null;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 켜짐 유지
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_main);
         onScreenTouch();
     /*
        @SuppressLint({"ResourceType", "MissingInflatedId", "LocalSuppress"})
        ConstraintLayout mainLayout = (ConstraintLayout)findViewById(R.id.main_layout);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print(("mainlayout clicked"));
             }
        });

 */

        wp = new WebPackMain(this);
        splashLayout = (RelativeLayout)findViewById(R.id.splash_layout);

        wp.init(getString(R.string.webview_url), (ConstraintLayout)findViewById(R.id.main_layout), getPackageManager() )
                //.addPermissionList(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                //.addPermissionList(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //.addPermissionList(Manifest.permission.READ_MEDIA_IMAGES)
                //.addPermissionList(Manifest.permission.READ_MEDIA_VIDEO)
                //.addPermissionList(Manifest.permission.READ_MEDIA_AUDIO)
                //.addPermissionList(Manifest.permission.ACCESS_FINE_LOCATION)
                //.addPermissionList(Manifest.permission.POST_NOTIFICATIONS)
                //.addPermissionList(Manifest.permission.FOREGROUND_SERVICE)
                //.kakaoSetting(true)
                //.facebookSetting(true)
                //.requestPermissionList()
                .startLoadingWebview();

        splashLayout.bringToFront();
        //getAppKeyHash();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        splashLayout.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            try {
                String link = extras.getString("link");
                if (link != null && !link.isEmpty()) {
                    wp.getmWebview().loadUrl(UrlQuerySanitizer.getUrlAndSpaceLegal().sanitize(link));
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onScreenBrightnessChange(Float brightness){
         WindowManager.LayoutParams params = getWindow().getAttributes();
         params.screenBrightness= brightness;
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 getWindow().setAttributes(params);
             }
         });
    }
    protected void onScreenTouch() {
        onScreenBrightnessChange(0.7F);
        if(timeThread!=null) timeThread.interrupt(); timeThread=null;
        if(handler!=null) handler.removeCallbacksAndMessages(null); handler= null;

        timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("=========change brightness=============");
                                onScreenBrightnessChange(0.05F);
                                if(timeThread!=null) timeThread.interrupt();
                                timeThread=null;
                                handler= null;
                            }
                        }, 240000);
                    }
                });

            }
        });
        timeThread.start();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println("change brightness dispatchTouchEventdispatchTouchEvent dispatchTouchEvent");
        System.out.println("change brightness ev: "+ev.getAction());
        if(ev.getAction()==MotionEvent.ACTION_DOWN) {
            if (timeThread != null) timeThread.interrupt();
            if(handler!=null) handler.removeCallbacksAndMessages(null); handler= null;

            timeThread = null;
            onScreenTouch();
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void onPause() {
        super.onPause();
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(wp.getmWebview(), (Object[]) null);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onResume", (Class[]) null)
                    .invoke(wp.getmWebview(), (Object[]) null);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (wp.getmWebview() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getString("REDIRECT_URL")!=null) {
                    String redirectURL = getIntent().getExtras().getString("REDIRECT_URL");
                    if (!redirectURL.equals("")) {
                        wp.redirectURL((redirectURL));
                    }
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        wp.backPressClose();
        Log.d("tag","on backpress");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        wp.requestPermissionResult(requestCode, permissions, grantResults);
        return;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        wp.onActivityResult(requestCode, resultCode, data);
    }




}