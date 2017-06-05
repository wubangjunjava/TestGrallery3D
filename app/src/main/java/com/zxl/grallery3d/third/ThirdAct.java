package com.zxl.grallery3d.third;

import android.app.Activity;
import android.os.Bundle;

import com.zxl.grallery3d.R;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/6/5 18:00
 */

public class ThirdAct extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_third);

        int[] images = {R.drawable.photo1, R.drawable.photo2,
                R.drawable.photo3, R.drawable.photo4, R.drawable.photo5,
                R.drawable.photo6, R.drawable.photo7, R.drawable.photo8,};

        GalleryFlowAdapter adapter = new GalleryFlowAdapter(this, images);
        adapter.createReflectedImages();

        GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.gallery_flow);
        galleryFlow.setAdapter(adapter);

    }
}
