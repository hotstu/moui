package io.github.hotstu.mouidemo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.github.hotstu.moui.util.MOStatusBarHelper
import io.github.hotstu.moui.widget.recycler.*
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : AppCompatActivity() {
    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        when(v.tag) {
            "MODialog" -> {
                val intent = Intent(this, DialogDemoActivity::class.java)
                startActivity(intent)
            }
            "MOFragment" -> {
                val intent = Intent(this, MoFragmentDemoActivity::class.java)
                startActivity(intent)
            }
            "SwipBackDemoActivity" -> {
                val intent = Intent(this, SwipBackDemoActivity::class.java)
                startActivity(intent)
            }
            else ->{

            }
        }
    }

    private val adapterDelegate: MOAdapterDelegate<MOTypedRecyclerAdapter> = object : MOAdapterDelegate<MOTypedRecyclerAdapter> {
        override fun onCreateViewHolder(adapter: MOTypedRecyclerAdapter, parent: ViewGroup): RecyclerView.ViewHolder {
            val inflate = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            return MOCommonViewHolder(inflate)
        }

        override fun onBindViewHolder(adapter: MOTypedRecyclerAdapter, holder: RecyclerView.ViewHolder, data: Any) {
            val commonHolder = holder as MOCommonViewHolder
            commonHolder.setText(android.R.id.text1, data as String)
            commonHolder.tag = data;
            commonHolder.setClickListener(onClickListener)
        }

        override fun isDelegateOf(clazz: Class<*>, item: Any, position: Int): Boolean {
            return clazz.isAssignableFrom(String::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MOStatusBarHelper.translucent(this)
        setContentView(R.layout.activity_home)
        val mAdapter = MOTypedRecyclerAdapter()
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = PredictiveLinearLayoutManager(this)
        val reachBottomListener: ReachBottomListener = object : ReachBottomListener() {
            override fun onReachBottom() {
                recyclerView.postDelayed({ this.resetLoading() }, 1000);
            }
        }
        recyclerView.addOnScrollListener(reachBottomListener)
        mAdapter.addDelegate(adapterDelegate)

        mAdapter.addItems(Arrays.asList("MODialog", "MOFragment", "SwipBackDemoActivity", "4", "5", "6", "7", "8", "9", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"))
    }
}

