package com.ruiwenliu.LovePraise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private PraiseLayout mPrakseLayouts=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrakseLayouts= (PraiseLayout) findViewById(R.id.act_main_pl);
        findViewById(R.id.act_main_fl).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //手指误差x/y轴计算
                        float x=motionEvent.getRawX()-200;
                        float y=motionEvent.getRawY()-500;
                        mPrakseLayouts.startPublish(x,y);
                        break;
                }
                return false;
            }
        });

    }
}
