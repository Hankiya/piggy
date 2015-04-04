package com.howbuy.aty;

import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.view.View;

import com.howbuy.config.ValConfig;
import com.howbuy.lib.aty.AbsSfAty;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.libtest.R;

public class AtySecret extends AbsSfAty{
	@Override
	protected int getPreferencesFromResourceId() {
		return R.xml.xml_settings_secret;
	}

	@Override
	protected String getPreferencesFromResourceName() {
		return ValConfig.SECRET_SF_NAME;
	} 
 
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(preference instanceof ListPreference){
			preference.setSummary(newValue+"");
			View v=getListView().getSelectedView();
			getListView().requestLayout();
		} 
		return true;
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key=preference.getKey();
		if(preference instanceof CheckBoxPreference){
			CheckBoxPreference box=(CheckBoxPreference) preference;
			boolean isChecked=box.isChecked();
			if(ValConfig.SECRET_DEBUG_URL.equals(key)){
				LogUtils.mDebugUrl=isChecked;
				pop("debug url is "+isChecked,true);
			}else  if(ValConfig.SECRET_DEBUG_LOG.equals(key)){
				LogUtils.mDebugLog=isChecked;
				pop("debug log is "+isChecked,true);
			}else  if(ValConfig.SECRET_DEBUG_LOG_FILE.equals(key)){
				LogUtils.mDebugLogFile=isChecked;
				pop("debug log file is "+isChecked,true);
			}else  if(ValConfig.SECRET_DEBUG_POP.equals(key)){
				LogUtils.mDebugPop=isChecked;
				pop("debug pop is "+isChecked,true);
			}else  if(ValConfig.SECRET_DEBUG_CRASH_MUTIFILE.equals(key)){
				LogUtils.mDebugCrashMutiLogFile=isChecked;
				pop("debug muti crash file is "+isChecked,true);
			}else  if(ValConfig.SECRET_DEBUG_CRASH_DIALOG.equals(key)){
				LogUtils.mDebugCrashDialog=isChecked;
				pop("debug crash show dialog is "+isChecked+" next time works.",true);
			}else  if(ValConfig.SECRET_DEBUG_CRASH_LAUNCH.equals(key)){
				LogUtils.mDebugCrashLaunch=isChecked;
				pop("debug crash launch self is "+isChecked,true);
			}
		}
		return true;
	}
	
	@Override
	protected void onAbsBuildActionBar() {
		buildActionBarSimple();
	}
	@Override
	public void onXmlBtClick(View v) {
		 
	}

	@Override
	public boolean onNetChanged(int netType, int preType) {
		// TODO Auto-generated method stub
		return false;
	}
 
 
}
