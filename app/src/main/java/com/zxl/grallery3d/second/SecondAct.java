package com.zxl.grallery3d.second;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zxl.grallery3d.R;

public class SecondAct extends Activity {

    private TextView tvTitle;
    private GalleryView gallery;
    private GalleryViewAdapter adapter;
    private boolean isalive = true;
    private int count_drawble = 0;  //总共的图片大小
    private int cur_index = 0;
    private static int MSG_UPDATE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_second);

        initRes();
    }

    private void initRes() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        gallery = (GalleryView) findViewById(R.id.mygallery);

        gallery.setOnItemClickListener(new OnItemClickListener() {            // 设置点击事件监听
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SecondAct.this, "img " + (position % 6) + " selected", Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new GalleryViewAdapter(this);
        adapter.createReflectedImages();    // 创建倒影效果
        gallery.setAdapter(adapter);
        gallery.setSelection(3);
        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {    // 设置选择事件监听
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvTitle.setText(adapter.titles[position % 6]);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        count_drawble = adapter.getCount();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isalive) {
                    cur_index = cur_index % count_drawble;  // 图片区间[0,count_drawable)
                    //Log.i(TAG, "cur_index"+ cur_index +" count_drawble --"+ count_drawble);
                    //msg.arg1 = cur_index
                    Message msg = mhandler.obtainMessage(MSG_UPDATE, cur_index, 0);
                    mhandler.sendMessage(msg);
                    //更新时间间隔为 3s
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    cur_index++;  //放置在Thread.sleep(2000) ；防止mhandler处理消息的同步性，导致cur_index>=count_drawble
                }
            }
        }).start();
    }

    //通过handler来更新主界面  mgallery.setSelection(positon),选中第position的图片，然后调用OnItemSelectedListener监听改变图像
    private Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE) {

                gallery.setSelection(msg.arg1);
                //直接更改图片 ，不触发Gallery.OnItemSelectedListener监听
                //imgSwitcher.setBackgroundResource(imgAdapter.getResId(msg.arg1));
            }
        }
    };

    public void onDestroy() {
        super.onDestroy();
        isalive = false;
    }
}