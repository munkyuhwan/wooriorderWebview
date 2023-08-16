package com.flex.wooriorder.WebviewPack.CustomWebviewClient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.flex.wooriorder.MainActivity;
import com.flex.wooriorder.R;
import com.flex.wooriorder.WebviewPack.CustomWebviewSettings;
import com.flex.wooriorder.WebviewPack.WebPackMain;

/**
 * Created by Moon on 2018. 5. 12..
 */

public class CustomWebChrome extends WebChromeClient {

    Activity mActivity;
    WebPackMain mWebPackMain;
    MainActivity mMainActivity;

    //private PackageManager packageManager;

    public CustomWebChrome(Activity activity, WebPackMain webPackMain, MainActivity mainActivity) {
        super();
        this.mActivity = activity;
        this.mWebPackMain = webPackMain;
        //this.packageManager = packageManager;
        this.mMainActivity = mainActivity;
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

        System.out.println("new window open1!!!!");

        mWebPackMain.setChildWebview(new WebView(this.mMainActivity));
        //mWebViewPop.getSettings().setJavaScriptEnabled(true);
        //mWebViewPop.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //mWebViewPop.getSettings().setSupportMultipleWindows(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        mWebPackMain.getChildWebview().setLayoutParams(params);
        ConstraintLayout mConstraintLayout = (ConstraintLayout) mMainActivity.findViewById(R.id.main_layout);
        mConstraintLayout.addView(mWebPackMain.getChildWebview());

        CustomWebviewSettings webViewPopSetting = new CustomWebviewSettings(mWebPackMain.getChildWebview(), mWebPackMain, mMainActivity);
        webViewPopSetting.doWebviewSetting();

        WebView.WebViewTransport transportWebview =  (WebView.WebViewTransport) resultMsg.obj;
        transportWebview.setWebView(mWebPackMain.getChildWebview());
        resultMsg.sendToTarget();
        /*
        CustomWebviewSettings webviewPopSetting = new CustomWebviewSettings(mWebViewPop, mWebPackMain, mMainActivity);
        webviewPopSetting.doWebviewSetting();
        ConstraintLayout mainLayout = (ConstraintLayout) mMainActivity.findViewById(R.id.main_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        mWebViewPop.setLayoutParams(params);
        mainLayout.addView(mWebViewPop);
        System.out.println("resultMsg: " + resultMsg.getData());

        mWebViewPop.requestFocusNodeHref(resultMsg);

         */

        return true;
    }

    @Override
    public void onCloseWindow(WebView window) {
        System.out.println("on close window");
        window.setVisibility(View.GONE);
        window.destroy();


        super.onCloseWindow(window);
    }
    public void openFileChooser(ValueCallback<Uri[]> uploadMsg) {


        mWebPackMain.setmUploadMsg(uploadMsg);

        final File root = new File(Environment.getExternalStorageDirectory() + "/"+Environment.DIRECTORY_DCIM + "/Camera/" );
        root.mkdir();
        final String fname = "img_" + System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        mWebPackMain.setOutputFileUri(Uri.fromFile(sdImageMainDirectory));

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        final PackageManager packageManager = mMainActivity.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mWebPackMain.getOutputFileUri());
            intent.setData(mWebPackMain.getOutputFileUri());
            cameraIntents.add(intent);
            Log.d("df", "camera intent get: " + cameraIntents.get(0));
        }

        //FileSystem
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        //galleryIntent.addCategory(Intent.CATEGORY_APP_GALLERY);
        galleryIntent.setType("image/*");


        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "File Browser");

        // Add the camera options.
        Log.d("intent", "choose to array: " + cameraIntents.toArray(new Parcelable[]{}));

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        mMainActivity.startActivityForResult(chooserIntent, mWebPackMain.getRcFileChoose());

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        openFileChooser(filePathCallback);

        return true;
    }


}
