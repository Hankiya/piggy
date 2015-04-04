package howbuy.android.piggy.dialogfragment;

import howbuy.android.bean.DialogBean;
import howbuy.android.bean.DialogBean.DialogBeanBuilder;
import howbuy.android.piggy.R;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.piggy.widget.ClearableEdittext;
import howbuy.android.util.Cons;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;

/**
 * 密码输入框
 * @author Administrator
 *
 */
public class LoginVerifyDialog extends DialogFragment {

	/**
	 * 输入框输入完
	 * @author Administrator
	 *
	 */
	public interface InputCallBack {
		public void onCallBackPwd(String pwd,String type,boolean isCancel);
	}

	;
	private ClearableEdittext mEditText;
	private InputCallBack mBack;
	private String callbackType;

	public InputCallBack getmBack() {
		return mBack;
	}

	public void setmBack(InputCallBack mBack) {
		this.mBack = mBack;
	}



	public static final LoginVerifyDialog newInstance(Bundle b) {
        LoginVerifyDialog aDialog= new LoginVerifyDialog();
		aDialog.setArguments(b);
		return aDialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		PiggyParams p = ApplicationParams.getInstance().getPiggyParameter();
		View inputLay = LayoutInflater.from(getActivity()).inflate(R.layout.login_password_input, null);
		mEditText = (ClearableEdittext) inputLay.findViewById(R.id.pwd);
        mEditText.setClearType(ClearableEdittext.TypePas);
        
        DialogBean db=null;
		if (getArguments()!=null) {
            db=getArguments().getParcelable(Cons.Intent_bean);
			callbackType= db.getDialogId();
		}
		callbackType=db.getDialogId();
		int inputType=0;
		if (db.getPwdType()==DialogBeanBuilder.PwdType_Trade) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
			} else {
				inputType = InputType.TYPE_CLASS_NUMBER;
				mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
		}else {
			inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
		}
		mEditText.setInputType(inputType);
		
        mEditText.setClearType(ClearableEdittext.TypePas);
        mEditText.setHint(db.getInputHint());

		AlertDialog  a=new AlertDialog.Builder(getActivity()).setView(inputLay).setPositiveButton(db.getSureBtnMsg(), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				String pwdValue = mEditText.getText().toString();
				if (mBack != null && !TextUtils.isEmpty(pwdValue)) {
					mBack.onCallBackPwd(pwdValue,callbackType,false);
				}
			}
		}).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				getDialog().dismiss();
				mBack.onCallBackPwd("",callbackType,true);
			}
		}).create();
		
		if (!TextUtils.isEmpty(db.getTitle())) {
			a.setTitle(db.getTitle());
		}
		return a;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
		// 弹出键盘
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		getDialog().setCancelable(false);
		getDialog().setCanceledOnTouchOutside(false);
	}
}
