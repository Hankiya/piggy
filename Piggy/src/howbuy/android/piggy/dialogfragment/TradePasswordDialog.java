package howbuy.android.piggy.dialogfragment;

import howbuy.android.piggy.R;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class TradePasswordDialog extends DialogFragment {
	private static TradePasswordDialog aDialog;
	private EditText mEditText;
	private TextView mAmountText;

	private TradePasswordDialog() {
		// TODO Auto-generated constructor stub
	}

	public static final TradePasswordDialog newInstance(Bundle b) {
		if (null == aDialog) {
			aDialog = new TradePasswordDialog();
		}
		aDialog.setArguments(b);
		return aDialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.tarde_password_input, null);
		mEditText = (EditText) v.findViewById(R.id.pwd);
		mAmountText = (TextView) v.findViewById(R.id.amount);
		return v;
	}

//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		return new AlertDialog.Builder(getActivity()).setMessage("检测您已经是好买其他渠道的客户，现在激活储蓄罐？").setPositiveButton("确认", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(getActivity(), ActiveActivity.class);
//				intent.putExtras(getArguments());
//				startActivity(intent);
//				dialog.dismiss();
//			}
//		}).setNegativeButton("取消", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				dialog.dismiss();
//			}
//		}).create();
//	}

}
