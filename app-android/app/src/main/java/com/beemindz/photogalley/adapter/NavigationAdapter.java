package com.beemindz.photogalley.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.beemindz.photogalley.R;
import com.beemindz.photogalley.activity.fragment.dummy.NavigationContent;

import java.util.List;

/**
 * Created by vanch_000 on 8/15/2014.
 */
public class NavigationAdapter extends BaseExpandableListAdapter {

  private List<NavigationContent.NavigationCategory> catList;
  private int itemLayoutId;
  private int groupLayoutId;
  private Context ctx;

  public NavigationAdapter(List<NavigationContent.NavigationCategory> catList, Context ctx) {

    this.itemLayoutId = R.layout.navigation_item;
    this.groupLayoutId = R.layout.navigation_group;
    this.catList = catList;
    this.ctx = ctx;
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return catList.get(groupPosition).getItemList().get(childPosition);
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return catList.get(groupPosition).getItemList().get(childPosition).hashCode();
  }

  @Override
  public View getChildView(int groupPosition, int childPosition,
                           boolean isLastChild, View convertView, ViewGroup parent) {
    View v = convertView;

    if (v == null) {
      LayoutInflater inflater = (LayoutInflater) ctx.getSystemService
          (Context.LAYOUT_INFLATER_SERVICE);
      v = inflater.inflate(R.layout.navigation_item, parent, false);
    }

    ImageView icon = (ImageView) v.findViewById(R.id.nav_item_icon);
    TextView itemName = (TextView) v.findViewById(R.id.itemName);

    NavigationContent.NavigationItem det = catList.get(groupPosition).getItemList().get(childPosition);
    if (det != null) {
      itemName.setText(det.title);
      if (det.icon != 0) icon.setImageDrawable(ctx.getResources().getDrawable(det.icon));
    }
    return v;

  }

  @Override
  public int getChildrenCount(int groupPosition) {
    int size = catList.get(groupPosition).getItemList().size();
    System.out.println("Child for group [" + groupPosition + "] is [" + size + "]");
    return size;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return catList.get(groupPosition);
  }

  @Override
  public int getGroupCount() {
    return catList.size();
  }

  @Override
  public long getGroupId(int groupPosition) {
    return catList.get(groupPosition).hashCode();
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded,
                           View convertView, ViewGroup parent) {
    ExpandableListView eLV = (ExpandableListView) parent;
    eLV.expandGroup(groupPosition);

    View v = convertView;

    if (v == null) {
      LayoutInflater inflater = (LayoutInflater) ctx.getSystemService
          (Context.LAYOUT_INFLATER_SERVICE);
      v = inflater.inflate(R.layout.navigation_group, parent, false);
    }

    ImageView icon = (ImageView) v.findViewById(R.id.nav_group_icon);
    TextView groupName = (TextView) v.findViewById(R.id.groupName);

    NavigationContent.NavigationCategory cat = catList.get(groupPosition);
    if (cat != null) {
      groupName.setText(cat.title);
      if (cat.icon != 0) icon.setImageDrawable(ctx.getResources().getDrawable(cat.icon));
    }
    return v;

  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

}
