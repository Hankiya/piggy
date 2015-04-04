package howbuy.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 信托详情
 * 
 * @author Administrator
 * 
 */
public class TrustsDetailBean extends ItemBean implements Parcelable {

	@Override
	public String getmClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	private String productList;
	private String pid;
	private String cpmc;
	private String xszt;
	private String cpqxCode;
	private String yqsyCode;
	private String qszjCode;
	private String hmdp;
	private String cpjs;
	private String clrq;
	private String fsrq;
	private String tzfxCode;
	private String fxkz;
	private String dycp;
	private String cpsm;
	private String gsmc;
	private String cid;
	private String gsbjCode;
	private String szd;
	private String cdpjc;
	private String xtfl;

	public String getProductList() {
		return productList;
	}

	public void setProductList(String productList) {
		this.productList = productList;
	}

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

	public String getCpqxCode() {
		return cpqxCode;
	}

	public void setCpqxCode(String cpqxCode) {
		this.cpqxCode = cpqxCode;
	}

	public String getYqsyCode() {
		return yqsyCode;
	}

	public void setYqsyCode(String yqsyCode) {
		this.yqsyCode = yqsyCode;
	}

	public String getQszjCode() {
		return qszjCode;
	}

	public void setQszjCode(String qszjCode) {
		this.qszjCode = qszjCode;
	}

	public String getHmdp() {
		return hmdp;
	}

	public void setHmdp(String hmdp) {
		this.hmdp = hmdp;
	}

	public String getCpjs() {
		return cpjs;
	}

	public void setCpjs(String cpjs) {
		this.cpjs = cpjs;
	}

	public String getClrq() {
		return clrq;
	}

	public void setClrq(String clrq) {
		this.clrq = clrq;
	}

	public String getFsrq() {
		return fsrq;
	}

	public void setFsrq(String fsrq) {
		this.fsrq = fsrq;
	}

	public String getTzfxCode() {
		return tzfxCode;
	}

	public void setTzfxCode(String tzfxCode) {
		this.tzfxCode = tzfxCode;
	}

	public String getFxkz() {
		return fxkz;
	}

	public void setFxkz(String fxkz) {
		this.fxkz = fxkz;
	}

	public String getDycp() {
		return dycp;
	}

	public void setDycp(String dycp) {
		this.dycp = dycp;
	}

	public String getCpsm() {
		return cpsm;
	}

	public void setCpsm(String cpsm) {
		this.cpsm = cpsm;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getGsbjCode() {
		return gsbjCode;
	}

	public void setGsbjCode(String gsbjCode) {
		this.gsbjCode = gsbjCode;
	}

	public String getSzd() {
		return szd;
	}

	public void setSzd(String szd) {
		this.szd = szd;
	}

	public String getCdpjc() {
		return cdpjc;
	}

	public void setCdpjc(String cdpjc) {
		this.cdpjc = cdpjc;
	}

	public String getXtfl() {
		return xtfl;
	}

	public void setXtfl(String xtfl) {
		this.xtfl = xtfl;
	}

	@Override
	public String toString() {
		return "TrustsDetail [productList=" + productList + ", pid=" + pid + ", cpmc=" + cpmc + ", xszt=" + xszt + ", cpqxCode=" + cpqxCode + ", yqsyCode=" + yqsyCode
				+ ", qszjCode=" + qszjCode + ", hmdp=" + hmdp + ", cpjs=" + cpjs + ", clrq=" + clrq + ", fsrq=" + fsrq + ", tzfxCode=" + tzfxCode + ", fxkz=" + fxkz + ", dycp="
				+ dycp + ", cpsm=" + cpsm + ", gsmc=" + gsmc + ", cid=" + cid + ", gsbjCode=" + gsbjCode + ", szd=" + szd + ", cdpjc=" + cdpjc + ", xtfl=" + xtfl + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(productList);
		dest.writeString(pid);
		dest.writeString(cpmc);
		dest.writeString(xszt);
		dest.writeString(cpqxCode);
		dest.writeString(yqsyCode);
		dest.writeString(qszjCode);
		dest.writeString(hmdp);
		dest.writeString(cpjs);
		dest.writeString(clrq);
		dest.writeString(fsrq);
		dest.writeString(tzfxCode);
		dest.writeString(fxkz);
		dest.writeString(dycp);
		dest.writeString(cpsm);
		dest.writeString(gsmc);
		dest.writeString(cid);
		dest.writeString(gsbjCode);
		dest.writeString(szd);
		dest.writeString(cdpjc);
		dest.writeString(xtfl);
		dest.writeInt(isNeedReload ? 0 : 1);
	}

	public static final Creator<TrustsDetailBean> CREATOR = new Creator<TrustsDetailBean>() {

		@Override
		public TrustsDetailBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			TrustsDetailBean _bBean = new TrustsDetailBean();
			_bBean.productList = source.readString();
			_bBean.pid = source.readString();
			_bBean.cpmc = source.readString();
			_bBean.xszt = source.readString();
			_bBean.cpqxCode = source.readString();
			_bBean.yqsyCode = source.readString();
			_bBean.qszjCode = source.readString();
			_bBean.hmdp = source.readString();
			_bBean.cpjs = source.readString();
			_bBean.clrq = source.readString();
			_bBean.fsrq = source.readString();
			_bBean.tzfxCode = source.readString();
			_bBean.fxkz = source.readString();
			_bBean.dycp = source.readString();
			_bBean.cpsm = source.readString();
			_bBean.gsmc = source.readString();
			_bBean.cid = source.readString();
			_bBean.gsbjCode = source.readString();
			_bBean.szd = source.readString();
			_bBean.cdpjc = source.readString();
			_bBean.isNeedReload = source.readInt() == 0;
			return _bBean;
		}

		@Override
		public TrustsDetailBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TrustsDetailBean[size];
		}

	};

}
