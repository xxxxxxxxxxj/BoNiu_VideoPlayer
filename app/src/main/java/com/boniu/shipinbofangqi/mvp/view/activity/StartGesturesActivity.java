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
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.event.GestureSuccessEvent;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.widget.gestures.Lock9View;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.GetGestures;
import com.gyf.immersionbar.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手势密码验证界面
 */
public class StartGesturesActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.hint_desc_tv)
    TextView hintDescTv;
    @BindView(R.id.lock_9_view)
    Lock9View lock9View;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.iv_toolbar_back)
    ImageView iv_toolbar_back;
    private String password;
    private int errorCount = 5;
    private int type;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_start_gestures;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvToolbarTitle.setText("请输入密码");
        password = GetGestures.readGestures(mContext);
        RingLog.e("password = " + password);
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
        type = getIntent().getIntExtra("type", 0);
    }

    @Override
    protected void initEvent() {
        lock9View.setGestureCallback(new Lock9View.GestureCallback() {

            @Override
            public void onNodeConnected(@NonNull int[] numbers) {
            }

            @Override
            public boolean onGestureFinished(@NonNull int[] numbers) {
                if (errorCount - 1 <= 0) {
                    RingToast.show("密码错误");
                    finish();
                } else {
                    StringBuilder builder = new StringBuilder();
                    for (int number : numbers) {
                        builder.append(number);
                    }
                    String inputPwd = builder.toString();
                    if (!password.equals(inputPwd)) {
                        hintDescTv.setTextColor(Color.RED);
                        errorCount -= 1;
                        hintDescTv.setText("手势密码不正确,剩余尝试次数" + errorCount + "次");
                    } else {
                        hintDescTv.setTextColor(Color.GRAY);
                        hintDescTv.setText("密码正确");
                        RingToast.show("密码正确");
                        if (type > 0) {
                            EventBus.getDefault().post(new GestureSuccessEvent(type));
                        } else {
                            startActivity(SetGesturesActivity.class);
                        }
                        finish();
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
