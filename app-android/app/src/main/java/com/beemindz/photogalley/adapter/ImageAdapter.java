package com.beemindz.photogalley.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beemindz.photogalley.R;
import com.beemindz.photogalley.activity.ImageDetailActivity;
import com.beemindz.photogalley.activity.fragment.dummy.DummyContent;
import com.beemindz.photogalley.util.Constants;
import com.beemindz.photogalley.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vanch_000 on 8/13/2014.
 */
public class ImageAdapter extends ArrayAdapter<DummyContent.DummyItem> {

  List<DummyContent.DummyItem> items;
  int resource;
  Context context;
  LayoutInflater inflater;
  ImageLoader imageLoader;
  DisplayImageOptions options;
  private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
  //initialize our progress dialog/bar
  private ProgressDialog mProgressDialog;
  public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

  static class ViewHolder {
    ImageView image;
    ImageButton imgBtnShare;
    ImageButton imgBtnComment;
  }

  public ImageAdapter(Context context, int resource, List<DummyContent.DummyItem> items, ImageLoader imageLoader) {
    super(context, resource, items);
    this.context = context;
    this.resource = resource;
    this.items = items;
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    this.imageLoader = imageLoader;
    options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.ic_stub)
        .showImageForEmptyUri(R.drawable.ic_empty)
        .showImageOnFail(R.drawable.ic_error)
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .considerExifParams(true)
        .displayer(new RoundedBitmapDisplayer(0))
        .build();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    try {
      DummyContent.DummyItem item = items.get(position);
      if (item != null) {
        ViewHolder holder = new ViewHolder();
        //if (convertView == null) {
          // get layout item.
          convertView = inflater.inflate(resource, null);

          // findByViewId for property ViewHolder
          holder.image = (ImageView) convertView.findViewById(R.id.item_image_view);
          holder.imgBtnShare = (ImageButton) convertView.findViewById(R.id.image_item_img_btn_share);

          convertView.setTag(holder);
        //} else {
        //  convertView.getTag();
        //}

        if (holder.image != null) {
          onImageShareListener(holder.imgBtnShare, item.url);
          // set async task ImageView by bitmap.
          imageLoader.displayImage(item.getUrl(), holder.image, options, animateFirstListener);

          onImageListener(holder.image, position, item.url);
        }
      }
      return convertView;
    } catch (Exception e) {
      return null;
    }
  }

  private void startImagePagerActivity(int position, String url) {
    Intent intent = new Intent(context, ImageDetailActivity.class);
    intent.putExtra(Constants.Extra.IMAGES, url);
    intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
    context.startActivity(intent);
  }

  private void onImageListener(ImageView imageView, final int position, final String url) {
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startImagePagerActivity(position, url);
      }
    });
  }

  private void onImageShareListener(ImageView imageView, final String url) {
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String downloadImg = new Utils(context).file_download(url);
        if (downloadImg != null) {
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

  public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public void onLoadingStarted(String imageUri, View view) {
      super.onLoadingStarted(imageUri, view);
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
      if (loadedImage != null) {
        ImageView imageView = (ImageView) view;
        boolean firstDisplay = !displayedImages.contains(imageUri);
        if (firstDisplay) {
          FadeInBitmapDisplayer.animate(imageView, 5000);
          displayedImages.add(imageUri);
        }
      }
    }
  }
}
