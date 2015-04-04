package howbuy.android.piggy.parameter;

import howbuy.android.piggy.application.ApplicationParams;

/**
 * 全局参数类 
 * @author Administrator
 *
 */
public class GlobalParams {
	private boolean debugFlag;
	private boolean encryptDebugFlag;
	
	public static GlobalParams getGlobalParams() {
		return ApplicationParams.getInstance().getGlobalParams();
	}

	public boolean isDebugFlag() {
		return debugFlag;
	}

	public void setDebugFlag(boolean debugFlag) {
		this.debugFlag = debugFlag;
	}

	public boolean isEncryptDebugFlag() {
		return encryptDebugFlag;
	}

	public void setEncryptDebugFlag(boolean encryptDebugFlag) {
		this.encryptDebugFlag = encryptDebugFlag;
	}
	
	
	
	
	
	
}
