package com.beemindz.photogalley.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beemindz.photogalley.R;
import com.beemindz.photogalley.activity.FbCommentActivity;
import com.beemindz.photogalley.activity.ImageDetailActivity;
import com.beemindz.photogalley.activity.fragment.dummy.DummyContent;
import com.beemindz.photogalley.util.Constants;
import com.beemindz.photogalley.util.ShareUtils;
import com.beemindz.photogalley.util.Utils;
import com.beemindz.photogalley.view.DynamicHeightImageView;
import com.beemindz.photogalley.view.ExtendableListView;
import com.beemindz.photogalley.view.StaggeredGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vanch_000 on 8/13/2014.
 */
public class ImageAdapter extends ArrayAdapter<DummyContent.DummyItem> {

  private List<DummyContent.DummyItem> items;
  private int resource;
  private Context context;
  private LayoutInflater inflater;
  private ImageLoader imageLoader;
  private DisplayImageOptions options;
  private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
  //initialize our progress dialog/bar
  private ProgressDialog mProgressDialog;
  public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

  static class ViewHolder {
    DynamicHeightImageView image;
    ImageButton imgBtnShare;
    ImageButton btnComment;
    Button btnVoteUp;
    Button btnVoteDown;
    ProgressBar progressBar;
  }

  public ImageAdapter(Context context, int resource, List<DummyContent.DummyItem> items, ImageLoader imageLoader) {
    super(context, resource, items);
    this.context = context;
    this.resource = resource;
    this.items = items;
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    this.imageLoader = imageLoader;
    options = new DisplayImageOptions.Builder()
        //.showImageOnLoading(R.drawable.ic_stub)
        //.showImageForEmptyUri(R.drawable.ic_empty)
        //.showImageOnFail(R.drawable.ic_error)
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    try {
      final ViewHolder holder;
      DummyContent.DummyItem item = items.get(position);
      if (item != null) {

        if (convertView == null) {
          // get layout item.
          convertView = inflater.inflate(resource, null);
          holder = new ViewHolder();
          assert convertView != null;
          // findByViewId for property ViewHolder
          holder.image = (DynamicHeightImageView) convertView.findViewById(R.id.item_image_view);
          holder.imgBtnShare = (ImageButton) convertView.findViewById(R.id.image_item_img_btn_share);
          holder.progressBar = (ProgressBar) convertView.findViewById(R.id.image_item_progressBar);
          holder.btnComment = (ImageButton) convertView.findViewById(R.id.image_item_img_btn_comment);
          holder.btnVoteDown = (Button) convertView.findViewById(R.id.image_item_img_btn_vote_down);
          holder.btnVoteUp = (Button) convertView.findViewById(R.id.image_item_img_btn_vote_up);

          convertView.setTag(holder);
        } else {
          holder = (ViewHolder)convertView.getTag();
        }

        if (holder.image != null) {
          new ShareUtils(context).onImageShareListener(holder.imgBtnShare, item.url);
          // set async task ImageView by bitmap.
          animateFirstListener = new AnimateFirstDisplayListener(holder.progressBar);
          imageLoader.displayImage(item.getUrl(), holder.image, options, animateFirstListener, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current,
                                         int total) {
              holder.progressBar.setProgress(Math.round(100.0f * current / total));
            }
          });

          holder.image.setAdjustViewBounds(false);
          holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);

          onImageListener(holder.image, position, item.url);
          onBtnCommentClickListener(holder.btnComment, item.url);
          new ShareUtils(context).onImageShareListener(holder.imgBtnShare, item.url);
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

  private void onBtnCommentClickListener(ImageButton button, final String imgUrl){
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, FbCommentActivity.class);
        intent.putExtra(Constants.IMAGE_URL, imgUrl);
        context.startActivity(intent);
      }
    });

  }

  public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

    ProgressBar progressBar;
    public AnimateFirstDisplayListener(){}
    public AnimateFirstDisplayListener(ProgressBar progressBar) {
      this.progressBar = progressBar;
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {
      progressBar.setProgress(0);
      progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFailed(String imageUri, View view,
                                FailReason failReason) {
      progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
      progressBar.setVisibility(View.GONE);
      if (loadedImage != null) {
        ImageView imageView = (ImageView) view;
        boolean firstDisplay = !displayedImages.contains(imageUri);
        if (firstDisplay) {
          FadeInBitmapDisplayer.animate(imageView, 5000);
          displayedImages.add(imageUri);
        }
      }
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
      progressBar.setVisibility(View.GONE);
    }
  }
}
