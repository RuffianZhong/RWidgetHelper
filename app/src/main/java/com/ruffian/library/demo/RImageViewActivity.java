package com.ruffian.library.demo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ruffian.library.widget.RImageView;

/**
 * @author ZhongDaFeng
 */
public class RImageViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_iv);
        RImageView imageView = (RImageView) findViewById(R.id.image);
      //  Glide.with(this).load("http://pic26.nipic.com/20121221/9252150_142515375000_2.jpg").into(imageView);
        loadNormal(this,"http://pic26.nipic.com/20121221/9252150_142515375000_2.jpg",imageView);

    }

    public static void loadNormal(Context mContext, String path, ImageView mImageView) {

        DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions()
                .crossFade();
        Glide.with(mContext)
                .load(path)
                .apply(getDefaultNoCenterOptions())
                .transition(transitionOptions)
                .into(mImageView);
    }

    public static RequestOptions getDefaultNoCenterOptions() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        return options;
    }
}
