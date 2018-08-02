package github.hotstu.naiuedemo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import github.hotstu.naiue.arch.MOFragment
import github.hotstu.naiue.arch.MOFragmentActivity

class MoFragmentDemoActivity : MOFragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startFragment(ColorFragment.getInstance(Color.GREEN));
    }


    class ColorFragment: MOFragment() {
        companion object {
            fun getInstance(color:Int ):ColorFragment {
                val colorFragment = ColorFragment()
                val bundle = Bundle()
                bundle.putInt("color", color)
                colorFragment.arguments = bundle
                return colorFragment
            }
        }
        /**
         * onCreateView
         */
        override fun onCreateView(): View {
            val v = FrameLayout(context)
            v.fitsSystemWindows = true
            v.setBackgroundColor( arguments?.getInt("color", Color.BLUE)?:Color.BLUE)
            return v
        }

    }
}
