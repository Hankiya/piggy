package com.howbuy.aty;

import howbuy.android.palmfund.R;

import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.view.View;
import android.widget.ArrayAdapter;
import cn.jpush.android.api.JPushInterface;

import com.howbuy.commonlib.ParCheckAppUpdate;
import com.howbuy.component.AppFrame;
import com.howbuy.component.AppService;
import com.howbuy.config.Analytics;
import com.howbuy.config.SelfConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.entity.UserInf;
import com.howbuy.frag.FragSetAbout;
import com.howbuy.frag.FragSetFeedback;
import com.howbuy.frag.FragSetLogin;
import com.howbuy.frag.FragSetRecommend;
import com.howbuy.frag.FragSetSubscribe;
import com.howbuy.lib.aty.AbsAty;
import com.howbuy.lib.aty.AbsSfAty;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.utils.Receiver;
import com.howbuy.utils.ShareHelper;
import com.howbuy.wireless.entity.protobuf.HostDistributionProtos.HostDistribution;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;

public class AtySettings extends AbsSfAty implements IReqNetFinished {
	public static final String Sina_SCREEN_NAME = "screen_name";
	UserInf mUser = null;
	UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login",
			RequestType.SOCIAL);
	String sinaNickName;
	ProgressDialog mDialog;

	private void showCheckUpdateDlg(boolean show) {
		if (show) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(this);
				mDialog.setMessage("正在检查更新...");
			}
			if (!mDialog.isShowing()) {
				mDialog.show();
			}
		} else {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUser = UserInf.getUser();
		String version = SysUtils.getVersionName(getApplicationContext());
		findPreference(ValConfig.SET_CHECKUPDATE).setSummary(
				getString(R.string.set_summary_checkupdate, version));
		getPlatformInfo();
	}

	private void getPlatformInfo() {
		mController.getPlatformInfo(AtySettings.this, SHARE_MEDIA.SINA, new UMDataListener() {

			@Override
			public void onStart() {
				// Toast.makeText(AtySettings.this, "获取平台数据开始...",
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				if (status == 200 && info != null) {
					Set<String> keys = info.keySet();
					for (String key : keys) {
						if (key.equals(Sina_SCREEN_NAME)) {
							sinaNickName = info.get(Sina_SCREEN_NAME).toString();
							findPreference(ValConfig.SET_BAND_SINA).setSummary(sinaNickName);
						}
					}
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		String login = mUser.isLogined() ? mUser.getUserName()
				: getString(R.string.set_summary_account);
		findPreference(ValConfig.SET_ACCOUNT).setSummary(login);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected int getPreferencesFromResourceId() {
		return R.xml.xml_settings;
	}

	@Override
	protected String getPreferencesFromResourceName() {
		return ValConfig.SET_SF_NAME;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		String title = preference.getTitle().toString();
		if (ValConfig.SET_ACCOUNT.equals(key)) {
			if (mUser.isLogined()) {
				showLoginedDlg();
			} else {
				launchItem(title, FragSetLogin.class.getName());
			}
		} else if (ValConfig.SET_SUBSCRIBE.equals(key)) {
			launchItem(title, FragSetSubscribe.class.getName());
		} else if (ValConfig.SET_FEEDBACK.equals(key)) {
			launchItem(title, FragSetFeedback.class.getName());
		} else if (ValConfig.SET_RECOMMEND.equals(key)) {
			launchItem(title, FragSetRecommend.class.getName());
		} else if (ValConfig.SET_CHECKUPDATE.equals(key)) {
			showCheckUpdateDlg(true);
			new ParCheckAppUpdate(1, this).execute();
		} else if (ValConfig.SET_APP_MARKET.equals(key)) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ValConfig.URL_APP_MARKET)));
		} else if (ValConfig.SET_APP_CHUXUGUAN.equals(key)) {
			launchView(title, ValConfig.URL_APP_CHUXUGUAN, "howbuy.android.piggy");
		} else if (ValConfig.SET_APP_XINTUO.equals(key)) {
			launchView(title, ValConfig.URL_APP_XINTUO, "howbuy.android.trustcollection");
		} else if (ValConfig.SET_ATTENTION_WX.equals(key)) {
			IWXAPI api = ShareHelper.createWXAPI(this);
			if (api.isWXAppInstalled()) {
				SysUtils.copyText(getApplicationContext(), "好买财富");
				if (api.openWXApp()) {
					pop("“好买财富”已复制到剪贴板", false);
					Analytics.onEvent(this, Analytics.MORE_APP, Analytics.KEY_FOR, "添加公众号");
				} else {
					pop("请去微信搜索“好买财富”", false);
				}
			}
		} else if (ValConfig.SET_ABOUT.equals(key)) {
			launchItem(title, FragSetAbout.class.getName());
		} else if (ValConfig.SET_PUSH.equals(key)) {
			CheckBoxPreference boxPreference = (CheckBoxPreference) preference;
			boolean isCheck = boxPreference.isChecked();
			if (isCheck) {
				JPushInterface.resumePush(getApplicationContext());
				AppFrame.getApp().getsF().edit().putBoolean(ValConfig.sFSettingPush, true).commit();
			} else {
				JPushInterface.stopPush(getApplicationContext());
				AppFrame.getApp().getsF().edit().putBoolean(ValConfig.sFSettingPush, false)
						.commit();
			}
		} else if (ValConfig.SET_BAND_SINA.equals(key)) {
			if (sinaNickName == null) {
				mController.doOauthVerify(AtySettings.this, SHARE_MEDIA.SINA, new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA arg0) {
					}

					@Override
					public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
					}

					@Override
					public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
						System.out.println(arg0);
						getPlatformInfo();

					}

					@Override
					public void onCancel(SHARE_MEDIA arg0) {

					}
				});
			} else {
				loginSinaOut();

			}
		}
		return true;
	}

	private void launchView(String title, String url, String pk) {
		Intent t = getPackageManager().getLaunchIntentForPackage(pk);
		if (t != null) {
			startActivity(t);
		} else {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			Analytics.onEvent(this, Analytics.MORE_APP, Analytics.KEY_FOR, title);
		}
	}

	private void launchItem(String title, String fragName) {
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, title);
		Intent t = new Intent(this, AtyEmpty.class);
		t.putExtra(AtyEmpty.KEY_FRAG_NAME, fragName);
		t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
		startActivity(t);
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
		return false;
	}

	/**
	 * 更新提示
	 * 
	 * @param code
	 */
	private void showUpdateDelaig(final boolean force, final String desc, final String url) {
		Builder builder = new Builder(this).setTitle("版本更新").setMessage(desc)
				.setIcon(android.R.drawable.ic_dialog_info);
		builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent("android.intent.action.VIEW");
				i.setData(Uri.parse(url));
				startActivity(i);
				if (force) {
					AbsAty.exitApp(false);
				}
			}
		});
		builder.setNegativeButton(force ? "退出" : "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (force) {
					AbsAty.exitApp(false);
				} else {
					dialog.dismiss();
				}
			}
		});
		builder.create().show();
	}

	private void switchAccount() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("切换账号");
		builder.setMessage("切换账号将会删除您所有的本地自选数据，您可以再次登录云账号将其恢复。");
		builder.setPositiveButton("删除", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try {
					UserInf user = UserInf.getUser();
					user.loginOut();
					user.save();
					resetOptRecord();
					AppFrame.getApp().getsF().edit().remove(ValConfig.SFOPTUserClose).commit();
				} catch (Exception e) {
					e.printStackTrace();
				}

				launchItem(getString(R.string.set_title_account), FragSetLogin.class.getName());

			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();
	}

	private void loginOut() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("注销");
		builder.setMessage("注销将会删除您所有的本地自选数据，您可以再次登录云账号将其恢复。");
		builder.setPositiveButton("删除", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try {
					mUser.loginOut();
					mUser.save();
					resetOptRecord();
					AppFrame.getApp().getsF().edit().remove(ValConfig.SFOPTUserClose).commit();
					GlobalApp
							.getApp()
							.getGlobalServiceMger()
							.executeTask(
									new ReqOpt(0, "your key arg", AppService.HAND_SYNC_OPTIONAL),
									null);
					findPreference(ValConfig.SET_ACCOUNT).setSummary(
							getString(R.string.set_summary_account));
				} catch (Exception e) {
					e.printStackTrace();
				}// login out current.
			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();
	}

	private void loginSinaOut() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("注销");
		builder.setMessage("是否注销新浪微博");
		builder.setPositiveButton("注销", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try {
					mController.deleteOauth(AtySettings.this, SHARE_MEDIA.SINA,
							new SocializeClientListener() {
								@Override
								public void onStart() {
								}

								@Override
								public void onComplete(int arg0, SocializeEntity arg1) {
									// TODO Auto-generated method stub
									findPreference(ValConfig.SET_BAND_SINA).setSummary("未绑定");
									sinaNickName = null;
								}
							});
				} catch (Exception e) {
					e.printStackTrace();
				}// login out current.
			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();
	}

	private void showLoginedDlg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(mUser.getUserName());
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] { "切换账号", "注销" });
		builder.setAdapter(adp, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					switchAccount();
				} else {
					loginOut();
				}
			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();
	}

	/**
	 * reset local opt record
	 */
	private final void resetOptRecord() {
		String sql = "update fundsinfo set xuan=-1 where xuan in(" + SelfConfig.UNSynsAdd + ","
				+ SelfConfig.UNSynsDel + "," + SelfConfig.SynsShowAdd + ")";
		DbUtils.exeSql(new SqlExeObj(sql), false);
		AppFrame.getApp().getLocalBroadcast().sendBroadcast(Receiver.FROM_OPTIONAL_SYNC, null);
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		showCheckUpdateDlg(false);
		HostDistribution startData = (HostDistribution) (result.isSuccess() ? result.mData : null);
		if (startData != null) {
			String code = startData.getVersionNeedUpdate();
			boolean isUpdate = AppFrame.getApp().getsF()
					.getBoolean(ValConfig.SFSystemNeedUpadate, true);
			if (isUpdate) {
				if (code.equals("2") == false) {
					showUpdateDelaig("1".equals(code), startData.getUpdateDesc(),
							startData.getUpdateUrl());
				} else {
					pop("暂无更新！", false);
				}
			}
		} else {
			pop("没有检测到更新", false);
		}
	}

}
