package com.beemindz.photogalley.util;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

/**
 * Created by vanch_000 on 8/13/2014.
 */
public class Utils {
  private Context context;

  // constructor
  public Utils(Context context) {
    this.context = context;
  }

  /*
     * getting screen width
     */
  @SuppressLint("NewApi")
  public static int getScreenWidth(Context context) {
    int columnWidth;
    WindowManager wm = (WindowManager) context
        .getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();

    final Point point = new Point();
    try {
      display.getSize(point);
    } catch (java.lang.NoSuchMethodError ignore) {
      // Older device
      point.x = display.getWidth();
      point.y = display.getHeight();
    }
    columnWidth = point.x;
    return columnWidth;
  }

  // BEGIN:SHARE FUNCTION.
  public String file_download(String uRl, String pathSave) {
    File direct = new File(Environment.getExternalStorageDirectory()
        + "/" + pathSave);

    if (!direct.exists()) {
      direct.mkdirs();
    }

    String nameImg = getFileNameFromUrl(uRl);
    DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    File imageExists = new File(String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), pathSave, nameImg));
    if (!imageExists.exists()) {
      Uri downloadUri = Uri.parse(uRl);
      DownloadManager.Request request = new DownloadManager.Request(
          downloadUri);
      request.setAllowedNetworkTypes(
          DownloadManager.Request.NETWORK_WIFI
              | DownloadManager.Request.NETWORK_MOBILE)
          .setAllowedOverRoaming(false).setTitle(nameImg)
          .setDescription("Download image.")
          .setDestinationInExternalPublicDir("/" + pathSave, nameImg);

      mgr.enqueue(request);
    }

    String path = String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), pathSave, nameImg);

    Log.d("ImageAdapter", path);
    return path;
  }

  public static String getMimeType(String filePath) {
    String type = null;
    String extension = getFileExtensionFromUrl(filePath);
    if (extension != null) {
      MimeTypeMap mime = MimeTypeMap.getSingleton();
      type = mime.getMimeTypeFromExtension(extension);
    }
    return type;
  }

  public static String getFileExtensionFromUrl(String url) {
    int dotPos = url.lastIndexOf('.');
    if (0 <= dotPos) {
      return (url.substring(dotPos + 1)).toLowerCase();
    }
    return "";
  }

  public static String getFileNameFromUrl(String url) {
    int dotPos = url.lastIndexOf('/');
    if (0 <= dotPos) {
      return (url.substring(dotPos + 1)).toLowerCase();
    }
    return "";
  }
  // END

  // BEGIN: TOAST UTILS
  /**
   * Displays a Toast notification for a short duration.
   *
   * @param context
   *            activity screen.
   * @param resId
   *            '@string' id.
   */
  public static void toast(Context context, int resId) {
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
  }

  /**
   * Displays a Toast notification for a short duration.
   *
   * @param context
   *            activity screen.
   * @param message
   *            message need show.
   */
  public static void toast(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }
  // END.
}
