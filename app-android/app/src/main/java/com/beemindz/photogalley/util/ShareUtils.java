package com.beemindz.photogalley.util;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

/**
 * Created by vanch_000 on 8/16/2014.
 */
public class ShareUtils {
  private Context context;

  public ShareUtils(Context context) {
    this.context = context;
  }

  public void onImageShareListener(ImageButton imageView, final String url) {
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String downloadImg = new Utils(context).file_download(url, Constants.PATH_SAVE_IMAGE_SHARE);
        if (!TextUtils.isEmpty(downloadImg)) {
          Toast.makeText(context, "Save image success", Toast.LENGTH_SHORT);
          File myFile = new File(downloadImg);
          Intent sendIntent = new Intent();
          sendIntent.setAction(Intent.ACTION_SEND);
          sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myFile));
          sendIntent.setType(Utils.getMimeType(myFile.getPath()));
          context.startActivity(sendIntent);
        } else {
          Toast.makeText(context, "Save image failed, not share", Toast.LENGTH_SHORT);
        }
      }
    });
  }

  public void setWallpaper(TypeInput typeInput, Bitmap bitmap, int resId) throws Exception {
    WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
    if (typeInput == TypeInput.BITMAP) {
      DisplayMetrics metrics = new DisplayMetrics();
      ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
      int height = metrics.heightPixels;
      int width = metrics.widthPixels;
      Log.d(getClass().getName(), "metrics.heightPixels:" + height + "- metrics.widthPixels:" + width);
      Bitmap bmp = Bitmap.createScaledBitmap(bitmap, width, height, true);
      wallpaperManager.setWallpaperOffsetSteps(1, 1);
      wallpaperManager.suggestDesiredDimensions(width, height);
      wallpaperManager.setBitmap(bmp);
    } else if (typeInput == TypeInput.RESOURCE) {
      wallpaperManager.setResource(resId);
    } else if (typeInput == TypeInput.URI) {
      //TODO
    }
  }
}

