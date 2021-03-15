package com.mobile.rxjava2andretrofit2.first_page.model;

import com.mobile.rxjava2andretrofit2.manager.RetrofitManager;
import com.mobile.rxjava2andretrofit2.first_page.request.FirstPageRequest;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

public class FirstPageModelImpl implements IFirstPageModel {

    private static final String TAG = "FirstPageModelImpl";

    public FirstPageModelImpl() {
    }

    @Override
    public Observable<ResponseBody> firstPage(Map<String, String> bodyParams) {
        return RetrofitManager.getInstance().getRetrofit()
                .create(FirstPageRequest.class)
                .getFirstPage(bodyParams);
    }

    @Override
    public Observable<ResponseBody> firstPageDetails(Map<String, String> bodyParams) {
        return RetrofitManager.getInstance().getRetrofit()
                .create(FirstPageRequest.class)
                .getFirstPageDetails(bodyParams);
    }

//    @Override
//    public Observable<FirstPageResponse.QuestionBean> firstPageData(Map<String, String> bodyParams) {
//        return RetrofitManager.getInstance().getRetrofit()
//                .create(FirstPageRequest.class)
//                .getFirstPageData(bodyParams);
//    }

}
