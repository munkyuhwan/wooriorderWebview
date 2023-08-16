package com.flex.wooriorder.WebviewPack;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.flex.wooriorder.WebviewPack.CustomJavascript.CustomJavascriptInterface;
import com.flex.wooriorder.WebviewPack.CustomWebviewClient.CustomWebChrome;
import com.flex.wooriorder.WebviewPack.CustomWebviewClient.CustomWebviewClient;
import com.flex.wooriorder.MainActivity;

import static android.content.Context.DOWNLOAD_SERVICE;

public class CustomWebviewSettings {
    private WebView mWebview;
    private WebPackMain mWebPackMain;
    private MainActivity mainActivity;

    public CustomWebviewSettings(WebView webView, WebPackMain webPackMain, MainActivity mainActivity) {
        this.mWebview = webView;
        this.mWebPackMain = webPackMain;
        this.mainActivity = mainActivity;
    }

    public void doWebviewSetting() {

        this.mWebview.getSettings().setUseWideViewPort(true);
        this.mWebview.getSettings().setLoadWithOverviewMode(true);


        this.mWebview.getSettings().setJavaScriptEnabled(true);
        this.mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.mWebview.getSettings().setSupportMultipleWindows(true);
        this.mWebview.getSettings().setMediaPlaybackRequiresUserGesture(false);

        this.mWebview.addJavascriptInterface(new CustomJavascriptInterface(this.mWebPackMain ), "Android");
        this.mWebview.setWebViewClient(new CustomWebviewClient(this.mainActivity));
        this.mWebview.setWebChromeClient(new CustomWebChrome(this.mWebPackMain, this.mWebPackMain, this.mainActivity));

        /*
        this.mWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                System.out.println("view: "+view);
                System.out.println("url: "+view.getUrl());
                System.out.println("isDialog: "+isDialog);
                System.out.println("isUserGesture: "+isUserGesture);
                System.out.println("resultMsg: "+resultMsg);
                System.out.println("resultMsg: "+resultMsg.getData());
                WebView.WebViewTransport transportWebview =  (WebView.WebViewTransport) resultMsg.obj;
                transportWebview.setWebView(new WebView(mainActivity));
                resultMsg.sendToTarget();
                return true;
            }
        });
        */

        //this.mWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //this.mWebview.getSettings().setAppCacheEnabled(true);

        this.mWebview.getSettings().setAllowFileAccess(true);
        this.mWebview.getSettings().setDomStorageEnabled(true);
        this.mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        this.mWebview.getSettings().setAllowContentAccess(true);
        this.mWebview.getSettings().setAllowFileAccessFromFileURLs(true);
        this.mWebview.getSettings().setAllowUniversalAccessFromFileURLs(true);

        this.mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        this.mWebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        this.mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.mWebview.getSettings().setEnableSmoothTransition(true);

        this.mWebview.setWebContentsDebuggingEnabled(true);

        // this.mWebview.getSettings().setGeolocationDatabasePath(getFilesDir().getPath());
        this.mWebview.getSettings().setGeolocationEnabled(true);

        //this.mWebview.getSettings().setAppCacheEnabled(false);
        this.mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.mWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        String userAgent = this.mWebview.getSettings().getUserAgentString();
        this.mWebview.getSettings().setUserAgentString(userAgent+" ANDROID_WEBVIEW ");

        this.mWebview.getSettings().setDatabaseEnabled(true);
        this.mWebview.getSettings().setDomStorageEnabled(true);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(this.mWebview, true);


        this.mWebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimeType);
                //------------------------COOKIE!!------------------------
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                //------------------------COOKIE!!------------------------
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) mainActivity.getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(mainActivity.getApplicationContext(), "다운로드를 시작합니다.", Toast.LENGTH_LONG).show();
            }
        });

    }
}
