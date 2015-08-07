package com.looksphere.goindia.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import com.looksphere.goindia.R;
import com.looksphere.goindia.application.SwachhApplication;
import com.looksphere.goindia.customview.RoundedImageView;
import com.looksphere.goindia.model.TaggableFriends;
import com.looksphere.goindia.model.TaggableFriendsWrapper;

public class FriendsAdapter extends BaseAdapter  {


    private ArrayList<TaggableFriends> friendsList;
    private LayoutInflater mLayoutInflater;
    Typeface typeface;
    SparseBooleanArray checkStates = new SparseBooleanArray();
    Context context;
    ImageLoader imageLoader;

    public FriendsAdapter(Context context, TaggableFriendsWrapper taggableFriendsWrapper) {
        this.context = context;
        // get the layout inflater
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        friendsList = taggableFriendsWrapper.getData();
        imageLoader = ImageLoader.getInstance();
        typeface = SwachhApplication.abelRegular;
    }

    @Override
    public int getCount() {
        // getCount() represents how many items are in the list
        return friendsList.size();
    }

    @Override
    // get the data of an item from a specific position
    // i represents the position of the item in the list
    public Object getItem(int i) {
        return null;
    }

    @Override
    // get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        // create a ViewHolder reference
        ViewHolder holder;

        // check to see if the reused view is null or not, if is not null then
        // reuse it
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = mLayoutInflater.inflate(R.layout.facebook_friend_item, null);
            holder.itemName = (TextView) convertView
                    .findViewById(R.id.textView1);
            holder.itemName.setTypeface(typeface);
            holder.itemimage = (RoundedImageView) convertView
                    .findViewById(R.id.image);
            // the setTag is used to store the data within this view
            convertView.setTag(holder);

        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }
        // get the string item from the position "position" from array list to
        // put it on the TextView

        try {

            String stringItem = friendsList.get(position).getName();
            String url = friendsList.get(position).getPicture().getData().getUrl();

            if (stringItem != null) {
                if (holder.itemName != null) {
                    // set the item name on the TextView
                    holder.itemName.setText(stringItem);
                }
            }

            if (holder.itemimage == null) {
                holder.itemimage = new RoundedImageView(context);
            }

            imageLoader.displayImage(url, holder.itemimage);


        } catch (Exception e) {
            Log.e("error", String.valueOf(e.getMessage()));
        }

        return convertView;

    }

    /**
     * Static class used to avoid the calling of "findViewById" every time the
     * getView() method is called, because this can impact to your application
     * performance when your list is too big. The class is static so it cache
     * all the things inside once it's created.
     */
    private class ViewHolder {

        protected TextView itemName;
        protected RoundedImageView itemimage;
    }

    public boolean isChecked(int position) {
        return checkStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        checkStates.put(position, isChecked);
    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }





}
