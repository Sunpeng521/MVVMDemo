package com.phone.mine_module.presenter

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BasePresenter
import com.phone.common_library.base.IBaseView
import com.phone.common_library.callback.OnCommonSingleParamCallback
import com.phone.common_library.manager.GsonManager
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.mine_module.R
import com.phone.mine_module.bean.MineResponse
import com.phone.mine_module.model.MineModelImpl
import com.phone.mine_module.view.IMineView
import com.phone.mine_module.view.IUserDataView

class MinePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IMinePresenter {

    private val TAG: String = "MinePresenterImpl"

    //    private IResourceChildView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model: MineModelImpl = MineModelImpl();

    init {
        attachView(baseView)
    }

    override fun mineData(fragment: Fragment, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                    .responseStringAutoDispose(
                        fragment,
                        model.mineData(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "mineData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
                                    val response = GsonManager.getInstance()
                                        .convert(success, MineResponse::class.java)

                                    if (response.error_code == 0) {
                                        baseView.mineDataSuccess(response.result.data)
                                    } else {
                                        baseView.mineDataError(response.reason)
                                    }
                                } else {
                                    baseView.mineDataError(
                                        BaseApplication.getInstance().resources.getString(
                                            R.string.loading_failed
                                        )
                                    )
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.mineDataError(error)
                                baseView.hideLoading()
                            }
                        })
//                compositeDisposable.add(disposable)
            }
        }
    }

    override fun mineData(appCompatActivity: AppCompatActivity, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                    .responseStringAutoDispose(
                        appCompatActivity,
                        model.mineData(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "mineData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
                                    val response = GsonManager.getInstance()
                                        .convert(success, MineResponse::class.java)
//                                    String jsonStr = GsonManager . getInstance ().toJson(response);
                                    if (response.error_code == 0) {
                                        baseView.mineDataSuccess(response.result.data)
                                    } else {
                                        baseView.mineDataError(response.reason)
                                    }
                                } else {
                                    baseView.mineDataError(
                                        BaseApplication.getInstance().resources.getString(
                                            R.string.loading_failed
                                        )
                                    )
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.mineDataError(error)
                                baseView.hideLoading()
                            }
                        })
//                compositeDisposable.add(disposable)
            }
        }
    }

    override fun userData(appCompatActivity: AppCompatActivity, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IUserDataView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                    .responseStringAutoDispose(
                        appCompatActivity,
                        model.userData(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "userData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
//                                    val response = GsonManager.getInstance().convert(success, MineResponse::class.java)

//                                    if (response.ans_list != null && response.ans_list.size > 0) {
//                                        baseView.mineDataSuccess(response.ans_list)
//                                    } else {
//                                        baseView.mineDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
//                                    }
                                } else {
                                    baseView.userDataError(
                                        BaseApplication.getInstance().resources.getString(
                                            R.string.loading_failed
                                        )
                                    )
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.userDataError(error)
                                baseView.hideLoading()
                            }
                        })
//                compositeDisposable.add(disposable)
            }
        }
    }

    override fun userData(
        appCompatActivity: AppCompatActivity,
        accessToken: String,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IUserDataView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                    .responseStringAutoDispose(
                        appCompatActivity,
                        model.userData(accessToken, bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "userData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
//                                    val response = GsonManager.getInstance().convert(success, MineResponse::class.java)

//                                    if (response.ans_list != null && response.ans_list.size > 0) {
//                                        baseView.mineDataSuccess(response.ans_list)
//                                    } else {
//                                        baseView.mineDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
//                                    }
                                } else {
                                    baseView.userDataError(
                                        BaseApplication.getInstance().resources.getString(
                                            R.string.loading_failed
                                        )
                                    )
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.userDataError(error)
                                baseView.hideLoading()
                            }
                        })
//                compositeDisposable.add(disposable)
            }
        }
    }
}