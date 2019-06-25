package com.mj.cafe.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mj.cafe.R;
import com.mj.cafe.bean.FoodBean;
import com.mj.cafe.bean.PayTypeBean;
import com.mj.cafe.view.AddWidget;

import java.math.BigDecimal;
import java.util.List;

public class PayTypeAdapter extends BaseQuickAdapter<PayTypeBean, BaseViewHolder> {

	public PayTypeAdapter(@Nullable List<PayTypeBean> data) {
		super(R.layout.item_paytype, data);
	}

	@Override
	protected void convert(BaseViewHolder helper, PayTypeBean item) {
		ImageView imageView = helper.getView(R.id.IvLogo);
		if(item.getPay_name().equals("Kakao Pay")){
			helper.setImageResource(R.id.IvLogo,R.drawable.kakaopay);
		}else if(item.getPay_name().equals("PAYCO")){
			helper.setImageResource(R.id.IvLogo,R.drawable.payco);
		}else if(item.getPay_name().equals("AliPAY")){
			helper.setImageResource(R.id.IvLogo,R.drawable.zhifubaozhifu);
		}else if(item.getPay_name().equals("Wechat Pay")){
			helper.setImageResource(R.id.IvLogo,R.drawable.weixinzhifu);
		}
//		helper.setText(R.id.car_name, item.getName())
//				.setText(R.id.car_price, item.getStrPrice(mContext, item.getBigDecimalPrice().multiply(BigDecimal.valueOf(item.getSelectCount()))))
	}
}
