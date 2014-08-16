package com.beemindz.photogalley.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.beemindz.photogalley.R;
import com.beemindz.photogalley.util.Constants;


public class FbCommentActivity extends ActionBarActivity {

  private String requestUrl;
  private WebView fbCommentWebView,childView =null;
  private LinearLayout parentLayout;
  private Activity MyActivity;
  private static Context mContext;

  @Override
  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
    setContentView(R.layout.ac_fb_comment);
    getWindow().setFeatureInt(Window.FEATURE_PROGRESS,Window.PROGRESS_VISIBILITY_ON);

    Intent intent = getIntent();
    requestUrl = intent.getStringExtra(Constants.IMAGE_URL);

    parentLayout =(LinearLayout)findViewById(R.id.linearLayout1);
    MyActivity = this;

    fbCommentWebView = (WebView) findViewById(R.id.fb_comment_webView) ;
//    fbCommentWebView.setLayoutParams(getLayoutParams());

    fbCommentWebView.setWebViewClient(new FaceBookClient());
    fbCommentWebView.setWebChromeClient(new MyChromeClient());
    fbCommentWebView.getSettings().setJavaScriptEnabled(true);
    fbCommentWebView.getSettings().setAppCacheEnabled(true);
    fbCommentWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    fbCommentWebView.getSettings().setSupportMultipleWindows(true);
    fbCommentWebView.getSettings().setSupportZoom(true);
    fbCommentWebView.getSettings().setBuiltInZoomControls(true);

//    parentLayout.addView(fbCommentWebView);
    fbCommentWebView.loadUrl(Constants.URL_FB_COMMENT + requestUrl);

  }

  private LinearLayout.LayoutParams getLayoutParams(){
    return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
  }


  final class MyChromeClient extends WebChromeClient{
    @Override
    public boolean onCreateWindow(WebView view, boolean dialog,
                                  boolean userGesture, Message resultMsg) {
      childView = new WebView(FbCommentActivity.this);
      childView.getSettings().setJavaScriptEnabled(true);
      childView.getSettings().setSupportZoom(true);
      childView.getSettings().setBuiltInZoomControls(true);
      childView.setWebViewClient(new FaceBookClient());
      childView.setWebChromeClient(this);
      childView.setLayoutParams(getLayoutParams());
      parentLayout.addView(childView);

      childView.requestFocus();
      fbCommentWebView.setVisibility(View.GONE);

          /*I think this is the main part which handles all the log in session*/
      WebView.WebViewTransport transport =(WebView.WebViewTransport)resultMsg.obj;
      transport.setWebView(childView);
      resultMsg.sendToTarget();
      return true;
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
      MyActivity.setProgress(newProgress*100);
    }

    @Override
    public void onCloseWindow(WebView window) {
      parentLayout.removeViewAt(parentLayout.getChildCount()-1);
      childView =null;
      fbCommentWebView.setVisibility(View.VISIBLE);
      fbCommentWebView.requestFocus();
    }
  }

  private class FaceBookClient extends WebViewClient{
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      Log.i("REQUEST URL",url);
      return false;
    }
  }

  @Override
  public void onBackPressed() {
    if(childView != null && parentLayout.getChildCount()==2){
      childView.stopLoading();
      parentLayout.removeViewAt(parentLayout.getChildCount()-1);
      if(fbCommentWebView.getVisibility() == View.GONE)
        fbCommentWebView.setVisibility(View.VISIBLE);
    }else{
      super.onBackPressed();
    }
  }
}
