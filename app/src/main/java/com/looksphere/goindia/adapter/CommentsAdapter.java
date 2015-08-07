package com.looksphere.goindia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import com.looksphere.goindia.R;
import com.looksphere.goindia.customview.RoundedImageView;
import com.looksphere.goindia.model.CommentItem;
import com.looksphere.goindia.utils.AppConstants;

/**
 * Created by SPARK on 18/10/14.
 */
public class CommentsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CommentItem> commentItems;
    LayoutInflater mInflater;
    ImageLoader imageLoader;

    public CommentsAdapter(Context context, ArrayList<CommentItem> comments){
        this.context = context;
        this.commentItems = comments;
        imageLoader = ImageLoader.getInstance();
        mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return commentItems.size();
    }

    @Override
    public Object getItem(int position) {
        return commentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.comments_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.roundedImageView = (RoundedImageView) convertView.findViewById(R.id.picture);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.username);
            viewHolder.commentText = (TextView) convertView.findViewById(R.id.commenttext);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        imageLoader.displayImage(AppConstants.GRAPH_API_URL+commentItems.get(position).getUserId()+AppConstants.GRAPH_API_IMAGE_SUFFIX, viewHolder.roundedImageView);
        viewHolder.userName.setText(commentItems.get(position).getUserHandle());
        viewHolder.commentText.setText(commentItems.get(position).getComment());


        return convertView;
    }

    static class ViewHolder
    {
        RoundedImageView roundedImageView;
        TextView userName;
        TextView commentText;
    }

}
