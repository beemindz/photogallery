package com.beemindz.photogalley.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;

import com.beemindz.photogalley.R;
import com.beemindz.photogalley.util.Constants;
import com.beemindz.photogalley.util.TouchImageView;
import com.beemindz.photogalley.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageDetailActivity extends ActionBarActivity {

  private String TAG = getClass().getName();
  private static final String STATE_POSITION = "STATE_POSITION";
  DisplayImageOptions options;
  ImageLoader imageLoader;
  TouchImageView imageView;
  String uri;

  SlidingDrawer slidingDrawer;
  Button slideButton, saveBtn, cropperBtn;

  boolean isShowBar = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ac_image_detail);
    getSupportActionBar().hide();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(true);

    imageView = (TouchImageView) findViewById(R.id.ac_image_detail_image_view);

    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    Bundle bundle = getIntent().getExtras();
    assert bundle != null;
    final String imageUrl = bundle.getString(Constants.Extra.IMAGES);
    uri = imageUrl;
    int pagerPosition = bundle.getInt(Constants.Extra.IMAGE_POSITION, 0);

    imageLoader = ImageLoader.getInstance();
    options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.ic_stub)
        .showImageForEmptyUri(R.drawable.ic_empty)
        .showImageOnFail(R.drawable.ic_error)
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .considerExifParams(true)
        .build();

    if (savedInstanceState != null) {
      pagerPosition = savedInstanceState.getInt(STATE_POSITION);
    }

    imageLoader.displayImage(imageUrl, imageView, options);
    imageView.setMaxZoom(4f);
    onImageClick(imageView);

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

    saveBtn = (Button) findViewById(R.id.ac_image_detail_save);
    cropperBtn = (Button) findViewById(R.id.ac_image_detail_cropper);
    cropperBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {

        } catch (Exception e) {
          Log.d(TAG, "cropperBtn click " + e);
        }
      }
    });
    // END.
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

  // BEGIN: show/hide action when click imageview.
  public void onImageClick(ImageView image) {
    Log.d(TAG, "onImageClick");
    image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (isShowBar)
          getSupportActionBar().hide();
        else
          getSupportActionBar().show();

        isShowBar = !isShowBar;
      }
    });
  }
  // END.

  // BEGIN: Cropper image


  // END
}
