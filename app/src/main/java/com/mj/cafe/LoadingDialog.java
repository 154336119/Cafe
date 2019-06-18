package com.mj.cafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LoadingDialog extends ProgressDialog {

	private TextView content;
	private String message;
	private Button btnRight;
	public LoadingDialog(Context context) {
		super(context, R.style.Dialog);
	}

	public LoadingDialog(Context context, String message) {
		super(context, R.style.Dialog);
		this.message = message;
	}

	public LoadingDialog(Context context, int theme, String message) {
		super(context, theme);
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_view_tips_loading);
		setCanceledOnTouchOutside(false);
		setInverseBackgroundForced(false);
		content = (TextView) findViewById(R.id.tips_msg);
		btnRight = (Button) findViewById(R.id.btn_right);
		setText(message);
		//setOnKeyListener(keyListener);
	}

	public void setText(String message) {
		if (content == null) {
			this.message = message;
			return;
		}

		if (TextUtils.isEmpty(message)) {
			content.setVisibility(View.GONE);
		}else{
			content.setVisibility(View.VISIBLE);
			content.setText(message);
		}
	}

	public void setText(int resId) {
		setText(getContext().getResources().getString(resId));
	}

	public void setOnRightBtnListener(View.OnClickListener onClickListener){
		btnRight.setOnClickListener(onClickListener);
	}
//	OnKeyListener keyListener=new OnKeyListener() {
//		@Override
//		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//			iDialogInterface.dialogLifecycleCancel();
//			return false;
//		}
//	};
//	private IDialogInterface iDialogInterface;
//	public interface IDialogInterface{
//		void dialogLifecycleCancel();
//	}
//	public void setDialogInterface(IDialogInterface iDialogInterface){
//		this.iDialogInterface=iDialogInterface;
//	}
}