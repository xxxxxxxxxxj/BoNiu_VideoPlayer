package com.boniu.shipinbofangqi.mvp.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.widget.gestures.LinkageGroup;
import com.boniu.shipinbofangqi.mvp.view.widget.gestures.Lock9View;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.GetGestures;
import com.gyf.immersionbar.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置手势密码界面
 */
public class SetGesturesActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.linkage_parent_view)
    LinkageGroup linkageParentView;
    @BindView(R.id.hint_desc_tv)
    TextView hintDescTv;
    @BindView(R.id.lock_9_view)
    Lock9View lock9View;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.iv_toolbar_back)
    ImageView iv_toolbar_back;
    private String tmp_password = "";

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_set_gestures;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvToolbarTitle.setText("设置手势密码");
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.a2D2D2D));
        iv_toolbar_back.setImageResource(R.mipmap.icon_title_close);
        tvToolbarTitle.setVisibility(View.GONE);
        ImmersionBar.with(this).statusBarColor(R.color.a2D2D2D).init();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        lock9View.setGestureCallback(new Lock9View.GestureCallback() {

            @Override
            public void onNodeConnected(@NonNull int[] numbers) {
                linkageParentView.autoLinkage(numbers, lock9View.lineColor);
            }

            @Override
            public boolean onGestureFinished(@NonNull int[] numbers) {
                StringBuilder builder = new StringBuilder();
                for (int number : numbers) {
                    builder.append(number);
                }
                String value = builder.toString();
                if (tmp_password.isEmpty()) {
                    if (numbers.length < 4) {
                        hintDescTv.setTextColor(Color.RED);
                        hintDescTv.setText("至少链接4个点,请重新绘制");
                        linkageParentView.clearLinkage();
                        return true;
                    } else {
                        linkageParentView.clearLinkage();
                        hintDescTv.setTextColor(Color.GRAY);
                        hintDescTv.setText("请再次绘制解锁图案");
                        tmp_password = value;
                    }
                } else {
                    if (numbers.length < 4) {
                        tmp_password = "";
                        hintDescTv.setTextColor(Color.RED);
                        hintDescTv.setText("至少链接4个点,请重新绘制");
                        linkageParentView.clearLinkage();
                        return true;
                    } else {
                        if (tmp_password.equals(value)) {
                            hintDescTv.setText("设置手势密码成功");
                            hintDescTv.setTextColor(Color.GRAY);
                            tmp_password = "";
                            GetGestures.saveGestures(mContext, value);
                            RingToast.show("设置手势密码成功");
                            finish();
                        } else {
                            linkageParentView.clearLinkage();
                            hintDescTv.setText("两次绘制不一致,请重新绘制");
                            hintDescTv.setTextColor(Color.RED);
                            tmp_password = "";
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBack(false);
    }

    @OnClick({R.id.iv_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
