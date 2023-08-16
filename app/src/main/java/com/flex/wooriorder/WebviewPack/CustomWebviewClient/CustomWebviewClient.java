package com.flex.wooriorder.WebviewPack.CustomWebviewClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.flex.wooriorder.WebviewPack.EtcUtils.IsAppInstalled;

import java.net.URISyntaxException;

//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * Created by Moon on 2018. 10. 3..
 */
public class CustomWebviewClient extends WebViewClient {

    private Activity mActivity;
    public CustomWebviewClient(Activity activity) {
        this.mActivity = activity;
    }
    private AlertDialog alertIsp;
    private void alertShow(final WebView paymentView) {
        alertIsp = new AlertDialog.Builder(mActivity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("알림")
                .setMessage("모바일 ISP 어플리케이션이 설치되어 있지 않습니다. \n설치를 눌러 진행 해 주십시요.\n취소를 누르면 결제가 취소 됩니다.")
                .setPositiveButton("설치", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //ISP 설치 페이지 URL
                        paymentView.loadUrl("http://mobile.vpay.co.kr/jsp/MISP/andown.jsp");
                        mActivity.finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mActivity, "(-1)결제를 취소 하셨습니다." ,
                                Toast.LENGTH_SHORT).show();
                        mActivity.finish();
                    }
                }).create();
        alertIsp.show();

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        Activity activity = mActivity;
        String url = request.getUrl().toString();
        System.out.println("111shouldOverrideUrlLoading======================================================================" );

        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:")) {
            System.out.println("222 shouldOverrideUrlLoading======================================================================" );
            Intent intent = new Intent();
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Log.e("MyWebViewClient", intent.getDataString());
                //intent.dataString?.let { Log.e("intent getDataString()", it) };

                Intent intent2 = new Intent(Intent.ACTION_VIEW, intent.getData());
                activity.startActivity(intent2);

            } catch (ActivityNotFoundException notfound) {
                Log.e("MyWebViewClient", notfound.toString());
                //설치된 앱을 찾지 못했을 경우 고객사에서 필요한 경우 앱 스토어로 이동될 수 있도록 처리
                // 아래 스토어 이동 샘플 참고

                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    String pkgName = intent.getPackage();
                    Log.e("MyWebViewClient", "----------------------------------------------------------ccc---");
                    Log.e("MyWebViewClient", pkgName);
                    if (pkgName != null && pkgName != "") {
                        try {
                            Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkgName));
                            activity.startActivity(intent2);
                        } catch (Exception e) {
                            Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkgName));
                            activity.startActivity(intent2);
                        }
                    }
                } catch (Exception e1) {
                    // 처리할 수 없는 scheme
                    Log.e("MyWebViewClient", e1.toString());
                }
            } catch(URISyntaxException syntaxError) {
                // 실행할 수 없는 scheme 이미르 고객사 앱에서 적절한 처리 필요
            }
            return true;
        }

        if(url.startsWith("https://pf.kakao.com")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mActivity.startActivity(intent);
            return true;
        }

        if (url.startsWith("http://map.kakao.com") ) {
            return true;
        }

        System.out.println("======================================================================" );
        System.out.println("should override loading  url: "+url);
        view.loadUrl(url);

        return true;


    }

/*
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e("shoulde override loading","==========url============================================================");
        Log.e("tag","shoulde override loading");
        Log.e("shoulde override loading","==========url============================================================");

        Log.d("<INICIS_TEST>","URL : "+url);

        if( !url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript:") ) {
            Intent intent;

            if (!URLUtil.isNetworkUrl(url) && !URLUtil.isJavaScriptUrl(url)) {
                final Uri uri;

                try {
                    uri = Uri.parse(url);
                } catch (Exception e) {
                    return false;
                }

                if ("intent".equals(uri.getScheme())) {
                    return startSchemeIntent(url);
                } else {
                    try {
                        mActivity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
            }

            try{
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); Log.d("<INICIS_TEST>", "intent getDataString : " + intent.getDataString());
            } catch (URISyntaxException ex) {
                Log.e("<INICIS_TEST>", "URI syntax error : " + url + ":" + ex.getMessage()); return false;
            } try{
                mActivity.startActivity(intent); }catch(ActivityNotFoundException e){

                if( url.startsWith("ispmobile://"))
                {
//onCreateDialog에서 정의한 ISP 어플리케이션 알럿을 띄워줍니다. //(ISP 어플리케이션이 없을경우)
                    //showDialog(DIALOG_ISP);
                    return false;
                }else if (url.startsWith("intent")) {
//일부카드사의 경우 ,intent:// 형식의 intent 스키마를 내려주지 않음 //ex) 현대카드 intent:hdcardappcardansimclick://
                    try {
                        Intent tempIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); String strParams = tempIntent.getDataString();
                        Intent intent2 = new Intent(Intent.ACTION_VIEW);
                        intent2.setData(Uri.parse(strParams));
                        mActivity.startActivity(intent2);
                        return true;
                    } catch (Exception e2) {
                        e.printStackTrace();
                        Intent intent3 = null;
                        try {
                            intent3 = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW); marketIntent.setData(Uri.parse("market://details?id=" + intent3.getPackage()));
                            mActivity.startActivity(marketIntent);
                        } catch (Exception e1) { e1.printStackTrace();
                        }
                        return true; }
                }
            }
        } else {
            view.loadUrl(url);
            return false;
        }
        return true;

    }
*/

    private boolean startSchemeIntent(String url) {
        final Intent schemeIntent;

        try {
            schemeIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            return false;
        }

        try {
            mActivity.startActivity(schemeIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            final String packageName = schemeIntent.getPackage();

            if (!TextUtils.isEmpty(packageName)) {
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                return true;
            }
        }

        return false;
    }




    private boolean handleNotFoundPaymentScheme(String scheme) {
        return IsAppInstalled.getInstance().isInstalled(scheme, mActivity.getPackageManager());
    }

}
