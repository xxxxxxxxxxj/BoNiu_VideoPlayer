package com.boniu.shipinbofangqi.mvp.view.fragment;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.fingerprintrecognition.FingerprintCore;
import com.boniu.shipinbofangqi.fingerprintrecognition.FingerprintUtil;
import com.boniu.shipinbofangqi.mvp.presenter.MyFragPresenter;
import com.boniu.shipinbofangqi.mvp.view.fragment.base.BaseFragment;
import com.boniu.shipinbofangqi.mvp.view.iview.IMyFragView;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.Global;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:09
 */
public class MyFragment extends BaseFragment<MyFragPresenter> implements IMyFragView {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.sh_fragmy)
    Switch sh_fragmy;
    @BindView(R.id.ll_fragmy_finger)
    LinearLayout ll_fragmy_finger;
    @BindView(R.id.srl_fragmy)
    SmartRefreshLayout srlFragMy;
    private FingerprintCore mFingerprintCore;

    @Override
    protected MyFragPresenter createPresenter() {
        return new MyFragPresenter(mActivity, this);
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
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        srlFragMy.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText("我的");
        ivToolbarBack.setVisibility(View.GONE);
        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));

        mFingerprintCore = new FingerprintCore(mActivity);
        if (mFingerprintCore.isSupport()) {
            ll_fragmy_finger.setVisibility(View.VISIBLE);
            //判断设备是否录入指纹
            boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
            //判断是否开启指纹支付
            boolean isFinger = spUtil.getBoolean(Global.SP_KEY_ISOPENFINGER, false);
            if (isFinger && hasEnrolledFingerprints) {
                sh_fragmy.setSwitchTextAppearance(mActivity, R.style.s_true);
            } else {
                sh_fragmy.setSwitchTextAppearance(mActivity, R.style.s_false);
                spUtil.saveBoolean(Global.SP_KEY_ISOPENFINGER, false);
            }
        } else {
            ll_fragmy_finger.setVisibility(View.GONE);
            spUtil.saveBoolean(Global.SP_KEY_ISOPENFINGER, false);
        }
        sh_fragmy.setChecked(spUtil.getBoolean(Global.SP_KEY_ISOPENFINGER, false));
        sh_fragmy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    //判断设备是否录入指纹
                    boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                    if (hasEnrolledFingerprints) {
                        sh_fragmy.setSwitchTextAppearance(mActivity, R.style.s_true);
                        spUtil.saveBoolean(Global.SP_KEY_ISOPENFINGER, true);
                    } else {
                        RingToast.show("您还没有录制指纹，请录入！");
                        FingerprintUtil.openFingerPrintSettingPage(mActivity);
                    }
                } else {
                    sh_fragmy.setSwitchTextAppearance(mActivity, R.style.s_false);
                    spUtil.saveBoolean(Global.SP_KEY_ISOPENFINGER, false);
                }
            }
        });
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
}
