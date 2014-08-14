package com.beemindz.photogalley.util;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by vanch_000 on 8/13/2014.
 */
public class SquareImageView extends ImageView {
  public SquareImageView(Context context) {
    super(context);
    setScaleType(ScaleType.MATRIX);
  }

  public SquareImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
  }

  @Override
  protected boolean setFrame(int l, int t, int r, int b) {
    recomputeImgMatrix();

    return super.setFrame(l, t, r, b);
  }

  private void recomputeImgMatrix() {
    final Matrix matrix = getImageMatrix();

    float scale;
    final int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
    final int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();
    final int drawableWidth = getDrawable().getIntrinsicWidth();
    final int drawableHeight = getDrawable().getIntrinsicHeight();

    if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
      scale = (float) viewHeight / (float) drawableHeight;
    } else {
      scale = (float) viewWidth / (float) drawableWidth;
    }

    matrix.setScale(scale, scale);
    setImageMatrix(matrix);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    recomputeImgMatrix();
  }
}
