package com.boniu.shipinbofangqi.mvp.view.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.model.event.MatisseDataEvent;
import com.boniu.shipinbofangqi.mvp.presenter.ResourcesFragPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.LoginActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.MainActivity;
import com.boniu.shipinbofangqi.mvp.view.fragment.base.BaseFragment;
import com.boniu.shipinbofangqi.mvp.view.iview.IResourcesFragView;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-29 09:48
 */
public class ResourcesFragment extends BaseFragment<ResourcesFragPresenter> implements IResourcesFragView {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.srl_fragresources)
    SmartRefreshLayout srlFragResources;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(MatisseDataEvent event) {
        if (event != null) {
            MainActivity mainActivity = (MainActivity) mActivity;
            mainActivity.setFragMentIndex(0);
        }
    }

    @Override
    protected ResourcesFragPresenter createPresenter() {
        return new ResourcesFragPresenter(mActivity, this);
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_resources;
    }

    @Override
    protected void initView() {
        srlFragResources.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText("资源");
        ivToolbarBack.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.ll_fragresources_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_fragresources_input:
                if (CommonUtil.isLogin(mActivity)) {
                    getVideo(9);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
        }
    }

    // Fragment页面onResume函数重载
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ResourcesFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    // Fragment页面onResume函数重载
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ResourcesFragment");
    }
}
