package com.boniu.shipinbofangqi.mvp.view.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.presenter.VideoFragPresenter;
import com.boniu.shipinbofangqi.mvp.view.fragment.base.BaseFragment;
import com.boniu.shipinbofangqi.mvp.view.iview.IVideoFragView;

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
public class VideoFragment extends BaseFragment<VideoFragPresenter> implements IVideoFragView {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.iv_toolbar_right)
    ImageView ivToolbarRight;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;

    @Override
    protected VideoFragPresenter createPresenter() {
        return new VideoFragPresenter(mActivity, this);
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        tvToolbarTitle.setText("视频");
        ivToolbarBack.setVisibility(View.GONE);
        ivToolbarRight.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
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

    @OnClick({R.id.iv_toolbar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_right:
                break;
        }
    }
}
