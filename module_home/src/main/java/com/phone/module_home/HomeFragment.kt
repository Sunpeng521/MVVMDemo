package com.phone.module_home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.phone.library_common.base.BaseMvpRxFragment
import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.ResultData
import com.phone.library_common.callback.OnCommonRxPermissionsCallback
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.common.ConstantData
import com.phone.library_common.manager.*
import com.phone.library_common.service.IHomeService
import com.phone.library_common.service.ISquareService
import com.phone.module_home.adapter.HomeAdapter
import com.phone.module_home.manager.AMAPLocationManager
import com.phone.module_home.presenter.HomePresenterImpl
import com.phone.module_home.view.IHomePageView
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = ConstantData.Route.ROUTE_HOME)
class HomeFragment : BaseMvpRxFragment<IBaseView, HomePresenterImpl>(), IHomePageView {

    private val TAG = HomeFragment::class.java.simpleName
    private var layoutOutLayer: FrameLayout? = null
    private var toolbar: Toolbar? = null
    private var tevTitle: TextView? = null
    private var tevRequestPermissionAndStartLocating: TextView? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var rcvData: RecyclerView? = null
    private lateinit var loadView: QMUILoadingView

    private var homeAdapter: HomeAdapter? = null
    private var isRefresh = false

    private var mPermissionsDialog: AlertDialog? = null
    private var amapLocationManager: AMAPLocationManager? = null

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun initLayoutId() = R.layout.home_fragment_home

    override fun initData() {
        isRefresh = true
        amapLocationManager = AMAPLocationManager.instance()
        amapLocationManager?.setOnCommonSingleParamCallback(object :
            OnCommonSingleParamCallback<AMapLocation> {
            override fun onSuccess(success: AMapLocation) {
                SharedPreferencesManager.put("address", success.address)
                LogManager.i(TAG, "address*****" + SharedPreferencesManager.get("address", ""))
            }

            override fun onError(error: String) {
                LogManager.i(TAG, "error*****$error")
            }
        })
    }

    override fun initViews() {
        rootView?.let {
            layoutOutLayer = it.findViewById<View>(R.id.layout_out_layer) as FrameLayout
            toolbar = it.findViewById<View>(R.id.toolbar) as Toolbar
            tevTitle = it.findViewById<View>(R.id.tev_title) as TextView
            tevRequestPermissionAndStartLocating =
                it.findViewById<View>(R.id.tev_request_permission_and_start_locating) as TextView
            refreshLayout = it.findViewById<View>(R.id.refresh_layout) as SmartRefreshLayout
            rcvData = it.findViewById<View>(R.id.rcv_data) as RecyclerView
            loadView = it.findViewById<View>(R.id.load_view) as QMUILoadingView
        }
        tevRequestPermissionAndStartLocating?.setOnClickListener {
            val ISquareService = ARouter.getInstance().build(ConstantData.Route.ROUTE_SQUARE_SERVICE)
                .navigation() as ISquareService
            LogManager.i(
                TAG,
                "squareService.getSquareDataList()******" + ISquareService.mSquareDataList.toString()
            )

            LogManager.i(TAG, "tevRequestPermissions")
            initRxPermissionsRxFragment()
        }
        initAdapter()
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(mRxAppCompatActivity)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rcvData?.layoutManager = linearLayoutManager
        rcvData?.itemAnimator = DefaultItemAnimator()
        homeAdapter = HomeAdapter(mRxAppCompatActivity)
        homeAdapter?.setOnItemViewClickListener { position, view -> //				if (view.getId() == R.id.tev_data) {
            //					//					url = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4"
            //					//					url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4"
            //					url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/detective_conan_japanese.mp4"
            //					//					fileFullname = mFileDTO.getFName()
            //					String[] arr = url.split("\\.")
            //					if (arr != null && arr.length > 0) {
            //						String fileName = ""
            //						StringBuilder stringBuilder = new StringBuilder()
            //						for (int i = 0 i < arr.length - 1 i++) {
            //							stringBuilder.append(arr[i])
            //						}
            //						fileName = stringBuilder.toString()
            //						String suffix = arr[arr.length - 1]
            //
            //						paramMap.clear()
            //						paramMap.put("url", url)
            //						paramMap.put("suffix", suffix)
            //						startActivityCarryParams(ShowVideoActivity.class, paramMap)
            //					}
            //
            //				}
            if (view.id == R.id.ll_root) {
                ARouter.getInstance().build(ConstantData.Route.ROUTE_WEB_VIEW)
                    .withString("loadUrl", homeAdapter?.mJuheNewsBeanList?.get(position)?.url)
                    .navigation()
            }
        }
        rcvData?.adapter = homeAdapter
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initFirstPage()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                isRefresh = true
                initFirstPage()
            }
        })
    }

    override fun initLoadData() {
        initFirstPage()
        LogManager.i(TAG, "FirstPageFragment initLoadData")

        //		startAsyncTask()

//        try {
//            //製造一個不會造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
//            User user = new User2()
//            User3 user3 = (User3) user
//            LogManager.i(TAG, user3.toString())
//        } catch (Exception e) {
//            ExceptionManager.getInstance().throwException(getRxAppCompatActivity(), e)
//        }
    }

    override fun attachPresenter(): HomePresenterImpl {
        return HomePresenterImpl(this)
    }

//	private void startAsyncTask() {
//
//		// This async task is an anonymous class and therefore has a hidden reference to the outer
//		// class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
//		// the activity instance will leak.
//		new AsyncTask<Void, Void, Void>() {
//			@Override
//			protected Void doInBackground(Void... params) {
//				// Do some slow work in background
//				SystemClock.sleep(10000)
//				return null
//			}
//		}.execute()
//
//		Toast.makeText(getRxAppCompatActivity(), "请关闭这个A完成泄露", Toast.LENGTH_SHORT).show()
//	}

    //	private void startAsyncTask() {
    //
    //		// This async task is an anonymous class and therefore has a hidden reference to the outer
    //		// class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
    //		// the activity instance will leak.
    //		new AsyncTask<Void, Void, Void>() {
    //			@Override
    //			protected Void doInBackground(Void... params) {
    //				// Do some slow work in background
    //				SystemClock.sleep(10000)
    //				return null
    //			}
    //		}.execute()
    //
    //		Toast.makeText(getRxAppCompatActivity(), "请关闭这个A完成泄露", Toast.LENGTH_SHORT).show()
    //	}

    override fun showLoading() {
        if (!mRxAppCompatActivity.isFinishing && !loadView.isShown()) {
            loadView.visibility = View.VISIBLE
            loadView.start()
        }
    }

    override fun hideLoading() {
        if (!mRxAppCompatActivity.isFinishing && loadView.isShown()) {
            loadView.stop()
            loadView.setVisibility(View.GONE)
        }
    }

    override fun homePageDataSuccess(success: List<ResultData.JuheNewsBean>) {
        if (!mRxAppCompatActivity.isFinishing) {
            if (isRefresh) {
                homeAdapter?.clearData()
                homeAdapter?.addData(success)
                refreshLayout?.finishRefresh()
            } else {
                homeAdapter?.addData(success)
                refreshLayout?.finishLoadMore()
            }
            LogManager.i(
                TAG,
                "firstPageAdapter?.mJuheNewsBeanList*****" + homeAdapter?.mJuheNewsBeanList.toString()
            )

            val homeService =
                ARouter.getInstance().build(ConstantData.Route.ROUTE_HOME_SERVICE)
                    .navigation() as IHomeService
            homeService.mHomeDataList =
                homeAdapter?.mJuheNewsBeanList ?: mutableListOf()
            hideLoading()
        }
    }

    override fun homePageDataError(error: String) {
        if (!mRxAppCompatActivity.isFinishing) {
//            showToast(error, true)
            showCustomToast(
                ScreenManager.dpToPx(20f), ScreenManager.dpToPx(20f),
                18, resources.getColor(R.color.white),
                resources.getColor(R.color.color_FF198CFF), ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f), error,
                true
            )
            if (isRefresh) {
                refreshLayout?.finishRefresh(false)
            } else {
                refreshLayout?.finishLoadMore(false)
            }
            hideLoading()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 207) {
//            initRxPermissions()
        }
    }

    /**
     * RxFragment里需要的时候直接调用就行了
     */
    private fun initRxPermissionsRxFragment() {
        val rxPermissionsManager = RxPermissionsManager.instance()
        rxPermissionsManager.initRxPermissions2(
            this,
            permissions,
            object : OnCommonRxPermissionsCallback {
                override fun onRxPermissionsAllPass() {
                    //所有的权限都授予
//                //製造一個不會造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
//                User user = new User2()
//                User3 user3 = (User3) user
//                LogManager.i(TAG, user3.toString())
                    val systemId = SharedPreferencesManager.get("systemId", "") as String
                    if (TextUtils.isEmpty(systemId)) {
                        SharedPreferencesManager.put("systemId", SystemManager.getSystemId())
                        LogManager.i(
                            TAG,
                            "isEmpty systemId*****${
                                SharedPreferencesManager.get(
                                    "systemId",
                                    ""
                                ) as String
                            }"
                        )
                    } else {
                        LogManager.i(TAG, "systemId*****$systemId")
                    }
                    amapLocationManager?.startLocation()
                }

                override fun onNotCheckNoMorePromptError() {
                    //至少一个权限未授予且未勾选不再提示
                    showSystemSetupDialog()
                }

                override fun onCheckNoMorePromptError() {
                    //至少一个权限未授予且勾选了不再提示
                    showSystemSetupDialog()
                }
            })
    }

    private fun showSystemSetupDialog() {
        cancelPermissionsDialog()
        if (mPermissionsDialog == null) {
            mPermissionsDialog = AlertDialog.Builder(mRxAppCompatActivity)
                .setTitle("权限设置")
                .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                .setPositiveButton("去授权") { dialog, which ->
                    cancelPermissionsDialog()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        "package", mBaseApplication.packageName, null
                    )
                    intent.data = uri
                    startActivityForResult(intent, 207)
                }
                .create()
        }
        mPermissionsDialog?.setCancelable(false)
        mPermissionsDialog?.setCanceledOnTouchOutside(false)
        mPermissionsDialog?.show()
    }

    /**
     * 关闭对话框
     */
    private fun cancelPermissionsDialog() {
        mPermissionsDialog?.cancel()
        mPermissionsDialog = null
    }

    private fun initFirstPage() {
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
            mBodyParams.clear()
            mBodyParams["type"] = "yule"
            mBodyParams["key"] = "d5cc661633a28f3cf4b1eccff3ee7bae"
            presenter?.homePage(this, mBodyParams)
        } else {
            homePageDataError(resources.getString(R.string.please_check_the_network_connection))
        }
    }

    override fun onDestroyView() {
        layoutOutLayer?.removeAllViews()
        layoutOutLayer = null
        if (amapLocationManager != null) {
            amapLocationManager?.destoryLocation()
        }
        super.onDestroyView()
    }

}