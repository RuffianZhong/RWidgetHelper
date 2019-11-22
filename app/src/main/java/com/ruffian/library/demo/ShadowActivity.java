package com.ruffian.library.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruffian.library.widget.RImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * 阴影效果展示
 *
 * @author ZhongDaFeng
 */
public class ShadowActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private final boolean isAuthor = false;//作者自测逻辑

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //demo 示例布局 activity_shadow
        setContentView(R.layout.activity_shadow);

        if (!isAuthor) return;
        /**
         * 以下代码为作者自测请忽略test
         */
        //test
        setContentView(R.layout.activity_list);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) list.add("Item" + (i + 1));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<String> mList;

        public MyAdapter(List<String> list) {
            this.mList = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(ShadowActivity.this).inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(mList.get(position));
            //test
            int height = dp2px(150) + (position) * 20;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            holder.layout_ll.setLayoutParams(params);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private LinearLayout layout_ll;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_text);
            layout_ll = itemView.findViewById(R.id.layout_ll);
        }
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
