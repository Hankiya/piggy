package howbuy.android.piggy.dialogfragment;

import howbuy.android.piggy.api.dto.UpdateDto;
import howbuy.android.util.Cons;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;

public class UpdateDialog extends DialogFragment implements android.content.DialogInterface.OnKeyListener {
	private static UpdateDialog aDialog;
	private UpdateDto mUpdateDto;
	private Boolean need;

	private UpdateDialog() {
		// TODO Auto-generated constructor stub
	}

	public static final UpdateDialog newInstance(Bundle b) {
		if (null == aDialog) {
			aDialog = new UpdateDialog();
		}
		aDialog.setArguments(b);
		return aDialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mUpdateDto = getArguments().getParcelable(Cons.Intent_bean);
		if (mUpdateDto == null) {
			return super.onCreateDialog(savedInstanceState);
		}
		String needUpdate = mUpdateDto.getVersionNeedUpdate();
		String desc = mUpdateDto.getUpdateDesc();
		final String url = mUpdateDto.getUpdateUrl();
		need = needUpdate.equals("1");
		String exitBtnText = need ? "退出" : "取消";
		Builder builder = new Builder(getActivity()).setTitle("检测到有新版本").setMessage(desc);
		builder.setPositiveButton("确认更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent("android.intent.action.VIEW");
				i.setData(Uri.parse(url));
				startActivity(i);
				if (need) {
					getActivity().finish();
				}
			}
		});

		builder.setNegativeButton(exitBtnText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (need) {
					getActivity().finish();
				}
			}
		});

		Dialog d = builder.create();
		d.setOnKeyListener(UpdateDialog.this);
		if (need) {
			builder.setCancelable(false);
			d.setCancelable(false);
			d.setCanceledOnTouchOutside(false);
		}
		return d;

	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (need != null && need) {
				getActivity().finish();
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

}
