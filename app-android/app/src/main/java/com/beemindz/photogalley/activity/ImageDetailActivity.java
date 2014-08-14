package com.beemindz.photogalley.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.beemindz.photogalley.R;
import com.beemindz.photogalley.util.Constants;
import com.beemindz.photogalley.util.TouchImageView;
import com.beemindz.photogalley.util.Utils;
import com.edmodo.cropper.CropImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageDetailActivity extends ActionBarActivity {
  private String TAG = getClass().getName();
  private static final String STATE_POSITION = "STATE_POSITION";
  DisplayImageOptions options;
  ImageLoader imageLoader;
  TouchImageView imageView;

  SlidingDrawer slidingDrawer;
  Button slideButton, saveBtn, cropperBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ac_image_detail);

    imageView = (TouchImageView) findViewById(R.id.ac_image_detail_image_view);

    overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    Bundle bundle = getIntent().getExtras();
    assert bundle != null;
    final String imageUrl = bundle.getString(Constants.Extra.IMAGES);
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
          performCrop(Uri.parse(imageUrl));
        } catch (Exception e) {
          Log.d(TAG, "cropperBtn click " + e);
        }
      }
    });
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
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  // BEGIN: Crop image
  final int CAMERA_CAPTURE = 1;
  final int CROP_PIC = 2;
  private Uri picUri;

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      if (requestCode == CAMERA_CAPTURE) {
        // get the Uri for the captured image
        picUri = data.getData();
        performCrop(picUri);
      }
      // user is returning from cropping the image
      else if (requestCode == CROP_PIC) {
        // get the returned data
        Bundle extras = data.getExtras();
        // get the cropped bitmap
        Bitmap thePic = extras.getParcelable("data");
        ImageView picView = imageView;
        picView.setImageBitmap(thePic);
      }
    }
  }

  /**
   * this function does the crop operation.
   */
  private void performCrop(Uri picUri) {
    // take care of exceptions
    try {
      // call the standard crop action intent (the user device may not
      // support it)
      Intent cropIntent = new Intent("com.android.camera.action.CROP");
      // indicate image type and Uri
      cropIntent.setDataAndType(picUri, "image/*");
      // set crop properties
      cropIntent.putExtra("crop", "true");
      // indicate aspect of desired crop
      cropIntent.putExtra("aspectX", 2);
      cropIntent.putExtra("aspectY", 1);
      // indicate output X and Y
      cropIntent.putExtra("outputX", 256);
      cropIntent.putExtra("outputY", 256);
      // <span id="IL_AD4" class="IL_AD">retrieve</span> data on return
      cropIntent.putExtra("return-data", true);
      // <span id="IL_AD5" class="IL_AD">start</span> the activity - we handle returning in onActivityResult
      startActivityForResult(cropIntent, CROP_PIC);
    }
    // respond to users whose devices do not support the crop action
    catch (ActivityNotFoundException anfe) {
      Toast toast = Toast
          .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
      toast.show();
    }
  }
  // END
}
