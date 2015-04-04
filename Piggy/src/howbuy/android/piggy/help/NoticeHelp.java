package howbuy.android.piggy.help;

import howbuy.android.piggy.R;
import howbuy.android.piggy.UserUtil;
import howbuy.android.piggy.api.dto.NoticeDto;
import howbuy.android.piggy.api.dto.NoticeDtoList;
import howbuy.android.piggy.api.dto.UserInfoDto;
import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.piggy.service.ServiceMger.TaskBean;
import howbuy.android.piggy.service.UpdateUserDataService;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeHelp implements OnClickListener {
	// 1:首页、2：存钱页、3：取钱页、4：注册页（俩个页面）、5、登陆页、6、app已使用人数）
	public static final String NAME = "NoticeHelp";
	public static final String FILENAME_SAVE_KEY = "FILENAME_SAVE_KEY";

	public static final String Req_Type_UnLogin = "1";
	public static final String Req_Type_Login = "2";
	public static final String Req_Type_All = "3";

	public static final String Notice_ID_All = "0";
	public static final String Notice_ID_Main = "1";
	public static final String Notice_ID_Save = "2";
	public static final String Notice_ID_Out = "3";
	public static final String Notice_ID_Register = "4";
	public static final String Notice_ID_Login = "5";
	public static final String Notice_ID_UserCount = "6";
	public static final String Notice_ID_Activity = "7";
	public static final String Notice_Type_INFO = "1";
	public static final String Notice_Type_WARN = "2";
	public static final String Notice_Type_WARN_Hard = "3";

	private ImageView mCancleBtn;
	private TextView mMsgView;
	private View mRoot;

	public static class Util {

		/**
		 * 判断提醒信息的类别
		 * 
		 * @param nLiDtoList
		 * @param type
		 * @return
		 */
		public static NoticeDto getNotice(NoticeDtoList nLiDtoList, String type) {
			if (nLiDtoList.getListNotice() != null) {
				for (NoticeDto nd : nLiDtoList.getListNotice()) {
					if (nd.getTipId().equals(type)) {
						return nd;
					}
				}
			}
			return null;
		}

		/**
		 * 获取提醒信息
		 * 
		 * @param Req_Type
		 */
		public static void launcherNoticeTask(String reqType, String reqId) {
			TaskBean t = new TaskBean(UpdateUserDataService.TaskType_Notice, reqId);
			String[] params = new String[] { "", "", "", "" };
			// String custNo,String bankAcct,String bankCode,String tipCategory
			if (UserUtil.isLogin()) {
				params[0] = UserUtil.getUserNo();
				UserInfoDto userINfo = PiggyParams.getInstance().getUserInfo();
				if (userINfo != null) {
//					params[1] = userINfo.getBankAcct();
//					params[2] = userINfo.getBankCode();
					params[1]="";
					params[2]="";
					
					//to doo
				}
			}
			params[3] = reqType;
			t.setReqParams(params);
			ApplicationParams.getInstance().getServiceMger().addTask(t);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_btn:
			Log.d(NAME, "notice help onClick");
			mRoot.setVisibility(View.GONE);
			try {
				NoticeDto nd=(NoticeDto) v.getTag();
				if (nd!=null) {
					FileLineKeyValueHelp.getInstance(null).writeLine(FILENAME_SAVE_KEY+nd.getTipId()+nd.getTipVer(), nd.getTipVer());
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	
	public void findView(Activity context,ViewGroup v) {
		Log.d(NAME, "notice help findView");
		if (v!=null) {
			mRoot = v.findViewById(R.id.nofiy_lay);
		}else {
			mRoot = context.findViewById(R.id.nofiy_lay);
		}
		mCancleBtn = (ImageView) mRoot.findViewById(R.id.cancle_btn);
		mMsgView = (TextView) mRoot.findViewById(R.id.msg);
		mCancleBtn.setOnClickListener(this);
	}

	public void showOrHide(NoticeDto nd) {
		// TODO Auto-generated method stub
		if (nd == null||nd.getTipMsg()==null) {
			mRoot.setVisibility(View.GONE);
		} else {
			String saveValue=	FileLineKeyValueHelp.getInstance(null).checkLine(FILENAME_SAVE_KEY+nd.getTipId()+nd.getTipVer());
			if (TextUtils.isEmpty(saveValue)) {
				mCancleBtn.setTag(nd);
				mRoot.setVisibility(View.VISIBLE);
				mMsgView.setText(nd.getTipMsg());
				String ndType = nd.getTipType();
				if (ndType.equals(Notice_Type_INFO)) {
					mMsgView.setCompoundDrawables(null, null, null, null);
					mCancleBtn.setVisibility(View.VISIBLE);
				} else if (ndType.equals(Notice_Type_WARN)) {
					mMsgView.setCompoundDrawables(null, null, null, null);
					mCancleBtn.setVisibility(View.VISIBLE);
				} else if (ndType.equals(Notice_Type_WARN_Hard)) {
					mMsgView.setCompoundDrawablesWithIntrinsicBounds (mMsgView.getResources().getDrawable(R.drawable.ic_tan), null, null, null);
					mCancleBtn.setVisibility(View.GONE);
				}
			}
		}
	}

}
