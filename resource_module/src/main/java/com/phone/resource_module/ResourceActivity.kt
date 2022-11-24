package com.phone.resource_module

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.phone.common_library.BaseApplication
import com.phone.common_library.adapter.TabFragmentStatePagerAdapter
import com.phone.common_library.adapter.TabNavigatorAdapter
import com.phone.common_library.base.BaseMvvmAppRxActivity
import com.phone.common_library.bean.TabBean
import com.phone.common_library.manager.*
import com.phone.resource_module.databinding.ActivityResourceBinding
import com.phone.resource_module.fragment.AndroidAndJsFragment
import com.phone.resource_module.fragment.SubResourceFragment
import com.phone.resource_module.view.IResourceView
import com.phone.resource_module.view_model.ResourceViewModelImpl
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

class ResourceActivity :
    BaseMvvmAppRxActivity<ResourceViewModelImpl, ActivityResourceBinding>(),
    IResourceView {

    private val TAG = ResourceActivity::class.java.simpleName
    private var fragmentStatePagerAdapter: TabFragmentStatePagerAdapter? = null

    override fun initLayoutId() = R.layout.activity_resource

    override fun initViewModel() =
        ViewModelProvider(rxAppCompatActivity).get(ResourceViewModelImpl::class.java)

    override fun initData() {
    }

    override fun initObservers() {
        viewModel.tabRxActivitySuccess.observe(this, {
            if (it != null && it.size > 0) {
                LogManager.i(TAG, "onChanged*****tabRxActivitySuccess")
                resourceTabDataSuccess(it)
            } else {
                resourceTabDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
            }
        })
        viewModel.tabRxActivityError.observe(this, {
            if (!TextUtils.isEmpty(it)) {
                LogManager.i(TAG, "onChanged*****tabRxActivityError")
                resourceTabDataError(it)
            }
        })
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FF198CFF)
    }

    override fun initLoadData() {
        LogManager.i(TAG, "ResourceActivity initLoadData")
        initResourceTabData()
    }

    override fun showLoading() {
        if (!mDatabind.loadView.isShown()) {
            mDatabind.loadView.setVisibility(View.VISIBLE)
            mDatabind.loadView.start()
        }
    }

    override fun hideLoading() {
        if (mDatabind.loadView.isShown()) {
            mDatabind.loadView.stop()
            mDatabind.loadView.setVisibility(View.GONE)
        }
    }

    override fun resourceTabDataSuccess(success: MutableList<TabBean>) {
        val fragmentList = mutableListOf<Fragment>()
        val dataList = mutableListOf<TabBean>()
        val tabBean = TabBean()
        tabBean.name = ResourcesManager.getString(R.string.android_and_js_interactive)
        dataList.add(tabBean)
        dataList.addAll(success)

        for (i in dataList) {
            if (ResourcesManager.getString(R.string.android_and_js_interactive).equals(i.name)) {
                fragmentList.add(AndroidAndJsFragment())
            } else {
                fragmentList.add(SubResourceFragment().apply {
                    //想各个fragment传递信息
                    val bundle = Bundle()
                    bundle.putInt("type", 20)
                    bundle.putInt("tabId", i.id)
                    bundle.putString("name", i.name)
                    arguments = bundle
                })
            }
        }

        fragmentStatePagerAdapter =
            TabFragmentStatePagerAdapter(
                supportFragmentManager,
                fragmentList
            )
        mDatabind.mineViewPager2.setAdapter(fragmentStatePagerAdapter)
        //下划线绑定
        val commonNavigator = CommonNavigator(rxAppCompatActivity)
        commonNavigator.adapter = getCommonNavigatorAdapter(dataList)
        mDatabind.tabLayout.navigator = commonNavigator
        MagicIndicatorManager.bindForViewPager(mDatabind.mineViewPager2, mDatabind.tabLayout)
        hideLoading()
    }

    override fun resourceTabDataError(error: String) {
        if (!rxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(20f),
                ScreenManager.dpToPx(20f),
                18,
                ResourcesManager.getColor(R.color.white),
                ResourcesManager.getColor(R.color.color_FFE066FF),
                ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f),
                error,
                true
            )
        }
        hideLoading()
    }

    private fun initResourceTabData() {
        if (RetrofitManager.isNetworkAvailable()) {
            showLoading()
            viewModel.resourceTabData2()
        } else {
            resourceTabDataError(resources!!.getString(R.string.please_check_the_network_connection))
        }
    }

    /**
     * 获取下划线根跟字适配器
     */
    private fun getCommonNavigatorAdapter(tabList: MutableList<TabBean>): CommonNavigatorAdapter {
        return TabNavigatorAdapter(mutableListOf<String>().apply {
            //将tab转换为String
            tabList.forEach {
                it.name?.let { it1 -> add(it1) }
            }
        }) {
            mDatabind.mineViewPager2.currentItem = it
        }
    }


}