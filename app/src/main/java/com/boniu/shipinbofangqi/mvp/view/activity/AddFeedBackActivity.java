package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.presenter.AddFeedBackActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.iview.IAddFeedBackActivityView;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加反馈页面
 */
public class AddFeedBackActivity extends BaseActivity<AddFeedBackActivityPresenter> implements IAddFeedBackActivityView {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_addfeedback_name)
    TextView tvAddfeedbackName;
    @BindView(R.id.et_addfeedback_name)
    EditText etAddfeedbackName;
    @BindView(R.id.showtext)
    TextView showtext;
    private String name;
    private String type;
    //输入框初始值
    private int num = 0;
    //输入框最大值
    public int mMaxNum = 400;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_add_feed_back;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvToolbarTitle.setText("帮助与反馈");
        tvAddfeedbackName.setText(name);
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {//界面加载完毕
            CommonUtil.showSoftInputFromWindow(mActivity, etAddfeedbackName);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initEvent() {
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                try {
                    //String regex = "/^(\\w|-|[\\u4E00-\\u9FA5])*$/";
                    //boolean isChinese = Pattern.matches(regex, charSequence.toString());
                    if (!Character.isLetterOrDigit(charSequence.charAt(i))/* || isChinese*/) {
                        return "";
                    }
                    return null;
                } catch (Exception e) {
                    RingLog.e("e = " + e.toString());
                    return null;
                }
            }
        };
        etAddfeedbackName.setFilters(new InputFilter[]{inputFilter});
        etAddfeedbackName.addTextChangedListener(new TextWatcher() {
            //记录输入的字数
            private CharSequence wordNum;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //实时记录输入的字数
                wordNum = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num + s.length();
                //TextView显示剩余字数
                showtext.setText("" + number + "/400");
                selectionStart = etAddfeedbackName.getSelectionStart();
                selectionEnd = etAddfeedbackName.getSelectionEnd();
                //判断大于最大值
                if (wordNum.length() > mMaxNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    etAddfeedbackName.setText(s);
                    etAddfeedbackName.setSelection(tempSelection);//设置光标在最后
                    RingToast.show("至多400字");
                }
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
    protected AddFeedBackActivityPresenter createPresenter() {
        return new AddFeedBackActivityPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.iv_toolbar_back, R.id.tv_addfeedback_sub})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_addfeedback_sub:
                if (StringUtil.isEmpty(StringUtil.checkEditText(etAddfeedbackName))) {
                    RingToast.show("不能为空～");
                    return;
                }
                if (StringUtil.checkEditText(etAddfeedbackName).length() < 4) {
                    RingToast.show("至少输入4个字符～");
                    return;
                }
                showLoadDialog();
                mPresenter.addFeedBack(type, StringUtil.checkEditText(etAddfeedbackName));
                break;
        }
    }

    @Override
    public void addFeedBackSuccess(Boolean response) {
        RingLog.e("addFeedBackSuccess() response = " + response);
        hideLoadDialog();
        if (response) {
            RingToast.show("提交成功");
            etAddfeedbackName.setText("");
        }
    }

    @Override
    public void addFeedBackFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("addFeedBackFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
        if (errorCode == AppConfig.EXIT_USER_CODE) {
            spUtil.removeData(Global.SP_KEY_ISLOGIN);
            spUtil.removeData(Global.SP_KEY_CELLPHONE);
            spUtil.removeData(Global.SP_KEY_ACCOUNTIUD);
            spUtil.removeData(Global.SP_KEY_TOKEN);
            RingToast.show("您已在其他设备登录");
            startActivity(LoginActivity.class);
        } else if (errorCode == AppConfig.CLEARACCOUNTID_CODE) {
            CommonUtil.getNewAccountId(mActivity);
        } else {
            int netWorkState = CommonUtil.getNetWorkState(mContext);
            if (netWorkState == CommonUtil.NETWORK_NONE) {
                RingToast.show("无网络连接");
            } else {
                RingToast.show(errorMsg);
            }
        }
    }
}
