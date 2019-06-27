package com.mj.cafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.hwangjr.rxbus.RxBus;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.R;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.bean.UserBean;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.utils.SizeUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

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
    @BindView(R.id.RlRootView)
    CardView RlRootView;
    private OptionsPickerView pvOptionsOne;
    private String dialog_title,dialog_negativeTxt,dialog_PositiveTxt;
    private List<String> optList = BizcContant.getCountry();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        ButterKnife.bind(this);
        setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.DEFAULT)));
        type = getIntent().getIntExtra("type", REIGSTER);
        if (type == REIGSTER) {
            LLAgagin.setVisibility(View.VISIBLE);
            textChangeListener(btn, EtPhone, EtPin, EtAgainPin);
            ViewGroup.LayoutParams params = RlRootView.getLayoutParams();
            params.height = SizeUtils.dp2px(this,830);
        } else {
            LLAgagin.setVisibility(View.GONE);
            textChangeListener(btn, EtPhone, EtPin);
            ViewGroup.LayoutParams params = RlRootView.getLayoutParams();
            params.height =  SizeUtils.dp2px(this,700);
            RlRootView.setLayoutParams(params);
        }
        initOptionPickerOne();
        //测试
        EtPhone.setText("15208305795");
        EtPin.setText("123456");

    }

    @OnClick({R.id.TvCountryCode, R.id.btn, R.id.IvBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.TvCountryCode:
                pvOptionsOne.show();
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
        LangTypeBean typeBean = (LangTypeBean)SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.CN));

        Observable<HttpMjResult<UserBean>> observable;
        if (type == REIGSTER) {
            if (!EtPin.getText().toString().equals(EtAgainPin.getText().toString())) {
                return;
            }
            observable = RetrofitSerciveFactory.provideComService().register(typeBean.getUserHttpType(), prefixNo, EtPhone.getText().toString(), EtPin.getText().toString());
        } else {
            observable = RetrofitSerciveFactory.provideComService().login(typeBean.getUserHttpType(), prefixNo, EtPhone.getText().toString(), EtPin.getText().toString());
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

    @Override
    public void setLangView(LangTypeBean langTypeBean) {
        switch (langTypeBean.getType()) {
            case CN:
                TvPhoneTips.setText(getString(R.string.cn_Please_enter_your_cellphone_number));
                TvPinTips.setText(getString(R.string.cn_Please_enter_pin_number));
                TvAgainPinTips.setText(R.string.cn_Please_enter_pin_number_again);
                dialog_negativeTxt = getString(R.string.cn_Confirm);
                dialog_PositiveTxt = getString(R.string.cn_Cancel);
                if (type == REIGSTER){
                    btn.setText(R.string.cn_Create_account);
                    TvLoginTips.setText(R.string.cn_Create_account);
                }else{
                    btn.setText(R.string.cn_Sign_up);
                    TvLoginTips.setText(R.string.cn_Sign_up);
                }
                break;
            case EN:
                TvPhoneTips.setText(getString(R.string.en_Please_enter_your_cellphone_number));
                TvPinTips.setText(getString(R.string.en_Please_enter_pin_number));
                TvAgainPinTips.setText(R.string.en_Please_enter_pin_number_again);
                dialog_negativeTxt = getString(R.string.en_Confirm);
                dialog_PositiveTxt = getString(R.string.en_Cancel);
                if (type == REIGSTER){
                    btn.setText(R.string.en_Create_account);
                    TvLoginTips.setText(R.string.en_Create_account);
                }else{
                    btn.setText(R.string.en_Sign_up);
                    TvLoginTips.setText(R.string.en_Sign_up);
                }
                break;
            case KO:
                TvPhoneTips.setText(getString(R.string.ko_Please_enter_your_cellphone_number));
                TvPinTips.setText(getString(R.string.ko_Please_enter_pin_number));
                TvAgainPinTips.setText(R.string.ko_Please_enter_pin_number_again);
                dialog_negativeTxt = getString(R.string.ko_Confirm);
                dialog_PositiveTxt = getString(R.string.ko_Cancel);
                if (type == REIGSTER){
                    btn.setText(R.string.ko_Create_account);
                    TvLoginTips.setText(R.string.ko_Create_account);
                }else{
                    btn.setText(R.string.ko_Sign_up);
                    TvLoginTips.setText(R.string.ko_Sign_up);
                }
                break;
        }
    }

    private void initOptionPickerOne() {//条件选择器初始化
        pvOptionsOne = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                TvCountryCode.setText(optList.get(options1));
                prefixNo = Integer.parseInt(optList.get(options1));
            }
        })
                .setContentTextSize(30)//设置滚轮文字大小
                .setSelectOptions(0, 1)//默认选中项\
                .setSubCalSize(35)
                .setSubmitText(dialog_negativeTxt)
                .setCancelText(dialog_PositiveTxt)
                .isDialog(true)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        pvOptionsOne.setPicker(optList);//二级选择器
    }
}
