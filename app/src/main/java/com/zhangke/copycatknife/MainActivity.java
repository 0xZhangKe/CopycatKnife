package com.zhangke.copycatknife;

import android.os.Bundle;
import android.view.View;

import com.zhangke.annotations.BindView;
import com.zhangke.annotations.OnClick;

import androidx.appcompat.app.AppCompatActivity;

//import com.zhangke.annotations.BindView;
//import com.zhangke.annotations.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_main_01)
    View tvMain01;

    @BindView(R.id.tv_main_02)
    View tvMain02;

    @BindView(R.id.tv_main_03)
    View tvMain03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.tv_main_01)
    void onViewClick(View view) {

    }
}
