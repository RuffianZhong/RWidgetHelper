package com.ruffian.library.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(this);
        findViewById(R.id.iv).setOnClickListener(this);
        findViewById(R.id.et).setOnClickListener(this);
        findViewById(R.id.view_group).setOnClickListener(this);
        findViewById(R.id.view).setOnClickListener(this);
        findViewById(R.id.gradient).setOnClickListener(this);
        findViewById(R.id.checked).setOnClickListener(this);
        findViewById(R.id.ripper).setOnClickListener(this);
        findViewById(R.id.shadow).setOnClickListener(this);
        findViewById(R.id.tv_spt).setSelected(true);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv:
                intent = new Intent(this, RTextViewActivity.class);
                break;
            case R.id.iv:
                intent = new Intent(this, RImageViewActivity.class);
                break;
            case R.id.et:
                intent = new Intent(this, REditTextActivity.class);
                break;
            case R.id.view_group:
                intent = new Intent(this, RViewGroupActivity.class);
                break;
            case R.id.view:
                intent = new Intent(this, RViewActivity.class);
                break;
            case R.id.gradient:
                intent = new Intent(this, GradientActivity.class);
                break;
            case R.id.checked:
                intent = new Intent(this, CheckedActivity.class);
                break;
            case R.id.ripper:
                intent = new Intent(this, RipperActivity.class);
                break;
            case R.id.shadow:
                intent = new Intent(this, ShadowActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
