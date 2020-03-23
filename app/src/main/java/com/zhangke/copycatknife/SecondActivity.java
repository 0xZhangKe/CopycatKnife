package com.zhangke.copycatknife;

import android.os.Bundle;
import android.view.View;

import com.zhangke.annotations.BindView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by ZhangKe on 2020/3/19.
 */
public class SecondActivity extends AppCompatActivity {

    @BindView(R.id.tv_second_01)
    View tvSecond01;

    @BindView(R.id.tv_second_02)
    View tvSecond02;

    @BindView(R.id.tv_second_03)
    View tvSecond03;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }


}
