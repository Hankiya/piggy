package com.howbuy.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.howbuy.db.DbUtils;
import com.howbuy.db.DbUtils.SqlExeObj;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.error.WrapException;
import com.howbuy.lib.utils.StreamUtils;

public class UserInf implements Parcelable, Serializable {
	private static String KEY_USER_CURRENT = "KEY_USER_CURRENT";
	private static final long serialVersionUID = 1L;
	private String UserName;
	private String Password; // 加密后的.
	private String CustNo;
	private long LoginTime = 0;
	private int State = 0;
	private String UserPhone;
	private static UserInf mUser = null;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getCustNo() {
		return CustNo;
	}

	public void setCustNo(String custNo) {
		CustNo = custNo;
	}

	public long getLoginTime() {
		return LoginTime;
	}

	public void setLoginTime(long loginTime) {
		LoginTime = loginTime;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}
	
	

	public String getUserPhone() {
		return UserPhone;
	}

	public void setUserPhone(String userPhone) {
		UserPhone = userPhone;
	}

	public static UserInf getUser() {
		if (mUser == null) {
			HashMap<String, Object> map = GlobalApp.getApp().getMapObj();
			if (map.containsKey(KEY_USER_CURRENT)) {
				mUser = (UserInf) map.get(KEY_USER_CURRENT);
			} else {
				mUser = new UserInf(null);
				map.put(KEY_USER_CURRENT, mUser);
			}
		}
		return mUser;
	}

	public boolean isLogined() {
		return State == 1;
	}

	public void loginOut() {
		State = 0;
	}

	public void loginIn(String username, String password, String custNo) {
		UserName = username;
		Password = password;
		CustNo = custNo;
		LoginTime = System.currentTimeMillis();
		State = 1;
	}

	public void loginIn(UserInf u) {
		if (u != null) {
			UserName = u.UserName;
			Password = u.Password;
			CustNo = u.CustNo;
			LoginTime = u.LoginTime;
			State = u.State;
			State = 1;
			LoginTime = System.currentTimeMillis();
			UserPhone=u.UserPhone;
		}
	}

	public UserInf(String userName, String password, String custNo, long loginTime, int state,String userPhone) {
		UserName = userName;
		UserPhone=userPhone;
		Password = password;
		CustNo = custNo;
		LoginTime = loginTime;
		State = state;
	}

	public UserInf(UserInf u) {
		loginIn(u);
	}

	public static byte[] toBtye(UserInf user) throws Exception {
		return StreamUtils.toBytes(user);
	}

	public static Object toObj(byte[] b) throws Exception {
		return StreamUtils.toObject(b);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(UserName);
		dest.writeString(Password);
		dest.writeString(CustNo);
		dest.writeLong(LoginTime);
		dest.writeInt(State);
		dest.writeString(UserPhone);

	}

	public static final Creator<UserInf> CREATOR = new Creator<UserInf>() {
		@Override
		public UserInf createFromParcel(Parcel s) {
			UserInf u = new UserInf(s.readString(), s.readString(), s.readString(), s.readLong(),
					s.readInt(),s.readString());
			return u;
		}

		@Override
		public UserInf[] newArray(int size) {
			return new UserInf[size];
		}
	};

	public static ArrayList<UserInf> load(boolean all) throws WrapException {
		ArrayList<UserInf> r = new ArrayList<UserInf>();
		StringBuilder sb = new StringBuilder();
		sb.append("select key,subkey,value,state,date from tb_common where key='user_login'");
		if (!all) {
			sb.append(" and state=1");
		}
		sb.append("  order by date desc");
		Cursor c = DbUtils.query(sb.toString(), null, false);
		if (c != null && c.moveToFirst()) {
			try {
				do {
					r.add((UserInf) UserInf.toObj(c.getBlob(2)));
				} while (c.moveToNext());
			} catch (Exception e) {
				throw WrapException.wrap(e, "");
			}
		}
		return r;
	}

	public WrapException save() throws Exception {
		String slq = "insert or replace into tb_common(key,subkey,value,state,date) values('user_login',?,?,?,?)";
		return DbUtils.exeSql(new SqlExeObj(slq, new Object[] { UserName, toBtye(this), State,
				LoginTime }), true);
	}

	@Override
	public String toString() {
		return "UserInf [UserName=" + UserName + ", Password=" + Password + ", CustNo=" + CustNo
				+ ", LoginTime=" + LoginTime + ", State=" + State + "]";
	}
}
