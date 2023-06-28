package com.phone.library_common.base

import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.ApiResponse2
import com.phone.library_common.bean.ApiResponse3
import com.phone.library_common.common.ApiException
import com.phone.library_common.manager.LogManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import retrofit2.HttpException
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/7 10:55
 * introduce :
 */

open class BasePresenter<T> {

    private val TAG = "BasePresenter"
    private var modelView: WeakReference<T?>? = null

    protected fun attachView(view: T) {
        modelView = WeakReference(view)
    }

    protected fun obtainView(): T? {
        return if (isAttach()) modelView?.get() else null
    }

    private fun isAttach(): Boolean {
        return modelView != null &&
                modelView?.get() != null
    }

    open fun detachView() {
        if (isAttach()) {
            modelView?.clear()
            modelView = null
        }
    }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    protected suspend fun <T> execute(block: suspend () -> ApiResponse2<T>): ApiResponse2<T> {
        var response: ApiResponse2<T>? = null
        withContext(Dispatchers.IO) {
            runCatching {
                block()
            }.onSuccess {
                response = it
            }.onFailure {
                it.printStackTrace()
                response = ApiResponse2<T>()
                val apiException = getApiException(it)
                response?.error_code = apiException.errorCode
                response?.reason = apiException.errorMessage
                response?.error = apiException
            }.getOrDefault(ApiResponse2<T>())
        }
        LogManager.i(TAG, "launch thread name*****${Thread.currentThread().name}")

        return response!!
    }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    protected suspend fun <T> execute2(block: suspend () -> ApiResponse3<T>): ApiResponse3<T> {
        var response: ApiResponse3<T>? = null
        withContext(Dispatchers.IO) {
            runCatching {
                block()
            }.onSuccess {
                response = it
            }.onFailure {
                it.printStackTrace()
                response = ApiResponse3<T>()
                val apiException = getApiException(it)
                response?.error_code = apiException.errorCode
                response?.reason = apiException.errorMessage
                response?.error = apiException
            }.getOrDefault(ApiResponse3<T>())
        }
        LogManager.i(TAG, "launch thread name*****${Thread.currentThread().name}")

        return response!!
    }

    /**
     * 捕获异常信息
     */
    private fun getApiException(e: Throwable): ApiException {
        return when (e) {
            is UnknownHostException -> {
                ApiException("网络异常", -100)
            }

            is JSONException -> {//|| e is JsonParseException
                ApiException("数据异常", -100)
            }

            is SocketTimeoutException -> {
                ApiException("连接超时", -100)
            }

            is ConnectException -> {
                ApiException("连接错误", -100)
            }

            is HttpException -> {
                ApiException("http code ${e.code()}", -100)
            }

            is ApiException -> {
                e
            }
            /**
             * 如果协程还在运行，个别机型退出当前界面时，viewModel会通过抛出CancellationException，
             * 强行结束协程，与java中InterruptException类似，所以不必理会,只需将toast隐藏即可
             */
            is CancellationException -> {
                ApiException("", -10)
            }

            else -> {
                ApiException("未知错误", -100)
            }
        }
    }

}