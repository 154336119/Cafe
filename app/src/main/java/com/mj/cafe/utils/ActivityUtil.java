package com.mj.cafe.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mj.cafe.R;


/**
 * @ClassName: ActivityUtil
 * @Package com.huizhuang.zxsq.utils
 * @Description: activity跳转工具
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月25日  上午9:49:41
 */
public class ActivityUtil {

	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 */
	public static void next(Activity curActivity, Class<?> nextActivity) {
		next(curActivity, nextActivity, null, -1, -1, R.anim.in_from_right,R.anim.out_to_left, false);
	}
	
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity) {
		nextByFragment(fragment, nextActivity, null, -1, -1, R.anim.in_from_right,R.anim.out_to_left, false);
	}
	
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, int inAnimId, int outAnimId) {
		nextByFragment(fragment, nextActivity, null, -1, -1, inAnimId, outAnimId, false);
	}
	
	public static void next(Activity curActivity, Class<?> nextActivity, int inAnimId, int outAnimId) {
		next(curActivity, nextActivity, null, -1, -1, inAnimId, outAnimId, false);
	}
	
	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param extras
	 * @param reqCode
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, Bundle extras, int reqCode) {
		next(curActivity, nextActivity, extras, reqCode, -1, R.anim.in_from_right, R.anim.out_to_left, false);
	}
	
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, Bundle extras, int reqCode) {
		nextByFragment(fragment, nextActivity, extras, reqCode, -1, R.anim.in_from_right, R.anim.out_to_left, false);
	}
	
	
	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param flag
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, int flag, int inAnimId, int outAnimId) {
		next(curActivity, nextActivity, null, -1, flag, inAnimId, outAnimId, false);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, int flag, int inAnimId, int outAnimId) {
		nextByFragment(fragment, nextActivity, null, -1, flag, inAnimId, outAnimId, false);
	}
	
	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param extras
	 * @param reqCode
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, Bundle extras, int reqCode, int inAnimId, int outAnimId) {
		next(curActivity, nextActivity, extras, reqCode, -1, inAnimId, outAnimId, false);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, Bundle extras, int reqCode, int inAnimId, int outAnimId) {
		nextByFragment(fragment, nextActivity, extras, reqCode, -1, inAnimId, outAnimId, false);
	}
	
	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param extras
	 * @param reqCode
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, Bundle extras, int reqCode, int flag, int inAnimId, int outAnimId) {
		next(curActivity, nextActivity, extras, reqCode, -1, inAnimId, outAnimId, false);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, Bundle extras, int reqCode, int flag, int inAnimId, int outAnimId) {
		nextByFragment(fragment, nextActivity, extras, reqCode, -1, inAnimId, outAnimId, false);
	}
	
	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param isFinish 当前activity是否finish掉
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, boolean isFinish) {
		next(curActivity, nextActivity, null, -1, -1, R.anim.in_from_right, R.anim.out_to_left, isFinish);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, boolean isFinish) {
		nextByFragment(fragment, nextActivity, null, -1, -1, R.anim.in_from_right, R.anim.out_to_left, isFinish);
	}
	/**
	 * 携带数据跳转到下一个页面
	 * @param curActivity
	 * @param nextActivity
	 * @param extra
	 * @param isFinish
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, Bundle extra, boolean isFinish) {
		next(curActivity, nextActivity, extra, -1, -1, R.anim.in_from_right, R.anim.out_to_left, isFinish);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, Bundle extra, boolean isFinish) {
		nextByFragment(fragment, nextActivity, extra, -1, -1, R.anim.in_from_right, R.anim.out_to_left, isFinish);
	}
	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 * @param isFinish 当前activity是否finish掉
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, int inAnimId, int outAnimId , boolean isFinish) {
		next(curActivity, nextActivity, null, -1, -1, inAnimId, outAnimId, isFinish);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, int inAnimId, int outAnimId , boolean isFinish) {
		nextByFragment(fragment, nextActivity, null, -1, -1, inAnimId, outAnimId, isFinish);
	}
	
	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param bd
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, Bundle bd, int inAnimId, int outAnimId, boolean finish) {
		next(curActivity, nextActivity, bd, -1, -1, inAnimId, outAnimId, finish);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, Bundle bd, int inAnimId, int outAnimId, boolean finish) {
		nextByFragment(fragment, nextActivity, bd, -1, -1, inAnimId, outAnimId, finish);
	}
	
	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param extras
	 * @param reqCode
	 * @param isFinish 当前activity是否finish掉
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, Bundle extras, int reqCode , boolean isFinish) {
		next(curActivity, nextActivity, extras, reqCode, -1, R.anim.in_from_right, R.anim.out_to_left, isFinish);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, Bundle extras, int reqCode , boolean isFinish) {
		nextByFragment(fragment, nextActivity, extras, reqCode, -1, R.anim.in_from_right, R.anim.out_to_left, isFinish);
	}


	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param flag
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 * @param isFinish 当前activity是否finish掉
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, int flag, int inAnimId, int outAnimId, boolean isFinish) {
		next(curActivity, nextActivity, null, -1, flag, inAnimId, outAnimId, isFinish);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, int flag, int inAnimId, int outAnimId, boolean isFinish) {
		nextByFragment(fragment, nextActivity, null, -1, flag, inAnimId, outAnimId, isFinish);
	}

	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param extras
	 * @param reqCode
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 * @param isFinish 当前activity是否finish掉
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, Bundle extras, int reqCode, int inAnimId, int outAnimId, boolean isFinish) {
		next(curActivity, nextActivity, extras, reqCode, -1, inAnimId, outAnimId, isFinish);
	}
	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, Bundle extras, int reqCode, int inAnimId, int outAnimId, boolean isFinish) {
		nextByFragment(fragment, nextActivity, extras, reqCode, -1, inAnimId, outAnimId, isFinish);
	}

	/**
	 * 跳转到下一个页面
	 * 
	 * @param curActivity
	 * @param nextActivity
	 * @param extras
	 * @param reqCode
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 * @param isFinish 当前activity是否finish掉
	 */
	public static void next(Activity curActivity, Class<?> nextActivity, Bundle extras, int reqCode, int flag, int inAnimId, int outAnimId, boolean isFinish) {
		Intent intent = new Intent(curActivity, nextActivity);
		if (null != extras) {
			intent.putExtras(extras);
		}
		if (flag != -1) {
			intent.setFlags(flag);
		}
		//设置返回值模式
		if (reqCode < 0) {
			curActivity.startActivity(intent);
		} else {
			curActivity.startActivityForResult(intent, reqCode);
		}
		//加入转场动画
		if (inAnimId != -1 && outAnimId != -1) {
			curActivity.overridePendingTransition(inAnimId, outAnimId);
		}
		//是否销毁当前activity
		if (isFinish) {
			curActivity.finish();
		}
	}

	/**
	 * 直接返回到指定的某个页面
	 * 
	 * @param curActivity
	 * @param backActivity
	 */
	public static void nextActivityWithClearTop(Activity curActivity, Class<?> backActivity) {
		nextActivityWithClearTop(curActivity, backActivity, null, R.anim.in_from_left, R.anim.out_to_right);
	}
	
	/**
	 * 直接返回到指定的某个页面
	 * 
	 * @param curActivity
	 * @param backActivity
	 * @param inAnimId
	 * @param outAnimId
	 */
	public static void nextActivityWithClearTop(Activity curActivity, Class<?> backActivity, int inAnimId, int outAnimId) {
		nextActivityWithClearTop(curActivity, backActivity, null, inAnimId, outAnimId);
	}

	/**
	 * 直接返回到指定的某个页面
	 * 
	 * @param curActivity
	 * @param backActivity
	 * @param extras
	 */
	public static void nextActivityWithClearTop(Activity curActivity, Class<?> backActivity, Bundle extras) {
		nextActivityWithClearTop(curActivity, backActivity, extras, R.anim.in_from_left, R.anim.out_to_right);
	}

	/**
	 * 直接返回到指定的某个页面
	 * 
	 * @param curActivity
	 * @param backActivity
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 */
	public static void nextActivityWithClearTop(Activity curActivity, Class<?> backActivity, Bundle extras, int inAnimId, int outAnimId) {
		Intent intent = new Intent(curActivity, backActivity);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (null != extras) {
			intent.putExtras(extras);
		}
		curActivity.startActivity(intent);
		//加入退场动画
		if (inAnimId != -1 && outAnimId != -1) {
			curActivity.overridePendingTransition(inAnimId, outAnimId);
		}
        curActivity.finish();
	}
	
	/**
	 * 返回到上一个页面
	 * 
	 * @param curActivity
	 */
	public static void back(Activity curActivity) {
		back(curActivity, R.anim.in_from_left, R.anim.out_to_right);
	}

	/**
	 * 返回到上一个页面
	 * 
	 * @param curActivity
	 * @param inAnimId 入场动画
	 * @param outAnimId 退场动画
	 */
	public static void back(Activity curActivity, int inAnimId, int outAnimId) {
		curActivity.finish();
		//加入退场动画
		if (inAnimId != -1 && outAnimId != -1) {
			curActivity.overridePendingTransition(inAnimId, outAnimId);
		}
	}

	/**
	 * 返回到上一个页面并返回值
	 * 
	 * @param curActivity
	 * @param retCode
	 * @param retData
	 */
	public static void backWithResult(Activity curActivity, int retCode, Bundle retData) {
		backWithResult(curActivity, retCode, retData, R.anim.in_from_left, R.anim.out_to_right);
	}

	/**
	 * 返回到上一个页面并返回值
	 * 
	 * @param curActivity
	 * @param retCode
	 * @param retData
	 * @param inAnimId
	 * @param outAnimId
	 */
	public static void backWithResult(Activity curActivity, int retCode, Bundle retData, int inAnimId, int outAnimId) {
		Intent intent = new Intent();
		if (null != retData) {
			intent.putExtras(retData);
		}
		curActivity.setResult(retCode, intent);
		curActivity.finish();
		//加入退场动画
		if (inAnimId != -1 && outAnimId != -1) {
			curActivity.overridePendingTransition(inAnimId, outAnimId);
		}
	}

	public static void nextByFragment(Fragment fragment, Class<?> nextActivity, Bundle extras, int reqCode, int flag, int inAnimId, int outAnimId, boolean isFinish){
		Intent intent = new Intent(fragment.getActivity(), nextActivity);
		if (null != extras) {
			intent.putExtras(extras);
		}
		if (flag != -1) {
			intent.setFlags(flag);
		}
		//设置返回值模式
		if (reqCode < 0) {
			fragment.startActivity(intent);
		} else {
			fragment.startActivityForResult(intent, reqCode);
		}
		//加入转场动画
		if (inAnimId != -1 && outAnimId != -1) {
			fragment.getActivity().overridePendingTransition(inAnimId, outAnimId);
		}
		//是否销毁当前activity
		if (isFinish) {
			fragment.getActivity().finish();
		}
	}

}
