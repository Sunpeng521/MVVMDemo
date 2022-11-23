package com.phone.project_module

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.common_library.BaseApplication
import com.phone.common_library.adapter.TabFragmentStatePagerAdapter
import com.phone.common_library.adapter.TabNavigatorAdapter
import com.phone.common_library.base.BaseMvvmRxFragment
import com.phone.common_library.bean.TabBean
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.MagicIndicatorManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.project_module.databinding.FragmentProjectBinding
import com.phone.project_module.fragment.SubProjectFragment
import com.phone.project_module.view.IProjectView
import com.phone.project_module.view_model.ProjectViewModelImpl
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

@Route(path = "/project_module/project")
class ProjectFragment : BaseMvvmRxFragment<ProjectViewModelImpl, FragmentProjectBinding>(),
    IProjectView {

    private val TAG = ProjectFragment::class.java.simpleName
    private var fragmentStatePagerAdapter: TabFragmentStatePagerAdapter? = null

    override fun initLayoutId() = R.layout.fragment_project

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() =
        ViewModelProvider(this).get(ProjectViewModelImpl::class.java)

    override fun initData() {
    }

    override fun initObservers() {
        viewModel.tabRxFragmentSuccess.observe(this, {
            if (it != null && it.size > 0) {
                LogManager.i(TAG, "onChanged*****tabRxFragmentSuccess")
                projectTabDataSuccess(it)
            } else {
                projectTabDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
            }
        })
        viewModel.tabRxFragmentError.observe(this, {
            if (!TextUtils.isEmpty(it)) {
                LogManager.i(TAG, "onChanged*****tabRxFragmentError")
                projectTabDataError(it)
            }
        })
    }

    override fun initViews() {
    }

    override fun initLoadData() {
        LogManager.i(TAG, "ProjectFragment initLoadData")
        initProjectTabData()
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

    override fun projectTabDataSuccess(success: MutableList<TabBean>) {
        val fragmentList = mutableListOf<Fragment>()
        success.forEach {
            fragmentList.add(SubProjectFragment().apply {
                //想各个fragment传递信息
                val bundle = Bundle()
                bundle.putInt("type", 20)
                bundle.putInt("tabId", it.id)
                bundle.putString("name", it.name)
                arguments = bundle
            })
        }
        fragmentStatePagerAdapter =
            TabFragmentStatePagerAdapter(
                childFragmentManager,
                fragmentList
            )
        mDatabind.mineViewPager2.setAdapter(fragmentStatePagerAdapter)

        //下划线绑定
        val commonNavigator = CommonNavigator(rxAppCompatActivity)
        commonNavigator.adapter = getCommonNavigatorAdapter(success)
        mDatabind.tabLayout.navigator = commonNavigator
        MagicIndicatorManager.bindForViewPager(mDatabind.mineViewPager2, mDatabind.tabLayout)

        hideLoading()
    }

    override fun projectTabDataError(error: String) {
        if (!rxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                18,
                ContextCompat.getColor(rxAppCompatActivity, R.color.white),
                ContextCompat.getColor(rxAppCompatActivity, R.color.color_FFE066FF),
                ScreenManager.dpToPx(rxAppCompatActivity, 40f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                error,
                true
            )
        }
        hideLoading()
    }

    private fun initProjectTabData() {
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            showLoading()
            viewModel.projectTabData()
        } else {
            projectTabDataError(resources.getString(R.string.please_check_the_network_connection))
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