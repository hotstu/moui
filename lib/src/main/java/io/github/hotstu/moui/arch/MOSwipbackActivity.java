package io.github.hotstu.moui.arch;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import io.github.hotstu.moui.widget.swipback.MOSwipeBackLayout;
import io.github.hotstu.moui.widget.swipback.Utils;

import static io.github.hotstu.moui.widget.swipback.MOSwipeBackLayout.EDGE_LEFT;


public abstract class MOSwipbackActivity extends MOActivity {
    private MOSwipeBackLayout.SwipeListener listener = new MOSwipeBackLayout.SwipeListener() {
        @Override
        public void onScrollStateChange(int state, float scrollPercent) {
            if (scrollPercent >= 1.0F){
                finish();
            }
        }

        @Override
        public void onScroll(int edgeFlag, float scrollPercent) {

        }

        @Override
        public void onEdgeTouch(int edgeFlag) {
            getWindow().getDecorView().setBackgroundColor(0);
            Utils.convertActivityToTranslucent(MOSwipbackActivity.this);
        }

        @Override
        public void onScrollOverThreshold() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        //swipe 的时候会将window的背景设为透明，使用者需要负责将layout添加背景色否则view会透明
        //int background = MOResHelper.getAttrColor(this, android.R.attr.windowBackground);
        //decorChild.setBackgroundColor(background);
        decor.removeView(decorChild);
        MOSwipeBackLayout wrap = MOSwipeBackLayout.wrap(decorChild, EDGE_LEFT);
        wrap.addSwipeListener(listener);
        decor.addView(wrap);
    }
}