package com.beemindz.photogalley.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;

import com.beemindz.photogalley.R;
import com.beemindz.photogalley.util.Constants;
import com.beemindz.photogalley.util.ShareUtils;
import com.beemindz.photogalley.util.TouchImageView;
import com.beemindz.photogalley.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageDetailActivity extends ActionBarActivity implements View.OnClickListener {

  private String TAG = getClass().getName();
  private static final String STATE_POSITION = "STATE_POSITION";
  DisplayImageOptions options;
  ImageLoader imageLoader;
  TouchImageView imageView;
  WebView fbLikeWebView, childView;
  RelativeLayout fbLikeLayout;
  String uri;

  SlidingDrawer slidingDrawer;
  Button slideButton, btnSave, btnCropper;
  ImageButton btnComment, btnShare;

  boolean isShowBar = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ac_image_detail);
    getSupportActionBar().hide();

    imageView = (TouchImageView) findViewById(R.id.ac_image_detail_image_view);
    fbLikeLayout = (RelativeLayout) findViewById(R.id.fb_like_layout);
    fbLikeWebView = (WebView) findViewById(R.id.fb_like_webview);
    btnShare = (ImageButton) findViewById(R.id.ac_image_detail_btn_share);

    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    Bundle bundle = getIntent().getExtras();
    assert bundle != null;
    final String imageUrl = bundle.getString(Constants.Extra.IMAGES);
    uri = imageUrl;

    int pagerPosition = bundle.getInt(Constants.Extra.IMAGE_POSITION, 0);

    imageLoader = ImageLoader.getInstance();
    options = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();

    if (savedInstanceState != null) {
      pagerPosition = savedInstanceState.getInt(STATE_POSITION);
    }

    imageLoader.displayImage(imageUrl, imageView, options);
    imageView.setMaxZoom(4f);

    // BEGIN: SlidingDrawer.
    slidingDrawer = (SlidingDrawer) findViewById(R.id.ac_image_detail_sliding_drawer);
    slideButton = (Button) findViewById(R.id.ac_image_detail_slide_button);

    slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
      @Override
      public void onDrawerOpened() {
        slideButton.setBackgroundResource(R.drawable.bullet_arrow_down);
      }
    });

    slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
      @Override
      public void onDrawerClosed() {
        slideButton.setBackgroundResource(R.drawable.bullet_arrow_up);
      }
    });

    btnSave = (Button) findViewById(R.id.ac_image_detail_save);
    btnCropper = (Button) findViewById(R.id.ac_image_detail_cropper);
    btnCropper.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {

        } catch (Exception e) {
          Log.d(TAG, "btnCropper click " + e);
        }
      }
    });
    // END.

    btnComment = (ImageButton) findViewById(R.id.ac_image_detail_btn_comment);
    btnComment.setOnClickListener(this);
    //Call fb Webview
    setUpFbLikeWebView(imageUrl);
    // share
    new ShareUtils(this).onImageShareListener(imageView, uri);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.image_detail, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {
      case R.id.detail_action_save:
        if (!TextUtils.isEmpty(uri)) {
          String save = new Utils(this).file_download(uri, Constants.PATH_SAVE_IMAGE_DETAIL);
          if (!TextUtils.isEmpty(save)) {
            String msg = String.format("%s %s", getResources().getString(R.string.toast_msg_save_image_success), Constants.PATH_SAVE_IMAGE_DETAIL);
            Utils.toast(this, msg);
          } else {
            Utils.toast(this, R.string.toast_msg_save_image_failed);
          }
        }
        break;
      case android.R.id.home:
        finish();
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  private void setUpFbLikeWebView(String imgUrl) {

    fbLikeWebView.setWebViewClient(new FaceBookClient());
    fbLikeWebView.setWebChromeClient(new MyChromeClient());
    final WebSettings webSettings = fbLikeWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    webSettings.setSupportMultipleWindows(true);
    fbLikeWebView.setBackgroundColor(0x00000000);

    fbLikeWebView.loadUrl(Constants.URL_FB_LIKE+imgUrl);
//    holder.fbLikeWebView.loadUrl("http://192.168.1.77/fbLike.html?imgUrl="+imgUrl);
//    fbLikeWebview.setLayoutParams(FILL);
//    holder.fbLikeLayout.addView(holder.fbLikeWebView);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ac_image_detail_btn_comment:
        Intent intent = new Intent(ImageDetailActivity.this, FbCommentActivity.class);
        intent.putExtra(Constants.IMAGE_URL, uri);
        startActivity(intent);

        break;
    }
  }

  final class MyChromeClient extends WebChromeClient {

    // Add new webview in same window
    @Override
    public boolean onCreateWindow(WebView view, boolean dialog,
                                  boolean userGesture, Message resultMsg) {
      childView = new WebView(ImageDetailActivity.this);

      childView.getSettings().setJavaScriptEnabled(true);
      childView.setWebChromeClient(this);
      childView.setWebViewClient(new FaceBookClient());
//      childView.setLayoutParams(FILL);
      fbLikeLayout.addView(childView);
      childView.requestFocus();
      fbLikeWebView.setVisibility(View.GONE);

      WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
      transport.setWebView(childView);
      resultMsg.sendToTarget();
      return true;
    }

    // remove new added webview whenever onCloseWindow gets called for new webview.
    @Override
    public void onCloseWindow(WebView window) {
      fbLikeLayout.removeViewAt(fbLikeLayout.getChildCount() - 1);
      childView =null;
      fbLikeWebView.setVisibility(View.VISIBLE);
      fbLikeWebView.requestFocus();
    }
  }

  private class FaceBookClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      Log.i("REQUEST URL", url);
      return false;
    }
  }

  @Override
  public void onBackPressed() {
    if(childView != null && fbLikeLayout.getChildCount()==2){
      childView.stopLoading();
      fbLikeLayout.removeViewAt(fbLikeLayout.getChildCount()-1);
      if(fbLikeWebView.getVisibility() == View.GONE)
        fbLikeWebView.setVisibility(View.VISIBLE);
    }else{
      super.onBackPressed();
    }
  }
}
