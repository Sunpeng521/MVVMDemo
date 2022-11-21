package com.phone.project_module

import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseMvvmRxFragment
import com.phone.common_library.callback.OnItemViewClickListener
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.project_module.adapter.ProjectAdapter
import com.phone.project_module.bean.DataX
import com.phone.project_module.databinding.FragmentProjectBinding
import com.phone.project_module.ui.EventScheduleActivity
import com.phone.project_module.view.IProjectChildView
import com.phone.project_module.view_model.ProjectViewModelImpl
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/project_module/project")
class ProjectFragment : BaseMvvmRxFragment<ProjectViewModelImpl, FragmentProjectBinding>(),
    IProjectChildView {

    companion object {
        private val TAG: String = ProjectFragment::class.java.simpleName
    }

    private val projectAdapter by lazy { ProjectAdapter(rxAppCompatActivity) }
    private var isRefresh: Boolean = true
    private var currentPage: Int = 1
    private var dataxSuccessObserver: Observer<MutableList<DataX>>? = null;
    private var dataxErrorObserver: Observer<String>? = null;

    override fun initLayoutId() = R.layout.fragment_project

    override fun initViewModel() =
        ViewModelProvider(rxAppCompatActivity).get(ProjectViewModelImpl::class.java)

    override fun initData() {
//        mDatabind.setVariable()
    }

    override fun initObservers() {
        dataxSuccessObserver = object : Observer<MutableList<DataX>> {
            override fun onChanged(t: MutableList<DataX>?) {
                if (t != null && t.size > 0) {
                    LogManager.i(TAG, "onChanged*****dataxSuccessObserver")
//                    LogManager.i(TAG, "onChanged*****${t.toString()}")
                    projectDataSuccess(t)
                    hideLoading()
                } else {
                    projectDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
                    hideLoading()
                }
            }

        }

        dataxErrorObserver = object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!TextUtils.isEmpty(t)) {
                    LogManager.i(TAG, "onChanged*****dataxErrorObserver")
                    projectDataError(t!!)
                }
            }

        }

        viewModel.dataxRxFragmentSuccess.observe(this, dataxSuccessObserver!!)
        viewModel.dataxRxFragmentError.observe(this, dataxErrorObserver!!)
    }

    override fun initViews() {
        projectAdapter.apply {
            setRcvOnItemViewClickListener(object : OnItemViewClickListener {

                override fun onItemClickListener(position: Int, view: View?) {
                    //                startActivity(VideoViewActivity::class.java)
                    startActivity(EventScheduleActivity::class.java)
                }
            })
        }
        mDatabind.rcvData.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = projectAdapter
        }

        mDatabind.refreshLayout.apply {
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    LogManager.i(TAG, "onLoadMore")
                    isRefresh = false
                    initProject("$currentPage")
                }

                override fun onRefresh(refreshLayout: RefreshLayout) {
                    LogManager.i(TAG, "onRefresh")
                    isRefresh = true
                    currentPage = 1;
                    initProject("$currentPage")
                }
            })
        }
    }

    override fun initLoadData() {
        LogManager.i(TAG, "initLoadData")
        mDatabind.refreshLayout.autoRefresh()

        LogManager.i(TAG, "ProjectFragment initLoadData")
    }

    override fun showLoading() {
        if (!rxAppCompatActivity.isFinishing()) {
            if (!mDatabind.loadView.isShown()) {
                mDatabind.loadView.setVisibility(View.VISIBLE)
                mDatabind.loadView.start()
            }
        }
    }

    override fun hideLoading() {
        if (!rxAppCompatActivity.isFinishing()) {
            if (mDatabind.loadView.isShown()) {
                mDatabind.loadView.stop()
                mDatabind.loadView.setVisibility(View.GONE)
            }
        }
    }

    override fun projectDataSuccess(success: MutableList<DataX>) {
        if (!rxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                projectAdapter.clearData();
                projectAdapter.addData(success)
                mDatabind.refreshLayout.finishRefresh()
            } else {
                projectAdapter.addData(success)
                mDatabind.refreshLayout.finishLoadMore()
            }
            currentPage++;
        }
    }

    override fun projectDataError(error: String) {
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

            if (isRefresh) {
                mDatabind.refreshLayout.finishRefresh(false)
            } else {
                mDatabind.refreshLayout.finishLoadMore(false)
            }
        }
    }

    private fun initProject(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            viewModel.projectData(this, currentPage)
        } else {
            projectDataError(BaseApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }
    }

}