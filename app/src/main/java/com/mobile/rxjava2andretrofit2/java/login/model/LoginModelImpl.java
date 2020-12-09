package com.mobile.rxjava2andretrofit2.java.login.model;

import com.mobile.rxjava2andretrofit2.java.login.model.base.ILoginModel;
import com.mobile.rxjava2andretrofit2.java.login.request.LoginRequest;
import com.mobile.rxjava2andretrofit2.java.manager.RetrofitManager;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

public class LoginModelImpl implements ILoginModel {

    private static final String TAG = "LoginModelImpl";

    public LoginModelImpl() {
    }

    @Override
    public Observable<ResponseBody> login(Map<String, String> bodyParams) {
        return RetrofitManager.getInstance().getRetrofit()
                .create(LoginRequest.class)
                .getLoginData(bodyParams);
    }

    @Override
    public Observable<ResponseBody> register(Map<String, String> bodyParams) {
        return RetrofitManager.getInstance().getRetrofit()
                .create(LoginRequest.class)
                .getRegisterData(bodyParams);
    }

}
