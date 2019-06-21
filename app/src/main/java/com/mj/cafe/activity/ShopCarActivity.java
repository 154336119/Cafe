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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.mj.cafe.BaseActivity;
import com.mj.cafe.BizcContant;
import com.mj.cafe.R;
import com.mj.cafe.adapter.CarAdapter;
import com.mj.cafe.bean.FoodBean;
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
import com.mj.cafe.utils.SharedPreferencesUtil;
import com.mj.cafe.utils.ViewUtils;
import com.mj.cafe.view.AddWidget;
import com.mj.cafe.view.ShopCarView;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private ShopCarView shopCarView;
    public BottomSheetBehavior behavior;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        ButterKnife.bind(this);
        initViews();
        IntentFilter intentFilter = new IntentFilter(CAR_ACTION);
        intentFilter.addAction(CLEARCAR_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        getGoodList();
    }

    private void initViews() {
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    }


    @OnClick({R.id.IvBack, R.id.IvZhongWen, R.id.IvHanYu, R.id.IvYingYu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IvBack:
                finish();
                break;
            case R.id.IvZhongWen:
                break;
            case R.id.IvHanYu:
                break;
            case R.id.IvYingYu:
                carAdapter.getData();
                break;
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
        });
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
//    //网络请求
//    private void getGoodList(){
//        OkGo.<LzyArrayResponse<Type>>post(BizcContant.API  +"/app/goods/category/list")
//                .tag(this)
//                .params("lang", (String)SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,BizcContant._CN))
//                .params("storeId",1)
//                .execute(new JsonCallback<LzyArrayResponse<Type>>() {
//                    @Override
//                    public void onSuccess(Response<LzyArrayResponse<Type>> response) {
////                        RxBus.get().post(new HistoryErrorEvent(response.body().data));
//                       List<Type> typeList = response.body().data;
//                    }
//                });
//    }
    //网络请求
    private void getGoodList(){
        RetrofitSerciveFactory.provideComService().getGoods((String)SharedPreferencesUtil.getData(BizcContant.SP_LANAUAGE,BizcContant._CN),1)
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
}
