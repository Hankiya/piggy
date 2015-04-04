package howbuy.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 信托产品
 * 
 * @author yescpu
 * 
 */
public class TrustBean extends ItemBean implements Parcelable {
	private String pid;
	/**
	 * 产品名称
	 */
	private String cpmc;
	/**
	 * 销售状态
	 */
	private String xszt;
	/**
	 * 产品年限
	 */
	private String cpnx;
	/**
	 * 预期收益
	 */
	private String yqsy;
	/**
	 * 好买点评
	 */
	private String hmdp;
	/**
	 * 产品卖点
	 */
	private String cpmd;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCpmc() {
		return cpmc;
	}

	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}

	public String getXszt() {
		return xszt;
	}

	public void setXszt(String xszt) {
		this.xszt = xszt;
	}

	public String getCpnx() {
		return cpnx;
	}

	public void setCpnx(String cpnx) {
		this.cpnx = cpnx;
	}

	public String getYqsy() {
		return yqsy;
	}

	public void setYqsy(String yqsy) {
		this.yqsy = yqsy;
	}

	public String getHmdp() {
		return hmdp;
	}

	public void setHmdp(String hmdp) {
		this.hmdp = hmdp;
	}

	public String getCpmd() {
		return cpmd;
	}

	public void setCpmd(String cpmd) {
		this.cpmd = cpmd;
	}

	@Override
	public String toString() {
		return "TrustBean [pid=" + pid + ", cpmc=" + cpmc + ", xszt=" + xszt + ", cpnx=" + cpnx + ", yqsy=" + yqsy + ", hmdp=" + hmdp + ", cpmd=" + cpmd + "]";
	}

	@Override
	public String getmClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(pid);
		dest.writeString(cpmc);
		dest.writeString(xszt);
		dest.writeString(cpnx);
		dest.writeString(yqsy);
		dest.writeString(hmdp);
		dest.writeString(cpmd);
		dest.writeInt(isNeedReload ? 0 : 1);
	}

	public static final Creator<TrustBean> CREATOR = new Creator<TrustBean>() {

		@Override
		public TrustBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TrustBean _bBean = new TrustBean();
			_bBean.pid = source.readString();
			_bBean.cpmc = source.readString();
			_bBean.xszt = source.readString();
			_bBean.cpnx = source.readString();
			_bBean.yqsy = source.readString();
			_bBean.hmdp = source.readString();
			_bBean.cpmd = source.readString();
			_bBean.isNeedReload = source.readInt() == 0;
			return _bBean;
		}

		@Override
		public TrustBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TrustBean[size];
		}

	};
}
