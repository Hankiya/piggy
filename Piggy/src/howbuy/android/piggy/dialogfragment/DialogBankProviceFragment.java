package howbuy.android.piggy.dialogfragment;

import howbuy.android.piggy.api.dto.ProvinceInfoDto;
import howbuy.android.piggy.api.dto.SupportBankAndProvinceDto;
import howbuy.android.piggy.api.dto.SupportBankDto;
import howbuy.android.piggy.ui.fragment.BindCardFragment;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogBankProviceFragment extends DialogFragment {
	private static SupportBankAndProvinceDto mDto;
	private static int mType;// 1为银行，2为省份
	private static SpinnerSelect iSelect;
	private String xxCode[];

	public interface SpinnerSelect {
		public void onSelect(int mType, String bankAndProviceCode, String bankAndProvinceName);
	}

	public static DialogBankProviceFragment newInstance(int type, SupportBankAndProvinceDto sdto, SpinnerSelect select) {
		DialogBankProviceFragment fragment = new DialogBankProviceFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		mDto = sdto;
		mType = type;
		iSelect = select;
		return fragment;
	}

	public String[] ChangeData(int type, SupportBankAndProvinceDto sdto) {
		String[] names = null;
		if (type == 1) {
			List<SupportBankDto> listmp=sdto.getsBankDto();
			names = new String[listmp.size()];
			xxCode = new String[listmp.size()];
			for (int i= 0; i < listmp.size(); i++) {
				SupportBankDto supportBankDto =listmp.get(i);
				names[i] = supportBankDto.getBankName();
				xxCode[i] = supportBankDto.getCode();
			}
		} else {
			
			List<ProvinceInfoDto> listmp=sdto.getsProvince();
			names = new String[listmp.size()];
			xxCode = new String[listmp.size()];
			for (int i = 0; i < listmp.size(); i++) {
				ProvinceInfoDto supportBankDto =listmp.get(i);
				names[i] = supportBankDto.getName();
				xxCode[i] = supportBankDto.getCode();
			}
		}
		return names;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String dtitle = mType == BindCardFragment.typeBank ? "请选择开户银行" : "请选择开户省份";
		final String[] data = ChangeData(mType, mDto);
		return new AlertDialog.Builder(getActivity()).setTitle(dtitle).setItems(data, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				iSelect.onSelect(mType, data[which],xxCode[which]);
			}
		}).create();

		// return new
		// AlertDialog.Builder(getActivity()).setIcon(R.drawable.ic_launcher).setTitle(title).setPositiveButton("OK",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int whichButton) {
		// // ((DialogFragmentExampleActivity)
		// // getActivity()).doPositiveClick();
		// }
		// }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int whichButton) {
		// // ((DialogFragmentExampleActivity)
		// // getActivity()).doNegativeClick();
		// }
		// }).create();
	}

}
