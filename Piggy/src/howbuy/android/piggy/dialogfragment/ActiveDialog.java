package howbuy.android.piggy.dialogfragment;

import howbuy.android.piggy.ui.ActiveActivity;
import howbuy.android.util.Cons;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.umeng.analytics.MobclickAgent;

public class ActiveDialog extends DialogFragment {
	private static ActiveDialog aDialog;
	private Bundle mBundle;

	private ActiveDialog() {
		// TODO Auto-generated constructor stub
	}

	public static final ActiveDialog newInstance(Bundle b) {
		return  new ActiveDialog();
	}
	

	public Bundle getmBundle() {
		return mBundle;
	}

	public void setmBundle(Bundle mBundle) {
		this.mBundle = mBundle;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new AlertDialog.Builder(getActivity()).setMessage("您已经是好买其他渠道的客户，是否激活储蓄罐？").setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ActiveActivity.class);
				intent.putExtras(getmBundle());
				startActivity(intent);
				MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Login, "激活");
				ActiveDialog.this.dismiss();
			}
		}).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MobclickAgent.onEvent(getActivity(), Cons.EVENT_UI_Login, "取消激活");
				ActiveDialog.this.dismiss();
			}
		}).create();
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);
		System.out.println("dissmiss");
	}
	
}
