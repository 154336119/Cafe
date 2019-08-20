package com.mj.cafe.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mj.cafe.R;
import com.mj.cafe.bean.FoodBean;
import com.mj.cafe.bean.SeatBean;
import com.mj.cafe.view.AddWidget;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatAdapter extends BaseQuickAdapter<SeatBean, BaseViewHolder> {
	private List<String> mList;
	private Context mContext;
	private Map<Integer, Boolean> map = new HashMap<>();
	private boolean onBind;
	private int checkedPosition = -1;
    private OnSelectBtnEnable mOnSelectBtnEnable;
	public interface OnSelectBtnEnable{
		void isEnable(boolean enable);
	}

	public SeatAdapter(@Nullable List<SeatBean> data ,Context context,OnSelectBtnEnable onSelectBtnEnable) {
		super(R.layout.item_seat, data);
		mContext = context;
		mOnSelectBtnEnable = onSelectBtnEnable;
	}

	@Override
	protected void convert(final BaseViewHolder helper, SeatBean item) {
		CheckBox checkBox = helper.getView(R.id.CheckBox);
		if(item.getEmpty()){
			helper.setBackgroundColor(R.id.Rlbackgrond, mContext.getResources().getColor(R.color.white));
			checkBox.setVisibility(View.GONE);
			helper.setVisible(R.id.TvNo,false);
		}else{
			helper.setBackgroundColor(R.id.Rlbackgrond, mContext.getResources().getColor(R.color.color_F1F1F1));
			checkBox.setVisibility(View.VISIBLE);
			helper.setVisible(R.id.TvNo,true);
			helper.setText(R.id.TvNo, item.getSeat_no());
//			//测试
//			if(item.getSeat_no().equals("A")){
//				checkBox.setEnabled(false);
//			}else{
//				checkBox.setEnabled(true);
//			}
			checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked == true) {
						map.clear();
						map.put(helper.getPosition(), true);
						checkedPosition = helper.getPosition();
						mOnSelectBtnEnable.isEnable(true);
					} else {
						map.remove(helper.getPosition());
						if (map.size() == 0) {
							checkedPosition = -1; //-1 代表一个都未选择
							mOnSelectBtnEnable.isEnable(false);
						}
					}
					if (!onBind) {
						notifyDataSetChanged();
					}
				}
			});
			onBind = true;
			if (map != null && map.containsKey(helper.getPosition())) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}
			onBind = false;
		}


//		helper.setText(R.id.car_name, item.getName())
//				.setText(R.id.car_price, item.getStrPrice(mContext, item.getBigDecimalPrice().multiply(BigDecimal.valueOf(item.getSelectCount()))))
	}

	//得到当前选中的位置
	public int getCheckedPosition() {
		return checkedPosition;
	}
}
