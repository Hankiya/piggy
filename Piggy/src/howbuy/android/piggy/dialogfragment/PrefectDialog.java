package howbuy.android.piggy.dialogfragment;

import howbuy.android.piggy.ui.PrefectActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class PrefectDialog extends DialogFragment {
	private static PrefectDialog aDialog;

	private PrefectDialog() {
		// TODO Auto-generated constructor stub
	}

	public static final PrefectDialog newInstance(Bundle b) {
		if (null == aDialog) {
			aDialog = new PrefectDialog();
		}
		aDialog.setArguments(b);
		return aDialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new AlertDialog.Builder(getActivity()).setMessage("手机储蓄罐未鉴权状态当日申购金额不得大于5000元，现在去鉴权？").setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), PrefectActivity.class);
//				intent.putExtras(getArguments());
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
