package com.looksphere.goindia.activity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import com.looksphere.goindia.R;


public class ImageFullViewActivity extends Activity {

    ImageView imageView;
    ImageLoader imageLoader;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        setContentView(R.layout.activity_image_full_view);

        if(getIntent()!=null)
        {
            url = getIntent().getStringExtra("url");
        }

        initUI();

    }

   void  initUI()
    {
        imageView = (ImageView) findViewById(R.id.image);
        imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, imageView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_full_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
