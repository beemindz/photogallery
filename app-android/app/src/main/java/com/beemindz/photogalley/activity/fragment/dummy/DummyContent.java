package com.beemindz.photogalley.activity.fragment.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

  /**
   * An array of sample (dummy) items.
   */
  public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

  /**
   * A map of sample (dummy) items, by ID.
   */
  public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

  static {
    // Add 3 sample items.
    addItem(new DummyItem("1", "http://chandai.tv/data/photos/06165427db354c10b57fc12316d63bd3/thumbnail-cad890c4d4b142cb8f16581073d916ee.jpg", "Item 1"));
    addItem(new DummyItem("2", "http://chandai.tv/data/photos/2ebf2e80fb834a39aaa1173682014c40/thumbnail-b8e16dd195c74d1ba92351d367215310.jpg", "Item 2"));
    addItem(new DummyItem("3", "http://chandai.tv/data/photos/d9af0888656f493b8f273b7349628820/thumbnail-aaa6919d991e466eaf340e0842d98383.jpg", "Item 3"));
    addItem(new DummyItem("4", "http://chandai.tv/data/photos/f04591330782441b86300cae3a6b935f/thumbnail-0f4ab405c740494fbcf54896f905ce72.jpg", "Item 4"));
    addItem(new DummyItem("5", "http://chandai.tv/data/photos/f4101fe6f9094d80893260cd4f089f14/thumbnail-4a3689f7a83140a49bb53f073908dff4.jpg", "Item 5"));
    addItem(new DummyItem("6", "http://chandai.tv/data/photos/de79f9a4b5da407782da4513acb91f08/thumbnail-e62acf906acf4a71909cb1f1ed8b3023.jpg", "Item 6"));
    addItem(new DummyItem("7", "http://chandai.tv/data/photos/aa1af5fb38794ee19de04465feb87d89/thumbnail-1a4d1ba259354015a6d3dc2d18f3fa6e.jpg", "Item 7"));
    addItem(new DummyItem("8", "http://chandai.tv/data/photos/7dabbd4124f54ad2a1905581cf70b4b5/thumbnail-04cd4492462e477aa98843c2868d894a.jpg", "Item 8"));
    addItem(new DummyItem("9", "http://chandai.tv/data/photos/37517aaa689741fca0210f5764303807/thumbnail-a41b4ba3cdea4fdbac99c1d853e0c104.jpg", "Item 9"));
    addItem(new DummyItem("10", "http://chandai.tv/data/photos/17b34ec62a004b3994b41828911ff391/thumbnail-c4700e41974b40de8f728ef34cdd7bfe.jpg", "Item 10"));
    addItem(new DummyItem("11", "http://chandai.tv/data/photos/0338844ee1514b60a4894c4a8ab15605/thumbnail-c03509128914462185964e5fbff24a9e.jpg", "Item 11"));
    addItem(new DummyItem("12", "http://chandai.tv/data/photos/0b28eb0e395747df8ebc7b85b0f98b30/thumbnail-13713ddd41054591a26b3fce2eb5027d.jpg", "Item 12"));
    /*for (int i = 13; i<= 100; i++) {
      addItem(new DummyItem(""+i, "http://chandai.tv/data/photos/2628161f349f444bb8b6b0b66e80bee4/thumbnail-057b0326f982403980e50f117595ee5a.jpg", "Item " + i));
    }*/
  }

  private static void addItem(DummyItem item) {
    ITEMS.add(item);
    ITEM_MAP.put(item.id, item);
  }

  /**
   * A dummy item representing a piece of content.
   */
  public static class DummyItem {
    public String id;
    public String content;
    public String url;

    public DummyItem(String id, String url, String content) {
      this.id = id;
      this.content = content;
      this.url = url;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return content;
    }
  }
}
