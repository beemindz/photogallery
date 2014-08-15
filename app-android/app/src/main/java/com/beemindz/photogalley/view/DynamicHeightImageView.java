package com.beemindz.photogalley.view;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by vanch_000 on 8/14/2014.
 */
public class DynamicHeightImageView extends ImageView {
  private double mHeightRatio;
  private static float radius = 18.0f;

  public DynamicHeightImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DynamicHeightImageView(Context context) {
    super(context);
  }

  public void setHeightRatio(double ratio) {
    if (ratio != mHeightRatio) {
      mHeightRatio = ratio;
      requestLayout();
    }
  }

  public double getHeightRatio() {
    return mHeightRatio;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (mHeightRatio > 0.0) {
      // set the image views size
      int width = MeasureSpec.getSize(widthMeasureSpec);
      int height = (int) (width * mHeightRatio);
      setMeasuredDimension(width, height);
    }
    else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }
}
