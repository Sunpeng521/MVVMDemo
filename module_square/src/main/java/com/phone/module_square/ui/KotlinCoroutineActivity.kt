package com.phone.module_square.ui

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.base.BaseBindingRxAppActivity
import com.phone.library_common.common.ConstantData
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.module_square.R
import com.phone.module_square.databinding.SquareActivityKotlinCoroutineBinding
import kotlinx.coroutines.*

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce : 多种协程的创建方式及其使用方式
 */

@Route(path = ConstantData.Route.ROUTE_KOTLIN_COROUTINE)
class KotlinCoroutineActivity : BaseBindingRxAppActivity<SquareActivityKotlinCoroutineBinding>() {

    companion object {
        private val TAG = KotlinCoroutineActivity::class.java.simpleName
    }

    var mJob: Job? = null
    var mCoroutineScope: CoroutineScope? = null
    var mMainScope: CoroutineScope? = null

    override fun initLayoutId() = R.layout.square_activity_kotlin_coroutine

    override fun initData() {

    }

    override fun initViews() {
        setToolbar(true)
        mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.color_80000000))
        mDatabind.layoutBack.setOnClickListener { v -> finish() }

        mDatabind.tevStartRunBlocking.setOnClickListener {
            startRunBlocking()
        }
        mDatabind.tevStartGlobalScope.setOnClickListener {
            startGlobalScope()
        }
        mDatabind.tevStartCoroutineScope.setOnClickListener {
            startCoroutineScope()
        }
        mDatabind.tevStartMainScope.setOnClickListener {
            startMainScope()
        }
        mDatabind.tevStartLifecycleScope.setOnClickListener {
            startLifecycleScope()
        }
    }

    private fun startRunBlocking() {
        //方法一：使用 runBlocking 顶层函数
        //启动一个新协程，并阻塞当前线程，直到其内部所有逻辑及子协程逻辑全部执行完成。
        //不建议使用
        //开启GlobalScope.launch这种协程之后就是在MAIN线程执行了
        runBlocking {
            LogManager.i(TAG, "runBlocking thread name*****${Thread.currentThread().name}")

            requestUserInfo()
            downloadFile()
        }
    }

    private fun startGlobalScope() {
        //方法二：使用GlobalScope 单例对象直接调用launch/async开启协程
        //在应用范围内启动一个新协程，协程的生命周期与应用程序一致。
        //由于这样启动的协程存在启动协程的组件已被销毁但协程还存在的情况，极限情况下可能导致资源耗尽，
        //所以Activity 销毁的时候记得要取消掉，避免内存泄漏
        //不建议使用，尤其是在客户端这种需要频繁创建销毁组件的场景。
        //开启GlobalScope.launch{} 或GlobalScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是子线程）。
        mJob?.cancel()
        mJob = GlobalScope.launch(Dispatchers.Main) {
            LogManager.i(TAG, "GlobalScope thread name*****${Thread.currentThread().name}")

            showLoading()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            requestUserInfo()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            downloadFile()
            hideLoading()
        }


//        mJob = GlobalScope.async(Dispatchers.Main) {
//            LogManager.i(TAG, "GlobalScope thread name*****${Thread.currentThread().name}")
//
//            showLoading()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            requestUserInfo()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            downloadFile()
//            hideLoading()
//        }
    }

    private fun startCoroutineScope() {
        //方法三：创建一个CoroutineScope 对象，创建的时候可以指定运行线程（默认运行在子线程）
        //Activity 销毁的时候记得要取消掉，避免内存泄漏
        //开启mCoroutineScope?.launch{} 或mCoroutineScope?.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定是创建的时候的线程）。
        mCoroutineScope = CoroutineScope(Dispatchers.Main)
        mCoroutineScope?.launch {
            LogManager.i(TAG, "mCoroutineScope thread name*****${Thread.currentThread().name}")
            showLoading()
            calculatePi()
            hideLoading()
        }


//        mCoroutineScope?.async {
//            LogManager.i(TAG, "mCoroutineScope2 async thread name*****${Thread.currentThread().name}")
//            requestUserInfo()
//        }
//        mCoroutineScope?.async {
//            LogManager.i(TAG, "mCoroutineScope3 async thread name*****${Thread.currentThread().name}")
//            videoDecoding()
//        }
    }

    private fun startMainScope() {
        //方法四：创建一个MainScope 对象，默认运行在UI线程
        //Activity 销毁的时候记得要取消掉，避免内存泄漏
        //开启GlobalScope.launch{} 或GlobalScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定是创建的时候的线程）。
        mMainScope = MainScope()
        mMainScope?.launch {//开启MainScope这种协程之后就是在MAIN线程执行了
            LogManager.i(TAG, "mainScope launch thread name*****${Thread.currentThread().name}")

            showLoading()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            requestUserInfo()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            videoDecoding()
            hideLoading()
        }

//        mMainScope?.async {
//            LogManager.i(TAG, "mainScope async thread name*****${Thread.currentThread().name}")
//
//            showLoading()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            requestUserInfo()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            videoDecoding()
//            hideLoading()
//        }
    }

    private fun startLifecycleScope() {
        //方法五：此种创建方式只能在Activity/Fragment内部创建，默认运行在UI线程，它自动绑定Activity生命周期，不用处理内容泄漏问题
        //开启lifecycleScope.launch{}或lifecycleScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定是创建的时候的线程）。
        lifecycleScope.launch {
            LogManager.i(TAG, "lifecycleScope launch thread name*****${Thread.currentThread().name}")

            showLoading()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            requestUserInfo()
            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
            videoDecoding()
            hideLoading()
        }

//        lifecycleScope.async {
//            LogManager.i(TAG, "lifecycleScope async thread name*****${Thread.currentThread().name}")
//
//            showLoading()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            requestUserInfo()
//            //进入挂起的函数后UI线程会进入阻塞状态，挂起函数执行完毕之后，UI线程再进入唤醒状态，然后往下执行
//            videoDecoding()
//            hideLoading()
//        }
    }

    override fun initLoadData() {

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

    /**
     * IO密集型协程：模拟请求用户信息（需要时间比较短的）
     */
    private suspend fun requestUserInfo() {
        LogManager.i(TAG, "start requestUserInfo")
        withContext(Dispatchers.IO) {
            delay(2 * 1000)
        }
        LogManager.i(TAG, "end requestUserInfo")
    }

    /**
     * IO密集型协程：模拟下载文件
     */
    private suspend fun downloadFile() {
        LogManager.i(TAG, "start downloadFile")
        withContext(Dispatchers.IO) {
//            //这个是真正的模拟下载文件需要的时长
//            delay(60 * 1000)

            //这里只是想早点看到效果，所以减少了时长
            delay(5 * 1000)
        }
        LogManager.i(TAG, "end downloadFile")
    }

    /**
     * CPU密集型协程：模拟计算圆周率（需要时间比较长的）
     */
    private suspend fun calculatePi() {
        LogManager.i(TAG, "start calculatePi")
        withContext(Dispatchers.Default) {
//            //这个是真正的模拟计算圆周率需要的时长
//            delay(5 * 60 * 1000)

            //这里只是想早点看到效果，所以减少了时长
            delay(10 * 1000)
        }
        LogManager.i(TAG, "end calculatePi")
    }

    /**
     * CPU密集型协程：模拟视频解码（需要时间比较长的）
     */
    private suspend fun videoDecoding() {
        LogManager.i(TAG, "start videoDecoding")
        withContext(Dispatchers.Default) {
//            //这个是真正的模拟视频解码需要的时长
//            delay(20 * 60 * 1000)

            //这里只是想早点看到效果，所以减少了时长
            delay(10 * 1000)
        }
        LogManager.i(TAG, "end videoDecoding")
    }

    override fun onDestroy() {
        //这几个任务要在Activity 销毁的时候取消，避免内存泄漏
        mJob?.cancel()
        mCoroutineScope?.cancel()
        mMainScope?.cancel()
        super.onDestroy()
    }

}