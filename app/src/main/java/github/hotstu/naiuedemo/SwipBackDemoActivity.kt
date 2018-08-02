package github.hotstu.naiuedemo

import android.os.Bundle
import github.hotstu.naiue.arch.MOSwipbackActivity

class SwipBackDemoActivity : MOSwipbackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swip_back_demo)
    }
}
