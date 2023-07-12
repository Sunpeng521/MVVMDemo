package com.phone.module_main

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_common.BuildConfig
import com.phone.library_common.adapter.TabFragmentStatePagerAdapter
import com.phone.library_mvp.BaseMvpRxAppActivity
import com.phone.library_base.base.IBaseView
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.common.ConstantData
import com.phone.library_custom_view.custom_view.MineLazyViewPager
import com.phone.module_main.presenter.MainPresenterImpl
import com.phone.module_main.view.IMainView


/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */

@Route(path = ConstantData.Route.ROUTE_MAIN)
class MainActivity : BaseMvpRxAppActivity<IBaseView, MainPresenterImpl>(), IMainView {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var layoutPleaseAddComponents: LinearLayout? = null
    private var tevPleaseAddComponents: TextView? = null
    private var layoutMain: LinearLayout? = null
    private var mineViewPager: MineLazyViewPager? = null
    private var layoutBottom: LinearLayout? = null
    private var tevFirstPage: TextView? = null
    private var tevProject: TextView? = null
    private var tevSquare: TextView? = null
    private var tevResourceCenter: TextView? = null
    private var tevMine: TextView? = null

    private val fragmentList = mutableListOf<Fragment>()

    override fun initLayoutId(): Int {
        return R.layout.main_activity_main
    }

    override fun initData() {
        if (!BuildConfig.IS_MODULE) {
            //Jump with parameters
            val firstPageFragment = ARouter.getInstance()
                .build(ConstantData.Route.ROUTE_HOME_FRAGMENT) //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(firstPageFragment)
            //Jump with parameters
            val projectFragment = ARouter.getInstance()
                .build(ConstantData.Route.ROUTE_PROJECT_FRAGMENT) //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(projectFragment)
            //Jump with parameters
            val squareFragment = ARouter.getInstance()
                .build(ConstantData.Route.ROUTE_SQUARE_FRAGMENT) //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(squareFragment)
            //Jump with parameters
            val resourceFragment = ARouter.getInstance()
                .build(ConstantData.Route.ROUTE_RESOURCE_FRAGMENT) //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(resourceFragment)
            //Jump with parameters
            val mineFragment = ARouter.getInstance()
                .build(ConstantData.Route.ROUTE_MINE_FRAGMENT) //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                .navigation() as Fragment
            fragmentList.add(mineFragment)
        }

    }

    override fun initViews() {
        layoutPleaseAddComponents =
            findViewById<View>(R.id.layout_please_add_components) as LinearLayout
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
        mineViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
        tevFirstPage?.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
        tevProject?.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
        tevSquare?.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
        tevResourceCenter?.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
        tevMine?.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
        setToolbar(false, R.color.library_color_FF198CFF)
        when (position) {
            0 -> {
                tevFirstPage?.setTextColor(ResourcesManager.getColor(R.color.library_color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.library_black))
            }

            1 -> {
                tevProject?.setTextColor(ResourcesManager.getColor(R.color.library_color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.library_white))
            }

            2 -> {
                tevSquare?.setTextColor(ResourcesManager.getColor(R.color.library_color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.library_white))
            }

            3 -> {
                tevResourceCenter?.setTextColor(ResourcesManager.getColor(R.color.library_color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.library_white))
            }

            4 -> {
                tevMine?.setTextColor(ResourcesManager.getColor(R.color.library_color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.library_black))
            }

            else -> {
                tevFirstPage?.setTextColor(ResourcesManager.getColor(R.color.library_color_FFE066FF))
                layoutBottom?.setBackgroundColor(ResourcesManager.getColor(R.color.library_black))
            }
        }
    }

    override fun initLoadData() {
        if (!BuildConfig.IS_MODULE) {
            setToolbar(false, R.color.library_color_FF198CFF)
            layoutPleaseAddComponents?.visibility = View.GONE
            layoutMain?.visibility = View.VISIBLE
            mineViewPager?.currentItem = 0
            tevFirstPage?.setTextColor(ResourcesManager.getColor(R.color.library_color_FFE066FF))
            tevProject?.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
            tevSquare?.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
            tevResourceCenter?.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
            tevMine?.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
        } else {
            setToolbar(false, R.color.library_color_FF198CFF)
            layoutPleaseAddComponents?.visibility = View.VISIBLE
            layoutMain?.visibility = View.GONE
        }
    }

    override fun attachPresenter(): MainPresenterImpl {
        return MainPresenterImpl(this)
    }

    override fun mainDataSuccess(success: String) {
        if (!mRxAppCompatActivity.isFinishing) {
            showToast(success, true)
        }
    }

    override fun mainDataError(error: String) {
        if (!mRxAppCompatActivity.isFinishing) {
            showToast(error, true)
        }
    }

}