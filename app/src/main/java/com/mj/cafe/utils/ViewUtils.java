package com.mj.cafe.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.mj.cafe.R;


public class ViewUtils {
	private static long lastClickTime;


	public static int dip2px(Context context, double d) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (d * scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	// 获取状态栏高度
	public static int getStatusBarHeight(Context mContext) {
		int result = 0;
		int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

		if (resourceId > 0) {
			result = mContext.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 200) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static void addTvAnim(View fromView, int[] carLoc, Context context, final CoordinatorLayout rootview) {
		int[] addLoc = new int[2];
		fromView.getLocationInWindow(addLoc);

		Path path = new Path();
		path.moveTo(addLoc[0], addLoc[1]);
		path.quadTo(carLoc[0], addLoc[1] - 200, carLoc[0], carLoc[1]);

		final TextView textView = new TextView(context);
		textView.setBackgroundResource(R.drawable.circle_blue);
		textView.setText("1");
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(fromView.getWidth(), fromView.getHeight());
		rootview.addView(textView, lp);
		ViewAnimator.animate(textView).path(path).accelerate().duration(400).onStop(new AnimationListener.Stop() {
			@Override
			public void onStop() {
				rootview.removeView(textView);
			}
		}).start();
	}
	public static void showClearCar(Context mContext, DialogInterface.OnClickListener onClickListener,String title
			,String negativeTxt,String PositiveTxt) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		TextView tv = new TextView(mContext);
		tv.setText(title+"?");
		tv.setTextSize(30);
		tv.setPadding(ViewUtils.dip2px(mContext, 26), ViewUtils.dip2px(mContext, 26), 0, 0);
		tv.setTextColor(Color.parseColor("#757575"));
		AlertDialog alertDialog = builder
				.setNegativeButton(negativeTxt, null)
				.setCustomTitle(tv)
				.setPositiveButton(PositiveTxt, onClickListener)
				.show();
		Button nButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		nButton.setTextColor(ContextCompat.getColor(mContext, R.color.color_green));
		nButton.setTextSize(30);
		nButton.setTypeface(Typeface.DEFAULT_BOLD);
		Button pButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		pButton.setTextColor(ContextCompat.getColor(mContext, R.color.color_green));
		pButton.setTextSize(30);
		pButton.setTypeface(Typeface.DEFAULT_BOLD);
	}
}
