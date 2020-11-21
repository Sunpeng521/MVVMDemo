package com.mobile.rxjava2andretrofit2.first_page.presenter;

import com.alibaba.fastjson.JSONObject;
import com.mobile.rxjava2andretrofit2.MineApplication;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BasePresenter;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.callback.OnCommonSingleParamCallback;
import com.mobile.rxjava2andretrofit2.common.Url;
import com.mobile.rxjava2andretrofit2.first_page.bean.FirstPageDetailsResponse;
import com.mobile.rxjava2andretrofit2.first_page.bean.FirstPageResponse;
import com.mobile.rxjava2andretrofit2.first_page.model.FirstPageModelImpl;
import com.mobile.rxjava2andretrofit2.first_page.presenter.base.IFirstPagePresenter;
import com.mobile.rxjava2andretrofit2.first_page.view.IFirstPageDetailsView;
import com.mobile.rxjava2andretrofit2.first_page.view.IFirstPageView;
import com.mobile.rxjava2andretrofit2.manager.LogManager;
import com.mobile.rxjava2andretrofit2.manager.Okhttp3Manager;
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager;

import java.util.Map;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 17:04
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

public class FirstPagePresenterImpl extends BasePresenter<IBaseView>
        implements IFirstPagePresenter {

    private static final String TAG = "FirstPagePresenterImpl";
    //    private IMineView firstPageView;//P需要与V 交互，所以需要持有V的引用
    private FirstPageModelImpl model;

    public FirstPagePresenterImpl(IBaseView baseView) {
        attachView(baseView);
        model = new FirstPageModelImpl();
    }

    @Override
    public void firstPage(Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IFirstPageView) {
                IFirstPageView firstPageView = (IFirstPageView) baseView;
                firstPageView.showLoading();

                disposable = RetrofitManager.getInstance()
                        .responseString(model.firstPage(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                if (!success.isEmpty()) {
                                    FirstPageResponse response = JSONObject.parseObject(success, FirstPageResponse.class);
                                    firstPageView.firstPageDataSuccess(response.getAns_list());
                                } else {
                                    firstPageView.firstPageDataError(MineApplication.getInstance().getResources().getString(R.string.loading_failed));
                                }
                                firstPageView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                firstPageView.firstPageDataError(error);
                                firstPageView.hideLoading();
                            }
                        });
                disposableList.add(disposable);

//                disposable = model.firstPageData(bodyParams)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<JSONObject>() {
//                            @Override
//                            public void accept(JSONObject jsonObject) throws Exception {
//                                String responseString = jsonObject.toJSONString();
//                                LogManager.i(TAG, "responseString*****" + responseString);
//                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                if (baseResponse.getCode() == 200) {
////                                    FirstPageResponse firstPageResponse = JSON.parse(responseString, FirstPageResponse.class);
//                                    firstPageView.firstPageDataSuccess(baseResponse.getMessage());
//                                } else {
//                                    firstPageView.firstPageDataError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
//                                }
//                                firstPageView.hideLoading();
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
//                                // 异常处理
//                                firstPageView.firstPageDataError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
//                                firstPageView.hideLoading();
//                            }
//                        });
            }
        }
    }

    @Override
    public void firstPageDetails(Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IFirstPageDetailsView) {
                IFirstPageDetailsView firstPageDetailsView = (IFirstPageDetailsView) baseView;
                firstPageDetailsView.showLoading();
                //rxjava2+retrofit2请求（响应速度更快）
                disposable = RetrofitManager.getInstance()
                        .responseString(model.firstPageDetails(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                if (!success.isEmpty()) {
                                    FirstPageDetailsResponse response = JSONObject.parseObject(success, FirstPageDetailsResponse.class);
                                    firstPageDetailsView.firstPageDetailsSuccess(response.getData());
                                } else {
                                    firstPageDetailsView.firstPageDetailsError(MineApplication.getInstance().getResources().getString(R.string.loading_failed));
                                }
                                firstPageDetailsView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                firstPageDetailsView.firstPageDetailsError(error);
                                firstPageDetailsView.hideLoading();
                            }
                        });
                disposableList.add(disposable);

                //okhttp3请求（响应速度稍慢，可改进）
                Okhttp3Manager.getInstance()
                        .postAsyncKeyValuePairsOkhttp3(Url.BASE_URL + Url.FIRST_PAGE_DETAILS_URL,
                                bodyParams,
                                new OnCommonSingleParamCallback<String>() {
                                    @Override
                                    public void onSuccess(String success) {
                                        LogManager.i(TAG, "success2*****" + success);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        LogManager.i(TAG, "error2*****" + error);
                                    }
                                });

            }
        }
    }

}
