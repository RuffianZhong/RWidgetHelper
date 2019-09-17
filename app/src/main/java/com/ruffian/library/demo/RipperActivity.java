package com.ruffian.library.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ruffian.library.widget.RTextView;


/**
 * 水波纹效果展示
 *
 * @author ZhongDaFeng
 */
public class RipperActivity extends AppCompatActivity {
    private RTextView tvTag;
    private RTextView btnUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripper);
        tvTag = (RTextView) findViewById(R.id.tv_tag);
        btnUpdate = (RTextView) findViewById(R.id.tv_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean enabled = tvTag.isEnabled();
                tvTag.setEnabled(!enabled);
                btnUpdate.setText(String.valueOf(!enabled));
            }
        });
    }
}
