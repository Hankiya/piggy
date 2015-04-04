package howbuy.android.piggy.dialogfragment;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * 手机号码选择
 * @author Administrator
 *
 */
public class DialogSelectFragment extends DialogFragment {
	private static int mType;
	private static SpinnerSelect iSelect;
	private static String xxCode[];

	public interface SpinnerSelect {
		public void onSelect(int mType, String value, int index);
	}

	public static DialogSelectFragment newInstance(int type, List<String> s, SpinnerSelect select) {
		DialogSelectFragment fragment = new DialogSelectFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		mType = type;
		iSelect = select;
		xxCode = ChangeData(s);
		return fragment;
	}

	public static String[] ChangeData(List<String> s) {
		String[] sArray = new String[s.size()];
		for (int i = 0; i < s.size(); i++) {
			sArray[i] = s.get(i);
		}
		return sArray;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String dtitle = "请选择手机号码";
		return new AlertDialog.Builder(getActivity()).setTitle(dtitle).setItems(xxCode, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				iSelect.onSelect(mType, xxCode[which], which);
			}
		}).create();
	}

}
