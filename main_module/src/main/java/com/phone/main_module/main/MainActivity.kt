package com.phone.main_module.main

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.common_library.adapter.TabFragmentStatePagerAdapter
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.IBaseView
import com.phone.common_library.custom_view.LazyViewPager
import com.phone.common_library.custom_view.MineLazyViewPager
import com.phone.common_library.manager.ResourcesManager
import com.phone.main_module.BuildConfig
import com.phone.main_module.R
import com.phone.main_module.main.presenter.MainPresenterImpl
import com.phone.main_module.main.view.IMainView

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
class MainActivity : BaseMvpRxAppActivity<IBaseView, MainPresenterImpl>(), IMainView {

    private val TAG = MainActivity::class.java.simpleName
    private var tevPleaseAddComponents: TextView? = null
    private var layoutMain: LinearLayout? = null
    private var mineViewPager: MineLazyViewPager? = null
    private var layoutBottom: LinearLayout? = null
    private var tevFirstPage: TextView? = null
    private var tevProject: TextView? = null
    private var tevSquare: TextView? = null
    private var tevResourceCenter: TextView? = null
    private var tevMine: TextView? = null

    private val fragmentList: MutableList<Fragment> = ArrayList()

    override fun initLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        if (!BuildConfig.IS_MODULE) {
            //Jump with parameters
            val firstPageFragment = ARouter.getInstance()
                .build("/first_page_module/first_page") //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(firstPageFragment)
            //Jump with parameters
            val projectFragment = ARouter.getInstance()
                .build("/project_module/project") //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(projectFragment)
            //Jump with parameters
            val squareFragment = ARouter.getInstance()
                .build("/square_module/square") //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(squareFragment)
            //Jump with parameters
            val resourceFragment = ARouter.getInstance()
                .build("/resource_module/resource") //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(resourceFragment)
            //Jump with parameters
            val mineFragment = ARouter.getInstance()
                .build("/mine_module/mine") //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(mineFragment)
        }
    }

    override fun initViews() {
        tevPleaseAddComponents = findViewById<View>(R.id.tev_please_add_components) as TextView
        layoutMain = findViewById<View>(R.id.layout_main) as LinearLayout
        mineViewPager = findViewById<View>(R.id.mine_view_pager) as MineLazyViewPager
        layoutBottom = findViewById<View>(R.id.layout_bottom) as LinearLayout
        tevFirstPage = findViewById<View>(R.id.tev_first_page) as TextView
        tevProject = findViewById<View>(R.id.tev_project) as TextView
        tevSquare = findViewById<View>(R.id.tev_square) as TextView
        tevResourceCenter = findViewById<View>(R.id.tev_resource_center) as TextView
        tevMine = findViewById<View>(R.id.tev_mine) as TextView
        val fragmentStatePagerAdapter = TabFragmentStatePagerAdapter(
            supportFragmentManager, fragmentList
        )
        mineViewPager?.adapter = fragmentStatePagerAdapter
        mineViewPager?.setOnPageChangeListener(object : LazyViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                resetTabData(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        tevFirstPage?.setOnClickListener { v: View? ->
            resetTabData(
                0
            )
        }
        tevProject?.setOnClickListener { v: View? ->
            resetTabData(
                1
            )
        }
        tevSquare?.setOnClickListener { v: View? ->
            resetTabData(
                2
            )
        }
        tevResourceCenter?.setOnClickListener { v: View? ->
            resetTabData(
                3
            )
        }
        tevMine?.setOnClickListener { v: View? ->
            resetTabData(
                4
            )
        }
    }

    private fun resetTabData(position: Int) {
        mineViewPager?.currentItem = position
        tevFirstPage?.setTextColor(ResourcesManager.getColor(R.color.color_FF999999))
        tevProject?.setTextColor(ResourcesManager.getColor(R.color.color_FF999999))
        tevSquare?.setTextColor(ResourcesManager.getColor(R.color.color_FF999999))
        tevResourceCenter?.setTextColor(ResourcesManager.getColor(R.color.color_FF999999))
        tevMine?.setTextColor(ResourcesManager.getColor(R.color.color_FF999999))
        setToolbar(false, R.color.color_FF198CFF)
        when (position) {
            0 -> {
                tevFirstPage?.setTextColor(ResourcesManager.getColor(R.color.color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.black))
            }
            1 -> {
                tevProject?.setTextColor(ResourcesManager.getColor(R.color.color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.white))
            }
            2 -> {
                tevSquare?.setTextColor(ResourcesManager.getColor(R.color.color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.white))
            }
            3 -> {
                tevResourceCenter?.setTextColor(ResourcesManager.getColor(R.color.color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.white))
            }
            4 -> {
                tevMine?.setTextColor(ResourcesManager.getColor(R.color.color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.black))
            }
            else -> {
                tevFirstPage?.setTextColor(ResourcesManager.getColor(R.color.color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.black))
            }
        }
    }

    override fun initLoadData() {
        if (!BuildConfig.IS_MODULE) {
            tevPleaseAddComponents?.visibility = View.GONE
            layoutMain?.visibility = View.VISIBLE
            mineViewPager?.currentItem = 0
            tevFirstPage?.setTextColor(ResourcesManager.getColor(R.color.color_FFE066FF))
            tevProject?.setTextColor(ResourcesManager.getColor(R.color.color_FF999999))
            tevSquare?.setTextColor(ResourcesManager.getColor(R.color.color_FF999999))
            tevResourceCenter?.setTextColor(ResourcesManager.getColor(R.color.color_FF999999))
            tevMine?.setTextColor(ResourcesManager.getColor(R.color.color_FF999999))
            setToolbar(false, R.color.color_FF198CFF)
        } else {
            tevPleaseAddComponents?.visibility = View.VISIBLE
            layoutMain?.visibility = View.GONE
        }
    }

    override fun attachPresenter(): MainPresenterImpl {
        return MainPresenterImpl(this)
    }

    override fun showLoading() {
        if (!mLoadView.isShown) {
            mLoadView.visibility = View.VISIBLE
            mLoadView.start()
        }
    }

    override fun hideLoading() {
        if (mLoadView.isShown) {
            mLoadView.stop()
            mLoadView.visibility = View.GONE
        }
    }

    override fun mainDataSuccess(success: String) {
        showToast(success, true)
    }

    override fun mainDataError(error: String) {
        showToast(error, true)
    }

}