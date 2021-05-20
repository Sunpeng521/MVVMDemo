package com.mobile.common_library.interceptor;

import android.text.TextUtils;

import com.mobile.common_library.BaseApplication;
import com.mobile.common_library.manager.LogManager;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 13:55
 * introduce : 添加Cookies拦截器
 */

public class AddCookiesInterceptor implements Interceptor {
	
	private static final String          TAG = "AddCookiesInterceptor";
	private              BaseApplication baseApplication;
	
	public AddCookiesInterceptor(BaseApplication baseApplication) {
		super();
		this.baseApplication = baseApplication;
	}
	
	@NonNull
	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		
		Request.Builder builder = chain.request().newBuilder();
		
		//添加accessToken
		String accessToken = baseApplication.getAccessToken();
		if (!TextUtils.isEmpty(accessToken)) {
			builder.addHeader("appToken", accessToken);
			LogManager.i(TAG, "appToken*****" + accessToken);
		}
		
		//        //添加cookie
		//        String cookie = mineApplication.getCookie();
		//        if (cookie != null && !"".equals(cookie)) {
		//            builder.addHeader("cookie", cookie);
		//            LogManager.i(TAG, "cookie*****" + cookie);
		//        }
		
		//        //添加用户代理
		//        builder.removeHeader("User-Agent")
		//                .addHeader("User-Agent",
		//                SystemManager.getUserAgent(mineApplication.getApplicationContext())).build();
		
		//		builder.removeHeader("User-Agent")//移除旧的
		//			.addHeader("User-Agent", WebSettings.getDefaultUserAgent(baseApplication));//添加真正的头部
		return chain.proceed(builder.build());
	}
}
