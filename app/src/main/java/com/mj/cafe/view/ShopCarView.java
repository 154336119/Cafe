package com.mj.cafe.view;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mj.cafe.R;
import com.mj.cafe.utils.ViewUtils;

import java.math.BigDecimal;

import static com.mj.cafe.activity.ShopCarActivity.carAdapter;


public class ShopCarView extends FrameLayout {
	public TextView car_limit, tv_amount;
	public ImageView iv_shop_car;
	public TextView car_badge;
	public TextView TvTotalTips;
	private BottomSheetBehavior behavior;
	public boolean sheetScrolling;
	public View shoprl;
	public int[] carLoc;

	public void setBehavior(final BottomSheetBehavior behavior) {
		this.behavior = behavior;
	}

	public void setBehavior(final BottomSheetBehavior behavior, final View blackView) {
		this.behavior = behavior;
		behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(@NonNull View bottomSheet, int newState) {
				sheetScrolling = false;
				if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
					blackView.setVisibility(View.GONE);
				}
			}

			@Override
			public void onSlide(@NonNull View bottomSheet, float slideOffset) {
				sheetScrolling = true;
				blackView.setVisibility(View.VISIBLE);
				ViewCompat.setAlpha(blackView, slideOffset);
			}
		});
		blackView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
				return true;
			}
		});
	}

	public ShopCarView(@NonNull Context context) {
		super(context);
	}

	public ShopCarView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (iv_shop_car == null) {
			iv_shop_car = findViewById(R.id.iv_shop_car);
			car_badge = findViewById(R.id.car_badge);
			car_limit = (TextView)findViewById(R.id.car_limit);
			tv_amount = findViewById(R.id.tv_amount);
			shoprl = findViewById(R.id.car_rl);
			TvTotalTips = (TextView)findViewById(R.id.TvTotalTips);
			shoprl.setOnClickListener(new toggleCar());
			carLoc = new int[2];
			iv_shop_car.getLocationInWindow(carLoc);
			carLoc[0] = carLoc[0] + iv_shop_car.getWidth() / 2 - ViewUtils.dip2px(getContext(), 10);
		}
	}

	public void updateAmount(BigDecimal amount) {
		if (amount.compareTo(new BigDecimal(0.0)) == 0) {
			car_limit.setEnabled(false);
//			findViewById(R.id.amount_container).setVisibility(View.GONE);
			behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
		}else {
			car_limit.setEnabled(true);
			findViewById(R.id.amount_container).setVisibility(View.VISIBLE);
		}
		tv_amount.setText("₩" + amount);
	}

	public void showBadge(int total) {
		if (total > 0) {
			car_badge.setVisibility(View.VISIBLE);
			car_badge.setText(total + "");
		} else {
			car_badge.setVisibility(View.INVISIBLE);
		}
	}

	private class toggleCar implements OnClickListener {

		@Override
		public void onClick(View view) {
			if (sheetScrolling) {
				return;
			}
			if (carAdapter.getItemCount() == 0) {
				return;
			}
			if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
				behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
			} else {
				behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
			}
		}
	}


	//返回显示金额，有符号
	public String getTotaliAccount(){
		return tv_amount.getText().toString();
	}

}
