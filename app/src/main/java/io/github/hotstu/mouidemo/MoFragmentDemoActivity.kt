package io.github.hotstu.mouidemo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import io.github.hotstu.moui.arch.MOFragment
import io.github.hotstu.moui.arch.MOFragmentActivity

class MoFragmentDemoActivity : MOFragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startFragment(ColorFragment.getInstance(Color.GREEN));
        startFragment(ColorFragment.getInstance(Color.BLUE));
    }


    class ColorFragment: MOFragment() {
        companion object {
            fun getInstance(color:Int ): ColorFragment {
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
            val v = FrameLayout(requireContext())
            v.fitsSystemWindows = true
            v.setBackgroundColor( arguments?.getInt("color", Color.BLUE)?:Color.BLUE)
            return v
        }

    }
}
