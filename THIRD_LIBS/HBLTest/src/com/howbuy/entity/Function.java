package com.howbuy.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.howbuy.lib.net.UrlUtils;

public class Function implements Parcelable{
	public String mTarget;
	private String mName;
	private String mReturn;
	private String mDescription;
	private String mSubUrl;
	private String mParams;
    private boolean isExpand=false;
    

   
    public Function(){}
	public Function(String mName, String mReturn, String mDescription,
			String mSubUrl) {
		this.mName = mName;
		this.mReturn = mReturn;
		this.mDescription = mDescription;
		this.mSubUrl = mSubUrl;
	}
    public void setExpanded(boolean isExpand){
    	this.isExpand=isExpand;
    }
    public boolean isExpanded(){
    	return isExpand;
    }
    
    public String getActivity(){
    	return mTarget;
    }
    
	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getReturn() {
		return mReturn;
	}

	public void setReturn(String mReturn) {
		this.mReturn = mReturn;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getSubUrl() {
		return mSubUrl;
	}

	public void setSubUrl(String mSubUrl) {
		this.mSubUrl = mSubUrl;
	}
    public String getUrl(){
    	return UrlUtils.buildUrl( mSubUrl);
    }
	public String getParams() {
		return mParams;
	}

	public void setParams(String mParams) {
		this.mParams = mParams;
	}
	@Override
	public String toString() {
		return "Function [mName=" + mName + ", mReturn=" + mReturn
				+ ", mDescription=" + mDescription + ", mSubUrl=" + mSubUrl
				+ ", mParams=" + mParams + ", isExpand=" + isExpand + "]";
	}
	
	 
	public String toShortString() {
		return  mName+" "+mDescription+" "+mReturn ;
	}
	
	 /**
     * Parcelable interface methods
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Write this rectangle to the specified parcel. To restore a rectangle from
     * a parcel, use readFromParcel()
     * @param out The parcel to write the rectangle's coordinates into
     */
    public void writeToParcel(Parcel out, int flags) {
      out.writeString(mName);
      out.writeString(mReturn);
      out.writeString(mDescription);
      out.writeString(mSubUrl);
      out.writeString(mParams);
      out.writeInt(isExpand?1:0);
    }
    /**
     * Set the rectangle's coordinates from the data stored in the specified
     * parcel. To write a rectangle to a parcel, call writeToParcel().
     *
     * @param in The parcel to read the rectangle's coordinates from
     */
    public void readFromParcel(Parcel in) {
       mName=in.readString();
       mReturn=in.readString();
       mDescription=in.readString();
       mSubUrl=in.readString();
       mParams=in.readString();
       isExpand=in.readInt()==1;
    }
    public static final Parcelable.Creator<Function> CREATOR = new Parcelable.Creator<Function>() {
        /**
         * Return a new rectangle from the data in the specified parcel.
         */
        public Function createFromParcel(Parcel in) {
        	Function r = new Function();
            r.readFromParcel(in);
            return r;
        }

        /**
         * Return an array of rectangles of the specified size.
         */
        public Function[] newArray(int size) {
            return new Function[size];
        }
    };
    

}
