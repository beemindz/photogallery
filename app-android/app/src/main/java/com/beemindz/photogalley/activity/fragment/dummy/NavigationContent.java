package com.beemindz.photogalley.activity.fragment.dummy;

import android.graphics.drawable.Drawable;

import com.beemindz.photogalley.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vanch_000 on 8/15/2014.
 */
public class NavigationContent {

  /**
   * An array of sample (dummy) items.
   */
  public static List<NavigationItem> ITEMS = new ArrayList<NavigationItem>();
  public static List<NavigationCategory> CATEGORY_ITEMS = new ArrayList<NavigationCategory>();

  /**
   * A map of sample (dummy) items, by ID.
   */
  public static Map<Integer, NavigationItem> ITEM_MAP = new HashMap<Integer, NavigationItem>();

  static {
    addItem(new NavigationItem(1, "Bikini", 0));
    addItem(new NavigationItem(2, "Nude", 0));
    addItem(new NavigationItem(3, "Hot girls", 0));

    addCategory(new NavigationCategory(1, "Category", R.drawable.icon_categories, ITEMS));
    addCategory(new NavigationCategory(2, "About", R.drawable.icon_about, null));
  }

  private static void addItem(NavigationItem item) {
    ITEMS.add(item);
    ITEM_MAP.put(item.id, item);
  }

  private static void addCategory(NavigationCategory category) {
    CATEGORY_ITEMS.add(category);
  }

  /**
   * Navigation Category.
   */
  public static class NavigationCategory {
    public int id;
    public String title;
    public int icon;
    private List<NavigationItem> itemList = new ArrayList<NavigationItem>();

    public NavigationCategory(int id, String title, int icon, List<NavigationItem> itemList) {
      this.id = id;
      this.title = title;
      this.icon = icon;
      if (itemList != null && itemList.size() > 0) {
        this.itemList.addAll(itemList);
      }
    }

    public List<NavigationItem> getItemList() {
      return itemList;
    }

    public void setItemList(List<NavigationItem> itemList) {
      this.itemList = itemList;
    }
  }

  /**
   * A item representing a piece of category.
   */
  public static class NavigationItem {
    public int id;
    public String title;
    public int icon;

    public NavigationItem(int id, String title, int icon) {
      this.id = id;
      this.title = title;
      this.icon = icon;
    }

    @Override
    public String toString() {
      return title;
    }
  }
}
