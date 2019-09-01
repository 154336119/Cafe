package com.mj.cafe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.R;
import com.mj.cafe.adapter.CarAdapter;
import com.mj.cafe.bean.FinishActivityEvent;
import com.mj.cafe.bean.FoodBean;
import com.mj.cafe.bean.LangTypeBean;
import com.mj.cafe.bean.TypeBean;
import com.mj.cafe.http.DialogCallback;
import com.mj.cafe.http.JsonCallback;
import com.mj.cafe.http.LzyArrayResponse;
import com.mj.cafe.http.LzyResponse;
import com.mj.cafe.retorfit.HttpMjResult;
import com.mj.cafe.retorfit.RetrofitSerciveFactory;
import com.mj.cafe.retorfit.rxjava.BaseSubscriber;
import com.mj.cafe.retorfit.rxjava.HttpMjEntityFun;
import com.mj.cafe.retorfit.rxjava.RxUtil;
import com.mj.cafe.utils.ActivityUtil;
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.utils.ViewUtils;
import com.mj.cafe.view.AddWidget;
import com.mj.cafe.view.ShopCarView;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mj.cafe.bean.LangTypeBean.CN;
import static com.mj.cafe.bean.LangTypeBean.EN;
import static com.mj.cafe.bean.LangTypeBean.KO;

public class ShopCarActivity extends BaseActivity implements AddWidget.OnAddClick{

    public static final String CAR_ACTION = "handleCar";
    public static final String CLEARCAR_ACTION = "clearCar";
    public static CarAdapter carAdapter;
    @BindView(R.id.IvBack)
    ImageView IvBack;
    @BindView(R.id.IvZhongWen)
    ImageView IvZhongWen;
    @BindView(R.id.IvHanYu)
    ImageView IvHanYu;
    @BindView(R.id.IvYingYu)
    ImageView IvYingYu;
    @BindView(R.id.LlTopBar)
    LinearLayout LlTopBar;
    @BindView(R.id.ListContainer)
    com.mj.cafe.view.ListContainer ListContainer;
    @BindView(R.id.rootview)
    CoordinatorLayout rootview;
    TextView TvClearTips;
    private ShopCarView shopCarView;
    public BottomSheetBehavior behavior;
    //
    private String dialog_title,dialog_negativeTxt,dialog_PositiveTxt;
    //座位
    private String mSeatArray = null;
    //1堂食, 2打包
    private Integer mEnjoyway = null;
    //点餐数据
    private String mGoodsArray =null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        ButterKnife.bind(this);
        mSeatArray = getIntent().getStringExtra("SeatArray");
        mEnjoyway = getIntent().getIntExtra("Enjoyway",1);
        initViews();
        IntentFilter intentFilter = new IntentFilter(CAR_ACTION);
        intentFilter.addAction(CLEARCAR_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        getGoodList();
    }

    private void initViews() {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TvClearTips = (TextView)findViewById(R.id.TvClearTips);
        initShopCar();
        ListContainer.setAddClick(this);
    }

    private void initShopCar() {
        behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));
        shopCarView = (ShopCarView) findViewById(R.id.car_mainfl);
        View blackView = findViewById(R.id.blackview);
        shopCarView.setBehavior(behavior, blackView);
        RecyclerView carRecView = (RecyclerView) findViewById(R.id.car_recyclerview);
//		carRecView.setNestedScrollingEnabled(false);
        carRecView.setLayoutManager(new LinearLayoutManager(this));
        ((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
        carAdapter = new CarAdapter(new ArrayList<FoodBean>(), this);
        carAdapter.bindToRecyclerView(carRecView);
        shopCarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setLangView((LangTypeBean) SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE, new LangTypeBean(LangTypeBean.DEFAULT)));
            }
        });
    }

    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu,R.id.car_limit})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.IvBack:
                finish();
                break;
            case R.id.IvZhongWen:
                postLangLiveData(new LangTypeBean(CN));
                break;
            case R.id.IvHanYu:
                postLangLiveData(new LangTypeBean(KO));
                break;
            case R.id.IvYingYu:
                postLangLiveData(new LangTypeBean(EN));
                break;
            case R.id.car_limit:
                bundle.putString("SeatArray",mSeatArray);
                bundle.putInt("Enjoyway",mEnjoyway);
                bundle.putString("GoodsArray", converGoodsData());
                bundle.putString("ShowTotialAccount",shopCarView.getTotaliAccount());
                ActivityUtil.next(this,ChoosePayTypeActitiy.class,bundle,false);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case CAR_ACTION:
                    FoodBean foodBean = (FoodBean) intent.getSerializableExtra("foodbean");
                    FoodBean fb = foodBean;
                    int p = intent.getIntExtra("position", -1);
                    if (p >= 0 && p < ListContainer.foodAdapter.getItemCount()) {
                        fb = ListContainer.foodAdapter.getItem(p);
                        fb.setSelectCount(foodBean.getSelectCount());
                        ListContainer.foodAdapter.setData(p, fb);
                    } else {
                        for (int i = 0; i < ListContainer.typeAdapter.getItemCount(); i++) {
                            fb = ListContainer.foodAdapter.getItem(i);
                            if (fb.getId() == foodBean.getId()) {
                                fb.setSelectCount(foodBean.getSelectCount());
                                ListContainer.foodAdapter.setData(i, fb);
                                break;
                            }
                        }
                    }
                    dealCar(fb);
                    break;
                case CLEARCAR_ACTION:

                    clearCar();
                    break;
            }
            if (CAR_ACTION.equals(intent.getAction())) {

            }
        }
    };

    public void clearCar(View view) {


        ViewUtils.showClearCar(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCar();
            }
        },dialog_title,dialog_PositiveTxt,dialog_negativeTxt);
    }

    private void clearCar() {
        List<FoodBean> flist = carAdapter.getData();
        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);
            fb.setSelectCount(0);
        }
        carAdapter.setNewData(new ArrayList<FoodBean>());
        ListContainer.foodAdapter.notifyDataSetChanged();
        shopCarView.showBadge(0);
//        ListContainer.typeAdapter.updateBadge(new HashMap<String, Long>());
        shopCarView.updateAmount(new BigDecimal(0.0));
    }

    private void dealCar(FoodBean foodBean) {
        HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        boolean hasFood = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            ListContainer.foodAdapter.notifyDataSetChanged();
        }
        List<FoodBean> flist = carAdapter.getData();
        int p = -1;
        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);
            if (fb.getId() == foodBean.getId() && fb.getType().equals(foodBean.getType())) {
                fb = foodBean;
                hasFood = true;
                if (foodBean.getSelectCount() == 0) {
                    p = i;
                } else {
                    carAdapter.setData(i, foodBean);
                }
            }
            total += fb.getSelectCount();
            if (typeSelect.containsKey(fb.getType())) {
                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
            } else {
                typeSelect.put(fb.getType(), fb.getSelectCount());
            }
            amount = amount.add(fb.getBigDecimalPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
        }
        if (p >= 0) {
            carAdapter.remove(p);
        } else if (!hasFood && foodBean.getSelectCount() > 0) {
            carAdapter.addData(foodBean);
            if (typeSelect.containsKey(foodBean.getType())) {
                typeSelect.put(foodBean.getType(), typeSelect.get(foodBean.getType()) + foodBean.getSelectCount());
            } else {
                typeSelect.put(foodBean.getType(), foodBean.getSelectCount());
            }
            amount = amount.add(foodBean.getBigDecimalPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
            total += foodBean.getSelectCount();
        }
        shopCarView.showBadge(total);
//        ListContainer.typeAdapter.updateBadge(typeSelect);
        shopCarView.updateAmount(amount);
    }

    @Override
    public void onAddClick(View view, FoodBean fb) {
        dealCar(fb);
        ViewUtils.addTvAnim(view, shopCarView.carLoc, this, rootview);
    }


    @Override
    public void onSubClick(FoodBean fb) {
        dealCar(fb);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
    //网络请求
    private void getGoodList(){
        LangTypeBean typeBean = (LangTypeBean)SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,new LangTypeBean(LangTypeBean.CN));
        RetrofitSerciveFactory.provideComService().getGoods(typeBean.getUserHttpType(),1)
                .compose(RxUtil.<HttpMjResult<List<TypeBean>>>applySchedulersForRetrofit())
                .map(new HttpMjEntityFun<List<TypeBean>>())
                .subscribe(new BaseSubscriber<List<TypeBean>>(this) {
                    @Override
                    public void onNext(List<TypeBean> entity) {
                        super.onNext(entity);
                        ListContainer.setdata(entity);
                    }
                });
    }

    //转换菜单数据
    private String converGoodsData(){
        JSONArray array = new JSONArray();
        for(FoodBean foodBean : carAdapter.getData()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("goodsId",foodBean.getId());
            jsonObject.put("num",foodBean.getSelectCount());
            array.add(jsonObject);
        }
        Logger.d("================:"+array.toJSONString());
        return array.toJSONString();
    }


    @Override
    public void setLangView(LangTypeBean langTypeBean) {
        switch (langTypeBean.getType()) {
            case CN:
                shopCarView.TvTotalTips.setText(getString(R.string.cn_Total));
                shopCarView.car_limit.setText(getString(R.string.cn_Payment));
                TvClearTips.setText(R.string.cn_Delete_all);
                dialog_title = getString(R.string.cn_Delete_all);
                dialog_negativeTxt = getString(R.string.cn_Confirm);
                dialog_PositiveTxt = getString(R.string.cn_Cancel);
                break;
            case EN:
                shopCarView.TvTotalTips.setText(getString(R.string.en_Total));
                shopCarView.car_limit.setText(getString(R.string.en_Payment));
                TvClearTips.setText(R.string.en_Delete_all);
                dialog_title = getString(R.string.en_Delete_all);
                dialog_negativeTxt = getString(R.string.en_Confirm);
                dialog_PositiveTxt = getString(R.string.en_Cancel);
                break;
            case KO:
                shopCarView.TvTotalTips.setText(getString(R.string.ko_Total));
                shopCarView.car_limit.setText(getString(R.string.ko_Payment));
                TvClearTips.setText(R.string.ko_Delete_all);
                dialog_title = getString(R.string.ko_Delete_all);
                dialog_negativeTxt = getString(R.string.ko_Confirm);
                dialog_PositiveTxt = getString(R.string.ko_Cancel);
                break;
        }
    }

    @Override
    public void langChangeForHttp() {
        super.langChangeForHttp();
        clearCar();
        getGoodList();
    }

    @Override
    protected boolean rxBusRegist() {
        return true;
    }
    @Subscribe
    public void onFinishEvent(FinishActivityEvent event) {
        finish();
    }
    @Override
    protected boolean setOpenTimeDown() {
        return true;
    }
}
