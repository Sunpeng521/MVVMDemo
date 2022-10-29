package com.phone.mine_module

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.common_library.base.BaseMvpRxFragment
import com.phone.common_library.base.IBaseView
import com.phone.common_library.callback.OnItemViewClickListener
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.common_library.ui.NewsDetailActivity
import com.phone.mine_module.adapter.MineAdapter
import com.phone.mine_module.bean.Data
import com.phone.mine_module.presenter.MinePresenterImpl
import com.phone.mine_module.ui.ParamsTransferChangeProblemActivity
import com.phone.mine_module.ui.ThreadPoolActivity
import com.phone.mine_module.ui.UserDataActivity
import com.phone.mine_module.view.IMineView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
@Route(path = "/mine_module/mine")
class MineFragment : BaseMvpRxFragment<IBaseView, MinePresenterImpl>(), IMineView {

    companion object {
        private val TAG: String = MineFragment::class.java.name
    }

//    private var mainActivity: MainActivity? = null

    private var juheNewsBeanList: MutableList<Data> = mutableListOf()
    private var mineAdapter: MineAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isRefresh: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState)
        return rootView
    }

    override fun initLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initData() {
//        mainActivity = rxAppCompatActivity as MainActivity
    }

    override fun initViews() {
        tev_title.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
//                initMine()
                startActivity(UserDataActivity::class.java)
            }
        })
        tev_logout.setOnClickListener {
            baseApplication.isLogin = false
            ARouter.getInstance().build("/main_module/login").navigation()
        }
        tev_thread_pool.setOnClickListener {
            startActivity(ThreadPoolActivity::class.java)
        }
        tev_params_transfer_change_problem.setOnClickListener {
            startActivity(ParamsTransferChangeProblemActivity::class.java)
        }

        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(rxAppCompatActivity)
        linearLayoutManager!!.setOrientation(RecyclerView.VERTICAL)
        rcv_data.layoutManager = (linearLayoutManager)
        rcv_data.itemAnimator = DefaultItemAnimator()

        mineAdapter = MineAdapter(rxAppCompatActivity!!)
        mineAdapter!!.setRcvOnItemViewClickListener(object : OnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
//                bodyParams.clear()
//                bodyParams["max_behot_time"] = "1000"
//                startActivityCarryParams(MineDetailsActivity::class.java, bodyParams)

//                //Jump with parameters
//                ARouter.getInstance().build("/mine_module/ui/mine_details")
//                        .withString("max_behot_time", (System.currentTimeMillis() / 1000).toString())
//                        .navigation()

                if (view?.id == R.id.ll_root) {
                    val intent = Intent(rxAppCompatActivity, NewsDetailActivity::class.java)
                    intent.putExtra("detailUrl", juheNewsBeanList.get(position).url)
                    startActivity(intent)
                }
            }
        })
        rcv_data.setAdapter(mineAdapter)
        mineAdapter!!.clearData()
        mineAdapter!!.addAllData(juheNewsBeanList)

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refresh_layout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initMine()
            }

            override fun onRefresh(refresh_layout: RefreshLayout) {
                isRefresh = true
                initMine()
            }
        })
    }

    override fun initLoadData() {
        refresh_layout.autoRefresh()

        LogManager.i(TAG, "MineFragment initLoadData")
    }

    override fun attachPresenter(): MinePresenterImpl {
        return MinePresenterImpl(this)
    }

    override fun showLoading() {
        if (load_view != null && !load_view.isShown()) {
            load_view.setVisibility(View.VISIBLE)
            load_view.start()
        }
    }

    override fun hideLoading() {
        if (load_view != null && load_view.isShown()) {
            load_view.stop()
            load_view.setVisibility(View.GONE)
        }
    }

    override fun mineDataSuccess(success: List<Data>) {
        if (!rxAppCompatActivity!!.isFinishing()) {
            if (isRefresh) {
                juheNewsBeanList.clear()
                juheNewsBeanList.addAll(success)
                mineAdapter!!.clearData();
                mineAdapter!!.addAllData(juheNewsBeanList)
                refresh_layout.finishRefresh()
            } else {
                juheNewsBeanList.addAll(success)
                mineAdapter!!.clearData();
                mineAdapter!!.addAllData(juheNewsBeanList)
                refresh_layout.finishLoadMore()
            }
        }
    }

    override fun mineDataError(error: String) {
        if (!rxAppCompatActivity!!.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                18,
                ContextCompat.getColor(rxAppCompatActivity!!, R.color.white),
                ContextCompat.getColor(rxAppCompatActivity!!, R.color.color_FFE066FF),
                ScreenManager.dpToPx(rxAppCompatActivity, 40f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                error,
                true
            )

            if (isRefresh) {
                refresh_layout.finishRefresh(false)
            } else {
                refresh_layout.finishLoadMore(false)
            }
        }
    }

    private fun initMine() {
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            bodyParams.clear()

            bodyParams["type"] = "keji"
            bodyParams["key"] = "d5cc661633a28f3cf4b1eccff3ee7bae"
            presenter.mineDataRxFragment(rxFragment, bodyParams)
        } else {
            showToast(resources.getString(R.string.please_check_the_network_connection), true)
            if (isRefresh) {
                refresh_layout.finishRefresh()
            } else {
                refresh_layout.finishLoadMore()
            }
        }
    }

    override fun onDestroyView() {
        layout_out_layer.removeAllViews()
        rootView = null
        super.onDestroyView()
    }

}