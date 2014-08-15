package com.beemindz.photogalley.activity.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.beemindz.photogalley.R;
import com.beemindz.photogalley.activity.ImageDetailActivity;
import com.beemindz.photogalley.activity.fragment.dummy.DummyContent;
import com.beemindz.photogalley.adapter.ImageAdapter;
import com.beemindz.photogalley.util.Constants;
import com.beemindz.photogalley.view.StaggeredGridView;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement
 * interface.
 */
public class ListImageFragmentFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

  ImageLoader imageLoader;

  /**
   * The Adapter which will be used to populate the ListView/GridView with
   * Views.
   */
  //private BaseAdapter mAdapter;
  private BaseAdapter adapter;

  /**
   * The fragment's ListView/GridView.
   */
  protected AbsListView mListView;
  StaggeredGridView gridView;

  // TODO: Rename and change types of parameters
  public static ListImageFragmentFragment newInstance(String session) {
    ListImageFragmentFragment fragment = new ListImageFragmentFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public ListImageFragmentFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
    }

    getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    imageLoader = ImageLoader.getInstance();
    //mAdapter = new ImageAdapter(getActivity(), R.layout.image_item, DummyContent.ITEMS, imageLoader);
    adapter = new ImageAdapter(getActivity(), R.layout.image_item, DummyContent.ITEMS, imageLoader);
  }

  @Override
  public void onStart() {
    super.onStart();
    getActivity().overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_listimagefragment, container, false);

    // Set the adapter
    /*mListView = (AbsListView) view.findViewById(android.R.id.list);
    ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);*/
    //mListView = (AbsListView) view.findViewById(android.R.id.list);
    gridView = (StaggeredGridView) view.findViewById(R.id.grid);
    //mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    //setBottomAdapter();
    setAlphaAdapter();

    // Set OnItemClickListener so we can be notified on item clicks
    //mListView.setOnItemClickListener(this);
    gridView.setOnItemClickListener(this);

    return view;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

  }

  /**
   * The default content for this Fragment has a TextView that is shown when
   * the list is empty. If you would like to change the text, call this method
   * to supply the text it should use.
   */
  public void setEmptyText(CharSequence emptyText) {
    View emptyView = mListView.getEmptyView();

    if (emptyText instanceof TextView) {
      ((TextView) emptyView).setText(emptyText);
    }
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    public void onFragmentInteraction(String id);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  /*---Animation adapter---*/
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private void setAlphaAdapter() {
    AnimationAdapter animAdapter = new AlphaInAnimationAdapter(adapter);
    animAdapter.setAbsListView(gridView);
    //mListView.setAdapter(animAdapter);
    gridView.setAdapter(adapter);
  }

  private boolean mHasRequestedMore;
  @Override
  public void onScrollStateChanged(final AbsListView view, final int scrollState) {
    Log.d(getClass().getName(), "onScrollStateChanged:" + scrollState);
  }

  @Override
  public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
    Log.d(getClass().getName(), "onScroll firstVisibleItem:" + firstVisibleItem +
        " visibleItemCount:" + visibleItemCount +
        " totalItemCount:" + totalItemCount);
    // our handling
    if (!mHasRequestedMore) {
      int lastInScreen = firstVisibleItem + visibleItemCount;
      if (lastInScreen >= totalItemCount) {
        Log.d(getClass().getName(), "onScroll lastInScreen - so load more");
        mHasRequestedMore = true;
        onLoadMoreItems();
      }
    }
  }

  private void onLoadMoreItems() {
    // stash all the data in our backing store
    setAlphaAdapter();
    // notify the adapter that we can update now
    adapter.notifyDataSetChanged();
    mHasRequestedMore = false;
  }
}
