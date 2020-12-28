package com.mobile.rxjava2andretrofit2.main;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BaseMvpAppActivity;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.custom_view.LazyViewPager;
import com.mobile.rxjava2andretrofit2.custom_view.MineLazyViewPager;
import com.mobile.rxjava2andretrofit2.first_page.FirstPageFragment;
import com.mobile.rxjava2andretrofit2.kotlin.mine.MineFragment;
import com.mobile.rxjava2andretrofit2.kotlin.resources_show.ResourceCenterFragment;
import com.mobile.rxjava2andretrofit2.main.adapter.TabFragmentPagerAdapter;
import com.mobile.rxjava2andretrofit2.main.presenter.MainPresenterImpl;
import com.mobile.rxjava2andretrofit2.main.view.IMainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseMvpAppActivity<IBaseView, MainPresenterImpl>
        implements IMainView {

    private static final String TAG = "MainActivity";
    @BindView(R.id.mine_view_pager)
    MineLazyViewPager mineViewPager;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @BindView(R.id.tev_first_page)
    TextView tevFirstPage;
    @BindView(R.id.tev_resource_center)
    TextView tevResourceCenter;
    @BindView(R.id.tev_mine)
    TextView tevMine;

    private List<Fragment> fragmentList = new ArrayList<>();
    private TabFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        fragmentList.add(new FirstPageFragment());
        fragmentList.add(new ResourceCenterFragment());
        fragmentList.add(new MineFragment());
    }

    @Override
    protected void initViews() {
//        ImmersionBar.with(MainActivity.this)
//                .keyboardEnable(false)
//                .statusBarDarkFont(false)
//                .statusBarColor(R.color.color_FFE066FF)
//                .navigationBarColor(R.color.color_FFE066FF).init();
        addContentView(loadView, layoutParams);

        fragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mineViewPager.setAdapter(fragmentPagerAdapter);
        mineViewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mineViewPager.setCurrentItem(0);
                        tevFirstPage.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                        tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
                        tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));

                        ImmersionBar.with(MainActivity.this)
                                .keyboardEnable(false)
                                .statusBarDarkFont(false)
                                .statusBarColor(R.color.color_FFE066FF)
                                .navigationBarColor(R.color.color_FFE066FF).init();
                        layoutBottom.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case 1:
                        mineViewPager.setCurrentItem(1);
                        tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
                        tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                        tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));

                        ImmersionBar.with(MainActivity.this)
                                .keyboardEnable(false)
                                .statusBarDarkFont(false)
                                .statusBarColor(R.color.color_FF198CFF)
                                .navigationBarColor(R.color.color_FF198CFF).init();
                        layoutBottom.setBackgroundColor(getResources().getColor(R.color.white));
                        break;
                    case 2:
                        mineViewPager.setCurrentItem(2);
                        tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
                        tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
                        tevMine.setTextColor(getResources().getColor(R.color.color_FFE066FF));

                        ImmersionBar.with(MainActivity.this)
                                .keyboardEnable(false)
                                .statusBarDarkFont(false)
                                .statusBarColor(R.color.color_FFE066FF)
                                .navigationBarColor(R.color.color_FFE066FF).init();
                        layoutBottom.setBackgroundColor(getResources().getColor(R.color.black));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initLoadData() {
        mineViewPager.setCurrentItem(0);
        tevFirstPage.setTextColor(getResources().getColor(R.color.color_FFE066FF));
        tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
        tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));

        ImmersionBar.with(MainActivity.this)
                .keyboardEnable(false)
                .statusBarDarkFont(false)
                .statusBarColor(R.color.color_FFE066FF)
                .navigationBarColor(R.color.color_FFE066FF).init();
    }

    @Override
    protected MainPresenterImpl attachPresenter() {
        return new MainPresenterImpl(this);
    }

    @Override
    public void showLoading() {
        if (loadView != null && !loadView.isShown()) {
            loadView.setVisibility(View.VISIBLE);
            loadView.start();
        }
    }

    @Override
    public void hideLoading() {
        if (loadView != null && loadView.isShown()) {
            loadView.stop();
            loadView.setVisibility(View.GONE);
        }
    }

    @Override
    public void mainDataSuccess(String success) {
        showToast(success, true);
    }

    @Override
    public void mainDataError(String error) {
        showToast(error, true);
    }

    @OnClick({R.id.tev_first_page, R.id.tev_resource_center, R.id.tev_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tev_first_page:
                mineViewPager.setCurrentItem(0);
                tevFirstPage.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
                tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
                break;
            case R.id.tev_resource_center:
                mineViewPager.setCurrentItem(1);
                tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
                tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
                break;
            case R.id.tev_mine:
                mineViewPager.setCurrentItem(2);
                tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
                tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
                tevMine.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                break;
        }
    }
}
