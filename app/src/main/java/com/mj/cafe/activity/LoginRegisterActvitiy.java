package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.R;
import com.mj.cafe.bean.UserBean;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class LoginRegisterActvitiy extends BaseActivity {
    public static final int REIGSTER = 0;
    public static final int LOGIN = 1;
    public int type;
    @BindView(R.id.TvLoginTips)
    TextView TvLoginTips;
    @BindView(R.id.TvPhoneTips)
    TextView TvPhoneTips;
    @BindView(R.id.TvCountryCode)
    TextView TvCountryCode;
    @BindView(R.id.TvPinTips)
    TextView TvPinTips;
    @BindView(R.id.TvAgainPinTips)
    TextView TvAgainPinTips;
    @BindView(R.id.LLAgagin)
    LinearLayout LLAgagin;
    @BindView(R.id.btn)
    TextView btn;
    @BindView(R.id.EtPhone)
    EditText EtPhone;
    @BindView(R.id.EtPin)
    EditText EtPin;
    @BindView(R.id.EtAgainPin)
    EditText EtAgainPin;
    //国家手机区号
    int prefixNo = 86;
    @BindView(R.id.IvBack)
    ImageView IvBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", REIGSTER);
        if (type == REIGSTER) {
            btn.setText("注册");
            TvLoginTips.setText("注册");
            LLAgagin.setVisibility(View.VISIBLE);
            textChangeListener(btn, EtPhone, EtPin, EtAgainPin);
        } else {
            btn.setText("登录");
            TvLoginTips.setText("登录");
            LLAgagin.setVisibility(View.GONE);
            textChangeListener(btn, EtPhone, EtPin);
        }
    }

    @OnClick({R.id.TvCountryCode, R.id.btn,R.id.IvBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.TvCountryCode:
                break;
            case R.id.btn:
                registerOrLogin();
                break;
            case R.id.IvBack:
                finish();
                break;
        }
    }


    //注册
    private void registerOrLogin() {
        Observable<HttpMjResult<UserBean>> observable;
        if (type == REIGSTER) {
            if (!EtPin.getText().toString().equals(EtAgainPin.getText().toString())) {
                showToastMsg("PIN码不一致");
                return;
            }
            observable = RetrofitSerciveFactory.provideComService().register("cn", prefixNo, EtPhone.getText().toString(), EtPin.getText().toString());
        } else {
            observable = RetrofitSerciveFactory.provideComService().login("cn", prefixNo, EtPhone.getText().toString(), EtPin.getText().toString());
        }
        observable.compose(RxUtil.<HttpMjResult<UserBean>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<UserBean>())
                .subscribe(new BaseSubscriber<UserBean>(this) {
                    @Override
                    public void onNext(UserBean entity) {
                        super.onNext(entity);
                        RxBus.get().post(entity);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                    }
                });
    }


    /**
     * 监听页面内容是否发生变化
     */
    protected void textChangeListener(final TextView button, final TextView... textViews) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int j = 0;
                int len = textViews.length;
                for (int i = 0; i < len; i++) {
                    if (!TextUtils.isEmpty(textViews[i].getText().toString())) {
                        j++;
                    }
                }
                if (j == len) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        };
        int len = textViews.length;
        for (int i = 0; i < len; i++) {
            textViews[i].addTextChangedListener(textWatcher);
        }
    }
}
