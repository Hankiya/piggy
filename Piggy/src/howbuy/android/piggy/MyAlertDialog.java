package howbuy.android.piggy;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.ui.LoginActivity;
import howbuy.android.piggy.ui.fragment.LoginFragment;
import howbuy.android.util.Cons;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.IntentCompat;
import android.view.KeyEvent;

public class MyAlertDialog extends DialogFragment {

	private static MyAlertDialog aDialog;

	private MyAlertDialog() {
		// TODO Auto-generated constructor stub
	}

	public static final MyAlertDialog newInstance(Bundle b) {
		if (null == aDialog) {
			aDialog = new MyAlertDialog();
		}
		aDialog.setArguments(b);
		return aDialog;
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new AlertDialog.Builder(getActivity()).setMessage("    账号登录异常，请重新登录").setPositiveButton("重新登录", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Intent ien = new Intent(getActivity(), LoginActivity.class);
//				ien.putExtra(Cons.Intent_type, LoginFragment.LoginType_Main);
//				startActivity(ien);
//				dialog.dismiss();
				
				ApplicationParams.getInstance().getsF().edit().remove(Cons.SFUserCusNo).remove(Cons.SFPatternValue).remove(Cons.SFPatternFlag).commit();
				ApplicationParams.getInstance().getPiggyParameter().removeUserDataPrivate();
				ApplicationParams.getInstance().getsF().edit().putBoolean(Cons.SFLoginOutFlag, true).commit();
				Intent intent2 = new Intent(getActivity(), LoginActivity.class);
				intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
				intent2.putExtra(Cons.Intent_type, LoginFragment.LoginType_Setting);
				startActivity(intent2);
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
					ApplicationParams.getInstance().clearActivityTask();
				}
				
			}
		}).setOnKeyListener(keylistener).setCancelable(false).create();
	}
	
		public boolean dispatchKeyEvent(KeyEvent event) {
				 switch(event.getKeyCode())
				 {
				  case KeyEvent.KEYCODE_BACK:
				  case KeyEvent.KEYCODE_HOME:
				  case KeyEvent.KEYCODE_MENU:
				  {
				     return true;
				  }
				 }
				 return false;
			};
	
		OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
	        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	            if (keyCode==KeyEvent.KEYCODE_BACK || keyCode==KeyEvent.KEYCODE_MENU || keyCode==KeyEvent.KEYCODE_HOME)
	            {
	             return true;
	            }
	            else
	            {
	             return false;
	            }
	        }
	    } ;
}
