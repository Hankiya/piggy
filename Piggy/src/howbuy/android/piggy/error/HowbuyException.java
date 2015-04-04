package howbuy.android.piggy.error;

public class HowbuyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HowbuyException(String detailMessage) {
		super(detailMessage);
	}

	public HowbuyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		throwable.printStackTrace();
	}

}
