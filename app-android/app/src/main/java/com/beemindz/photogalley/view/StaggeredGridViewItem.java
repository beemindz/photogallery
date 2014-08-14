package com.beemindz.photogalley.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vanch_000 on 8/14/2014.
 */
public abstract  class StaggeredGridViewItem {
  public abstract View getView(LayoutInflater inflater, ViewGroup parent);
  public abstract int getViewHeight(LayoutInflater inflater, ViewGroup parent);
}
