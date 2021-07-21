package io.github.hotstu.mouidemo

import android.os.Bundle
import io.github.hotstu.moui.arch.MOSwipbackActivity

class SwipBackDemoActivity : MOSwipbackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swip_back_demo)
    }
}
