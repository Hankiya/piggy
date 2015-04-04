package howbuy.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2014/8/23.
 */
public class DialogBean implements Parcelable {
    private String dialogId;
    private String title;
    private String message;
    private String pwdType;
    private String inputHint;
    private String sureBtnMsg;
    private String cancleBtnMsg;

    public DialogBean(String dialogId, String title, String message, String pwdType, String sureBtnMsg, String cancleBtnMsg,String inputHint) {
        this.dialogId = dialogId;
        this.title = title;
        this.message = message;
        this.pwdType = pwdType;
        this.sureBtnMsg = sureBtnMsg;
        this.cancleBtnMsg = cancleBtnMsg;
        this.inputHint=inputHint;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPwdType() {
        return pwdType;
    }

    public void setPwdType(String pwdType) {
        this.pwdType = pwdType;
    }

    public String getSureBtnMsg() {
        return sureBtnMsg;
    }

    public void setSureBtnMsg(String sureBtnMsg) {
        this.sureBtnMsg = sureBtnMsg;
    }

    public String getCancleBtnMsg() {
        return cancleBtnMsg;
    }

    public void setCancleBtnMsg(String cancleBtnMsg) {
        this.cancleBtnMsg = cancleBtnMsg;
    }

    public String getInputHint() {
        return inputHint;
    }

    public void setInputHint(String inputHint) {
        this.inputHint = inputHint;
    }

    @Override
    public String toString() {
        return "DialogBean{" +
                "dialogId='" + dialogId + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", pwdType='" + pwdType + '\'' +
                ", sureBtnMsg='" + sureBtnMsg + '\'' +
                ", inputHint='" + inputHint + '\'' +
                ", cancleBtnMsg='" + cancleBtnMsg + '\'' +
                '}';
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dialogId);
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeString(this.pwdType);
        dest.writeString(this.sureBtnMsg);
        dest.writeString(this.cancleBtnMsg);
        dest.writeString(this.inputHint);

    }

    private DialogBean(Parcel in) {
        this.dialogId = in.readString();
        this.title = in.readString();
        this.message = in.readString();
        this.pwdType = in.readString();
        this.sureBtnMsg = in.readString();
        this.cancleBtnMsg = in.readString();
        this.inputHint=in.readString();
    }

    public static final Creator<DialogBean> CREATOR = new Creator<DialogBean>() {
        public DialogBean createFromParcel(Parcel source) {
            return new DialogBeanBuilder().setIn(source).createDialogBean();
        }

        public DialogBean[] newArray(int size) {
            return new DialogBean[size];
        }
    };

    public static class DialogBeanBuilder {
    	public static final String PwdType_Trade = "1";
    	public static final String PwdType_Login = "2";
        private String dialogId;
        private String title;
        private String message;
        private String pwdType;
        private String sureBtnMsg;
        private String cancleBtnMsg;
        private String inputHint;
        private Parcel in;

        public DialogBeanBuilder setDialogId(String dialogId) {
            this.dialogId = dialogId;
            return this;
        }

        public DialogBeanBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public DialogBeanBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public DialogBeanBuilder setPwdType(String pwdType) {
            this.pwdType = pwdType;
            return this;
        }

        public DialogBeanBuilder setSureBtnMsg(String sureBtnMsg) {
            this.sureBtnMsg = sureBtnMsg;
            return this;
        }

        public DialogBeanBuilder setCancleBtnMsg(String cancleBtnMsg) {
            this.cancleBtnMsg = cancleBtnMsg;
            return this;
        }

        public DialogBeanBuilder setInputHint(String inputHint) {
            this.inputHint = inputHint;
            return this;
        }

        public DialogBeanBuilder setIn(Parcel in) {
            this.in = in;
            return this;
        }

        public DialogBean createDialogBean() {
        	if (pwdType==null) {
				pwdType=PwdType_Trade;
			}
            return new DialogBean(dialogId, title, message, pwdType, sureBtnMsg, cancleBtnMsg,inputHint);
        }
    }
}
