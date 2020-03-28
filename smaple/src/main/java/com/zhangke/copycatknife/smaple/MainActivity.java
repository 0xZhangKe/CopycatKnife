package com.zhangke.copycatknife.smaple;

import android.os.Bundle;
import android.widget.TextView;

import com.zhangke.annotations.BindView;
import com.zhangke.copycatknife.CopycatKnife;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_main_01)
    TextView tvMain01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CopycatKnife.bind(this);
        tvMain01.setText("Hello CopycatKnife!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
