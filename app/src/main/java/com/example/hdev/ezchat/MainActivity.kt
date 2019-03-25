package com.example.hdev.ezchat

import android.annotation.SuppressLint
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.github.ksoichiro.android.observablescrollview.ScrollUtils
import com.nineoldandroids.view.ViewHelper
import kotlinx.android.synthetic.main.profile_layout.image_cover
import kotlinx.android.synthetic.main.profile_layout.scroll
import kotlinx.android.synthetic.main.profile_layout.toolbar

class MainActivity : AppCompatActivity(), ObservableScrollViewCallbacks {
    private var mParallaxImageHeight: Int = 0

    @RequiresApi(VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)
        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0F, R.color.colorPrimary))
        scroll.setScrollViewCallbacks(this)
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.image_profile_height)

        toolbar.navigationIcon = resources.getDrawable(R.drawable.back)
        toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {
                Toast.makeText(this, "Do some thing", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        onScrollChanged(scroll.getCurrentScrollY(), false, false)
    }

    override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
    }

    @SuppressLint("ResourceAsColor")
    override fun onScrollChanged(scrollY: Int, firstScroll: Boolean, dragging: Boolean) {
        val baseColor = resources.getColor(R.color.colorWhite)
        val alpha = Math.min(1F, (scrollY / mParallaxImageHeight).toFloat())
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor))
        ViewHelper.setTranslationY(image_cover, (scrollY / 2).toFloat())
    }

    override fun onDownMotionEvent() {
    }
}
