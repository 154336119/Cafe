package com.mj.cafe.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.mj.cafe.R;
import com.mj.cafe.bean.TypeBean;
import com.mj.cafe.utils.BaseUtils;
import com.mj.cafe.utils.ViewUtils;
import com.mj.cafe.adapter.FoodAdapter;
import com.mj.cafe.adapter.TypeAdapter;
import com.mj.cafe.bean.FoodBean;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ListContainer extends LinearLayout {

	public TypeAdapter typeAdapter;
	private LinearLayoutManager linearLayoutManager;
	private List<FoodBean> foodBeanList = new ArrayList<>();
	private boolean move;
	private int index;
	private Context mContext;
	public FoodAdapter foodAdapter;
	public static List<FoodBean> commandList = new ArrayList<>();
	private TextView tvStickyHeaderView;
	private View stickView;
	private RecyclerView recyclerView1;
	private RecyclerView recyclerView2;
	public ListContainer(Context context) {
		super(context);
	}

	public ListContainer(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		inflate(mContext, R.layout.view_listcontainer, this);
		recyclerView1 = findViewById(R.id.recycler1);
		recyclerView2 = findViewById(R.id.recycler2);
	}


	private void moveToPosition(int n) {
		//先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
		int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
		int lastItem = linearLayoutManager.findLastVisibleItemPosition();
		//然后区分情况
		if (n <= firstItem) {
			//当要置顶的项在当前显示的第一个项的前面时
			recyclerView2.scrollToPosition(n);
		} else if (n <= lastItem) {
			//当要置顶的项已经在屏幕上显示时
			int top = recyclerView2.getChildAt(n - firstItem).getTop();
			recyclerView2.scrollBy(0, top);
		} else {
			//当要置顶的项在当前显示的最后一项的后面时
			recyclerView2.scrollToPosition(n);
			//这里这个变量是用在RecyclerView滚动监听里面的
			move = true;
		}
	}



	public void setAddClick(AddWidget.OnAddClick onAddClick) {
		foodAdapter = new FoodAdapter(foodBeanList, onAddClick);
		View view = new View(mContext);
		view.setMinimumHeight(ViewUtils.dip2px(mContext, 120));
		foodAdapter.addFooterView(view);
		foodAdapter.bindToRecyclerView(recyclerView2);
		stickView = findViewById(R.id.stick_header);
		tvStickyHeaderView = findViewById(R.id.tv_header);
		tvStickyHeaderView.setText("类别0");
        recyclerView2.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                typeAdapter.fromClick = false;
                return false;
            }
        });
		recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (move) {
					move = false;
					//获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
					int n = index - linearLayoutManager.findFirstVisibleItemPosition();
					if (0 <= n && n < recyclerView.getChildCount()) {
						//获取要置顶的项顶部离RecyclerView顶部的距离
						int top = recyclerView.getChildAt(n).getTop();
						//最后的移动
						recyclerView.smoothScrollBy(0, top);
					}
				} else {
					View stickyInfoView = recyclerView.findChildViewUnder(stickView.getMeasuredWidth() / 2, 5);
					if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
						tvStickyHeaderView.setText(String.valueOf(stickyInfoView.getContentDescription()));
						typeAdapter.setType(String.valueOf(stickyInfoView.getContentDescription()));
					}

					View transInfoView = recyclerView.findChildViewUnder(stickView.getMeasuredWidth() / 2, stickView.getMeasuredHeight
							() + 1);
					if (transInfoView != null && transInfoView.getTag() != null) {
						int transViewStatus = (int) transInfoView.getTag();
						int dealtY = transInfoView.getTop() - stickView.getMeasuredHeight();
						if (transViewStatus == FoodAdapter.HAS_STICKY_VIEW) {
							if (transInfoView.getTop() > 0) {
								stickView.setTranslationY(dealtY);
							} else {
								stickView.setTranslationY(0);
							}
						} else if (transViewStatus == FoodAdapter.NONE_STICKY_VIEW) {
							stickView.setTranslationY(0);
						}
					}
				}
			}
		});
	}


	public void setdata(List<TypeBean> typeBeanList){
		for(TypeBean typeBean : typeBeanList){
			foodBeanList.addAll(typeBean.getGoodsList());
		}

		recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
		//测试数据
		typeAdapter = new TypeAdapter(typeBeanList);
		View view = new View(mContext);
		view.setMinimumHeight(ViewUtils.dip2px(mContext, 120));
		typeAdapter.addFooterView(view);
		typeAdapter.bindToRecyclerView(recyclerView1);
		recyclerView1.addItemDecoration(new SimpleDividerDecoration(mContext));
		((DefaultItemAnimator) recyclerView1.getItemAnimator()).setSupportsChangeAnimations(false);
		recyclerView1.addOnItemTouchListener(new OnItemClickListener() {
			@Override
			public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
				if (recyclerView2.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
					typeAdapter.fromClick = true;
					typeAdapter.setChecked(i);
					String type = view.getTag().toString();
					for (int ii = 0; ii < foodBeanList.size(); ii++) {
						FoodBean typeBean = foodBeanList.get(ii);
						if (typeBean.getType().equals(type)) {
							index = ii;
							moveToPosition(index);
							break;
						}
					}
				}
			}
		});
		linearLayoutManager = new LinearLayoutManager(mContext);
		recyclerView2.setLayoutManager(linearLayoutManager);
		recyclerView2.addItemDecoration(
				new HorizontalDividerItemDecoration.Builder(mContext)
						.color(Color.parseColor("#ffffff"))
						.sizeResId(R.dimen.dp_10)
						.build());
		recyclerView2.addItemDecoration(new SimpleDividerDecoration(mContext));
		((DefaultItemAnimator) recyclerView2.getItemAnimator()).setSupportsChangeAnimations(false);
	}
}
