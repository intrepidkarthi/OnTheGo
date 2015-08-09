package com.looksphere.goindia.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.looksphere.goindia.R;
import com.looksphere.goindia.activity.SwachhMapActivity;
import com.looksphere.goindia.chat.ChatActivity;
import com.looksphere.goindia.controller.SharedPreferencesController;
import com.looksphere.goindia.customview.RoundedImageView;
import com.looksphere.goindia.model.SwachhFeedItem;
import com.looksphere.goindia.utils.AppConstants;
import com.looksphere.goindia.utils.AppController;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SwachhFeedItem> feedItems;
    ImageLoader imageLoader;
    SharedPreferencesController sharedPreferencesController;
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    //private final Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimations = new HashMap<>();

    public FeedListAdapter(Activity activity, List<SwachhFeedItem> feedItems) {
        this.activity = activity;
        //imageLoader = AppController.getInstance(activity.getApplicationContext()).getImageLoader();
        imageLoader = ImageLoader.getInstance();
        this.feedItems = feedItems;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.feed_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.timestamp = (TextView) convertView
                    .findViewById(R.id.timestamp);
            holder.location = (TextView) convertView.findViewById(R.id.location);
            holder.profilePic = (RoundedImageView) convertView
                    .findViewById(R.id.profilePic);
            holder.feedDescription = (TextView) convertView.findViewById(R.id.description);
            holder.feedImageView = (ImageView) convertView
                    .findViewById(R.id.feedImage1);
            holder.severity = (TextView) convertView.findViewById(R.id.priority);
            holder.mapButton = (ImageView) convertView.findViewById(R.id.map);
            holder.iSwachh = (ImageView) convertView.findViewById(R.id.swachh);
            holder.likesCount = (TextView) convertView.findViewById(R.id.like_count);
            holder.commentLayout = (RelativeLayout) convertView.findViewById(R.id.single_comment_layout);
            holder.likeLayout = (RelativeLayout) convertView.findViewById(R.id.like_layout);
            holder.commentProfilePic = (RoundedImageView) convertView.findViewById(R.id.commentuser);
            holder.commentText = (TextView) convertView.findViewById(R.id.usercomment);
            holder.like = (ImageView) convertView.findViewById(R.id.like);
            holder.comment = (ImageView) convertView.findViewById(R.id.comment);
            holder.isHeart = false;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(activity);


        final SwachhFeedItem item = feedItems.get(position);

        holder.name.setText(item.getUserHandle());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.timestamp.setText(timeAgo);

        holder.feedDescription.setText(item.getDescription().replace("minutes", "mins").replace("hours", "hr").replace("days", "d").replace(" ago", ""));

        // user profile pic
        imageLoader.displayImage(item.getProfilePic(), holder.profilePic);


        if(item.getSeverity().equals("HIGH"))
            holder.severity.setText("High priority");
        else if(item.getSeverity().equals("MEDIUM"))
            holder.severity.setText("Medium priority");
        else if(item.getSeverity().equals("LOW"))
            holder.severity.setText("Low priority");


        // Feed image
        if (item.getImage() != null) {
            imageLoader.displayImage(item.getImage(), holder.feedImageView);
            holder.feedImageView.setVisibility(View.VISIBLE);

        } else {
            holder.feedImageView.setVisibility(View.GONE);
        }

        holder.feedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("image click", "it happens " + item.getImage());
                try {
//                    Intent newintent = new Intent(activity.getApplicationContext(), ImageFullViewActivity.class);
//                    newintent.putExtra("url", item.getImage());
//                    activity.startActivity(newintent);

                    Intent intent = new Intent(activity, SwachhMapActivity.class);
                    intent.putExtra("latitude", item.getLatitude());
                    intent.putExtra("longitude", item.getLongitude());
                    intent.putExtra("address", item.getLocationAddress());
                    intent.putExtra("description", item.getDescription());
                    intent.putExtra("landmark", item.getLandmark());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


                } catch (Exception e) {

                }
            }
        });


        holder.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SwachhMapActivity.class);
                intent.putExtra("latitude", item.getLatitude());
                intent.putExtra("longitude", item.getLongitude());
                intent.putExtra("address", item.getLocationAddress());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("landmark", item.getLandmark());
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        holder.location.setText(item.getLocationAddress());
        holder.location.setSelected(true);

        if (item.getLikesCount() > 0) {
            holder.likeLayout.setVisibility(View.VISIBLE);
            holder.likesCount.setText(item.getLikesCount() + " likes");
        } else
            holder.likeLayout.setVisibility(View.GONE);


        if (item.isLiked()) {
            holder.isHeart = true;
            holder.like.setImageResource(0);
            holder.like.setImageResource(R.drawable.ic_heart_click);
            holder.like.setTag(item.getId());
        } else {
            holder.isHeart = false;
            holder.like.setImageResource(0);
            holder.like.setImageResource(R.drawable.ic_heart);
            holder.like.setTag(item.getId());
        }


        if (item.getCommentsCount() > 0) {
            holder.commentLayout.setVisibility(View.VISIBLE);
            imageLoader.displayImage(AppConstants.GRAPH_API_URL + item.getCommentItems().get(0).getUserId() + AppConstants.GRAPH_API_IMAGE_SUFFIX
                    , holder.commentProfilePic);
            holder.commentText.setText(item.getCommentItems().get(0).getComment());

        } else {
            holder.commentLayout.setVisibility(View.GONE);
        }


        holder.commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity, ChatActivity.class);
//                if (item.getCommentsCount() > 0) {
//                    intent.putExtra("comments", item.getCommentItems());
//                    intent.putExtra("feedId", item.getId());
//                    intent.putExtra("data", true);
//                } else {
//                    intent.putExtra("data", false);
//                    intent.putExtra("feedId", item.getId());
//                }
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.isHeart) {
                    holder.like.setImageResource(0);
                    holder.like.setImageResource(R.drawable.ic_heart);
                    holder.like.setTag(item.getId());
                    holder.isHeart = false;
                } else {
                    holder.like.setImageResource(0);
                    holder.like.setImageResource(R.drawable.ic_heart_click);
                    holder.like.setTag(item.getId());
                    holder.isHeart = true;
                }


                JSONObject requestObj = new JSONObject();
                try {
                    requestObj.put("dirtId", v.getTag());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_HOST_ADDRESS + AppConstants.LIKE_POST, requestObj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            Log.d("clicked", response.toString());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("", "Error: " + error.getMessage());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> actualHeader = new HashMap<String, String>();
                        String apiKey = sharedPreferencesController.getString("APIKey");
                        actualHeader.put("APIKey", apiKey);
                        return actualHeader;
                    }
                };

                // Adding request to volley request queue
                AppController.getInstance(activity.getApplicationContext()).addToRequestQueue(req);



                AnimatorSet animatorSet = new AnimatorSet();
                //likeAnimations.put(holder, animatorSet);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.like, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.like, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.like, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.like.setImageResource(R.drawable.ic_heart_click);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

//                animatorSet.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        resetLikeAnimationState(holder);
//                    }
//                });

                animatorSet.start();




            }
        });


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("comment count", "" + item.getCommentsCount());
                Intent intent = new Intent(activity, ChatActivity.class);
//                if (item.getCommentsCount() > 0) {
//                    intent.putExtra("comments", item.getCommentItems());
//                    intent.putExtra("feedId", item.getId());
//                    intent.putExtra("data", true);
//                } else {
//                    intent.putExtra("data", false);
//                    intent.putExtra("feedId", item.getId());
//                }
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });

        holder.iSwachh.setTag(item.getId());

        holder.iSwachh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Object dirtId = v.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(R.string.swachhthis)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //fire the missile here dude
                                JSONObject requestObj = new JSONObject();
                                try {
                                    requestObj.put("dirtId", dirtId);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_HOST_ADDRESS + AppConstants.ASSIGNING_TASK, requestObj, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if (response != null) {

                                            Toast toast = Toast.makeText(activity, "Successfully assigned to you !!!", Toast.LENGTH_SHORT);
                                            toast.show();
                                            Log.d("", response.toString());
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d("", "Error: " + error.getMessage());
                                    }
                                }) {
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> actualHeader = new HashMap<String, String>();
                                        String apiKey = sharedPreferencesController.getString("APIKey");
                                        actualHeader.put("APIKey", apiKey);
                                        return actualHeader;
                                    }
                                };

                                // Adding request to volley request queue
                                AppController.getInstance(activity.getApplicationContext()).addToRequestQueue(req);


                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
        });


//        holder.completeDirt.setTag(item.getId().toString());
//        holder.completeDirt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JSONObject requestObj = new JSONObject();
//                try {
//                    requestObj.put("dirtId", view.getTag());
//                    requestObj.put("description","Its good one out.");
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_HOST_ADDRESS+"/protected/completedirt", requestObj, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        if (response != null) {
//
//                            Toast toast = Toast.makeText(activity, "Successfully completed the task !!!", Toast.LENGTH_SHORT);
//                            toast.show();
//                            Log.d("",response.toString());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyLog.d("", "Error: " + error.getMessage());
//                    }
//                }){
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> actualHeader = new HashMap<String,String>();
//                        String apiKey = sharedPreferencesController.getString("APIKey");
//                        actualHeader.put("APIKey", apiKey);
//                        return actualHeader;
//                    }
//                };
//
//                // Adding request to volley request queue
//                AppController.getInstance(activity.getApplicationContext()).addToRequestQueue(req);
//            }
//        });

        return convertView;
    }


    static class ViewHolder {
        TextView name;
        TextView timestamp;
        TextView location;
        RoundedImageView profilePic;
        RoundedImageView commentProfilePic;
        TextView commentText;
        ImageView feedImageView;
        ImageView like;
        ImageView comment;
        ImageView assignToMe;
        ImageView completeDirt;
        TextView likesCount;
        TextView severity;
        RelativeLayout commentLayout;
        RelativeLayout likeLayout;
        int commentCount;
        boolean isHeart = false;
        TextView feedDescription;
        ImageView iSwachh;
        ImageView mapButton;
    }


    public void append(List<SwachhFeedItem> extraFeedItems) {

        int listSize = extraFeedItems.size();
        for(int i = 0; i < listSize; ++i) {
            Log.d("feed items ", extraFeedItems.get(i).getImage());
            this.feedItems.add(extraFeedItems.get(i));
        }


    }


}
