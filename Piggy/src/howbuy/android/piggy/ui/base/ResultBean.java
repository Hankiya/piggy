package howbuy.android.piggy.ui.base;

import howbuy.android.bean.ListBean;

public class ResultBean<T extends ListBean<?, ?>> {
	T newValue;
	/**
	 * cons.showXXX();
	 */
	int Result;

	String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getNewValue() {
		return newValue;
	}

	public void setNewValue(T newValue) {
		this.newValue = newValue;
	}

	public int getResult() {
		return Result;
	}

	public void setResult(int result) {
		Result = result;
	}
}
