package com.phone.common_library.base

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.phone.common_library.BaseApplication
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

abstract class BaseMvpRxFragment<V, T : BasePresenter<V>> : RxFragment() {

    private val TAG = BaseMvpRxFragment::class.java.simpleName
    protected var presenter: T? = null

    protected var url: String? = null
    protected var bodyParams = HashMap<String, String>()
    protected lateinit var rxAppCompatActivity: RxAppCompatActivity
    protected lateinit var baseApplication: BaseApplication

    protected var rootView: View? = null
    protected lateinit var rxFragment: RxFragment
//    private boolean isFirstLoad = true;

    //    private boolean isFirstLoad = true;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //        if (rootView == null) {
        //            rootView = inflater.inflate(initLayoutId(), container, false);
        //        } else {
        //            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
        //            if (viewGroup != null) {
        //                viewGroup.removeView(rootView);
        //            }
        //        }
        rxFragment = this
        rootView = inflater.inflate(initLayoutId(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rxAppCompatActivity = activity as RxAppCompatActivity
        baseApplication = rxAppCompatActivity.application as BaseApplication
        initData()
        presenter = attachPresenter()
        initViews()
        initLoadData()

//        RxPermissionsManager rxPermissionsManager = RxPermissionsManager.getInstance(this);
//        rxPermissionsManager.initRxPermissionsRxFragment(new OnCommonRxPermissionsCallback() {
//            @Override
//            public void onRxPermissionsAllPass() {
//                CrashHandlerManager crashHandlerManager = CrashHandlerManager.getInstance(rxAppCompatActivity);
//                crashHandlerManager.sendPreviousReportsToServer();
//                crashHandlerManager.init();
//            }
//
//            @Override
//            public void onNotCheckNoMorePromptError() {
//
//            }
//
//            @Override
//            public void onCheckNoMorePromptError() {
//
//            }
//        });
    }

    protected abstract fun initLayoutId(): Int

    protected abstract fun initData()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

    /**
     * 适配为不同的view 装载不同的presenter
     *
     * @return
     */
    protected abstract fun attachPresenter(): T

    protected fun showToast(message: String?, isLongToast: Boolean) {
        //        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (!rxAppCompatActivity.isFinishing) {
            val toast: Toast
            val duration: Int
            duration = if (isLongToast) {
                Toast.LENGTH_LONG
            } else {
                Toast.LENGTH_SHORT
            }
            toast = Toast.makeText(rxAppCompatActivity, message, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    protected fun showCustomToast(
        left: Int, right: Int,
        textSize: Int, textColor: Int,
        bgColor: Int, height: Int,
        roundRadius: Int, message: String?,
        isLongToast: Boolean
    ) {
        val frameLayout = FrameLayout(rxAppCompatActivity)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        frameLayout.layoutParams = layoutParams
        val textView = TextView(rxAppCompatActivity)
        val layoutParams1 =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height)
        textView.layoutParams = layoutParams1
        textView.setPadding(left, 0, right, 0)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        textView.setTextColor(textColor)
        textView.gravity = Gravity.CENTER
        textView.includeFontPadding = false
        val gradientDrawable = GradientDrawable() //创建drawable
        gradientDrawable.setColor(bgColor)
        gradientDrawable.cornerRadius = roundRadius.toFloat()
        textView.background = gradientDrawable
        textView.text = message
        frameLayout.addView(textView)
        val toast = Toast(rxAppCompatActivity)
        toast.view = frameLayout
        if (isLongToast) {
            toast.duration = Toast.LENGTH_LONG
        } else {
            toast.duration = Toast.LENGTH_SHORT
        }
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread.id == Thread.currentThread().id
    }

    protected fun startActivity(cls: Class<*>?) {
        val intent = Intent(rxAppCompatActivity, cls)
        startActivity(intent)
    }

    protected fun startActivityCarryParams(cls: Class<*>?, params: Map<String?, String?>?) {
        val intent = Intent(rxAppCompatActivity, cls)
        val bundle = Bundle()
        if (params != null && params.size > 0) {
            for (key in params.keys) {
                if (params[key] != null) { //如果参数不是null，才把参数传给后台
                    bundle.putString(key, params[key])
                }
            }
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    protected fun startActivityForResult(cls: Class<*>?, requestCode: Int) {
        val intent = Intent(rxAppCompatActivity, cls)
        startActivityForResult(intent, requestCode)
    }

    protected fun startActivityForResultCarryParams(
        cls: Class<*>?,
        params: Map<String?, String?>?,
        requestCode: Int
    ) {
        val intent = Intent(rxAppCompatActivity, cls)
        val bundle = Bundle()
        if (params != null && params.size > 0) {
            for (key in params.keys) {
                if (params[key] != null) { //如果参数不是null，才把参数传给后台
                    bundle.putString(key, params[key])
                }
            }
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    protected fun detachPresenter() {
        presenter?.detachView()
    }

    override fun onDestroyView() {
        detachPresenter()
        bodyParams.clear()
        rootView?.let {
            rootView = null
        }
        super.onDestroyView()
    }

}
