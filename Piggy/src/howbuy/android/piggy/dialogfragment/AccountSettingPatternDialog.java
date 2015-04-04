package howbuy.android.piggy.dialogfragment;

import howbuy.android.piggy.ui.LockSetActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AccountSettingPatternDialog extends DialogFragment {
	private static AccountSettingPatternDialog aDialog;

	private AccountSettingPatternDialog() {
		// TODO Auto-generated constructor stub
	}

	public static final AccountSettingPatternDialog newInstance(Bundle b) {
		if (null == aDialog) {
			aDialog = new AccountSettingPatternDialog();
		}
		aDialog.setArguments(b);
		return aDialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new AlertDialog.Builder(getActivity()).setMessage("检测您还从未设置过手势密码，现在设置？").setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), LockSetActivity.class);
				startActivity(intent);
				dialog.dismiss();
			}
		}).setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).create();
	}

}
