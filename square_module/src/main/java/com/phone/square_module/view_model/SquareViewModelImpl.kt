package com.phone.square_module.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseViewModel
import com.phone.common_library.bean.DataX
import com.phone.common_library.bean.SquareBean
import com.phone.common_library.callback.OnCommonSingleParamCallback
import com.phone.common_library.manager.GsonManager
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.SingleLiveData
import com.phone.square_module.model.SquareModelImpl
import com.phone.square_module.R
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SquareViewModelImpl() : BaseViewModel(), ISquareViewModel {

    companion object {
        private val TAG: String = SquareViewModelImpl::class.java.simpleName
    }

    private var model: SquareModelImpl = SquareModelImpl()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragmentSuccess: SingleLiveData<List<DataX>> = SingleLiveData()
    val dataxRxFragmentError: SingleLiveData<String> = SingleLiveData()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxAppCompatActivitySuccess: SingleLiveData<List<DataX>> = SingleLiveData()
    val dataxRxAppCompatActivityError: SingleLiveData<String> = SingleLiveData()

    override fun squareDataRxFragment(rxFragment: RxFragment, currentPage: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success = model.squareData2(currentPage).execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "success*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val response: SquareBean =
                            GsonManager.getInstance()
                                .convert(success, SquareBean::class.java)
                        if (response.data?.datas != null && response.data!!.datas!!.size > 0) {
//                                LogManager.i(TAG, "response*****${response.toString()}")

                            dataxRxFragmentSuccess.value = response.data!!.datas
                        } else {
                            dataxRxFragmentError.value =
                                BaseApplication.getInstance().resources.getString(R.string.no_data_available)
                        }
                    } else {
                        dataxRxFragmentError.value =
                            BaseApplication.getInstance().resources.getString(R.string.loading_failed)
                    }
                }

//                model.squareData2(currentPage).enqueue(object : Callback<ResponseBody> {
//                    override fun onResponse(
//                        call: Call<ResponseBody>,
//                        response: Response<ResponseBody>
//                    ) {
//                        launch(Dispatchers.Main) {
//                            val success = response.body()?.string()
//                            LogManager.i(TAG, "success*****$success")
//                            if (!TextUtils.isEmpty(success)) {
//                                val response: SquareBean =
//                                    GsonManager.getInstance()
//                                        .convert(success, SquareBean::class.java)
//                                if (response.data?.datas != null && response.data!!.datas!!.size > 0) {
////                                LogManager.i(TAG, "response*****${response.toString()}")
//
//                                    dataxRxFragmentSuccess.value = response.data!!.datas
//                                } else {
//                                    dataxRxFragmentError.value =
//                                        BaseApplication.getInstance().resources.getString(R.string.no_data_available)
//                                }
//                            } else {
//                                dataxRxFragmentError.value =
//                                    BaseApplication.getInstance().resources.getString(R.string.loading_failed)
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        launch(Dispatchers.Main) {
//                            val error = t.toString()
//                            LogManager.i(TAG, "error*****$error")
//                            dataxRxFragmentError.value = error
//                        }
//                    }
//
//                })
            }
        }

//            RetrofitManager.getInstance()
//                .responseStringRxFragmentBindToLifecycle(rxFragment,
//                    model.squareData(currentPage),
//                    object : OnCommonSingleParamCallback<String> {
//                        override fun onSuccess(success: String) {
//                            LogManager.i(TAG, "success*****$success")
//                            if (!TextUtils.isEmpty(success)) {
//                                val response: SquareBean =
//                                    GsonManager.getInstance().convert(success, SquareBean::class.java)
//                                if (response.data?.datas != null && response.data!!.datas!!.size > 0) {
////                                LogManager.i(TAG, "response*****${response.toString()}")
//
//
//                                    dataxRxFragmentSuccess.value = response.data!!.datas
//                                } else {
//                                    dataxRxFragmentError.value =
//                                        BaseApplication.getInstance().resources.getString(R.string.no_data_available)
//                                }
//                            } else {
//                                dataxRxFragmentError.value =
//                                    BaseApplication.getInstance().resources.getString(R.string.loading_failed)
//                            }
//                        }
//
//                        override fun onError(error: String) {
//                            LogManager.i(TAG, "error*****$error")
//                            dataxRxFragmentError.value = error
//                        }
//                    })
//        }
    }

    override fun squareDataRxAppCompatActivity(
        rxAppCompatActivity: RxAppCompatActivity,
        currentPage: String
    ) {
        RetrofitManager.getInstance()
            .responseStringRxAppActivityBindUntilEvent(rxAppCompatActivity,
                model.squareData(currentPage),
                object : OnCommonSingleParamCallback<String> {
                    override fun onSuccess(success: String) {
                        LogManager.i(TAG, "success*****$success")
                        if (!TextUtils.isEmpty(success)) {
                            val response: SquareBean =
                                GsonManager.getInstance().convert(success, SquareBean::class.java)
                            if (response.data?.datas != null && response.data!!.datas!!.size > 0) {
//                                LogManager.i(TAG, "response*****${response.toString()}")


                                dataxRxAppCompatActivitySuccess.value = response.data!!.datas
                            } else {
                                dataxRxAppCompatActivityError.value =
                                    BaseApplication.getInstance().resources.getString(R.string.no_data_available)
                            }
                        } else {
                            dataxRxAppCompatActivityError.value =
                                BaseApplication.getInstance().resources.getString(R.string.loading_failed)
                        }
                    }

                    override fun onError(error: String) {
                        LogManager.i(TAG, "error*****$error")
                        dataxRxAppCompatActivityError.value = error
                    }
                })
    }

}