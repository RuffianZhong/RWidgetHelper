package com.ruffian.library.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * 阴影效果展示
 *
 * @author ZhongDaFeng
 */
public class ShadowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
        findViewById(R.id.layout_ll).setClickable(true);
    }
}
