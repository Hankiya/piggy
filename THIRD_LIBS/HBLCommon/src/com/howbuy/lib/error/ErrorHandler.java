package com.howbuy.lib.error;

import java.util.HashMap;

import com.howbuy.lib.utils.StrUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-1-15 下午5:53:26
 */
public class ErrorHandler {
	private HashMap<String, ErrType> mMap = new HashMap<String, ErrType>();
	private int mCodeMax = 0;

	public ErrorHandler() {
		initErrorMap();
	}

	private void initErrorMap() {
		mMap.put("ArrayIndexOutOfBoundsException", new ErrType(++mCodeMax, "数组下标越界异常"));
		mMap.put("ArithmeticException", new ErrType(++mCodeMax, "算术异常"));
		mMap.put("ClassCastException", new ErrType(++mCodeMax, "类型强制转换异常"));
		mMap.put("ConnectException", new ErrType(++mCodeMax, "无法连接异常"));
		mMap.put("ConnectTimeoutException", new ErrType(++mCodeMax, "连接超时异常"));
		mMap.put("SocketTimeoutException", new ErrType(++mCodeMax, "请求超时了"));
		mMap.put("EOFException", new ErrType(++mCodeMax, "文件或网络异常结束"));
		mMap.put("FileNotFoundException", new ErrType(++mCodeMax, "文件未找到异常"));
		mMap.put("IllegalStateException", new ErrType(++mCodeMax, " 违法的状态异常"));
		mMap.put("IllegalThreadStateException", new ErrType(++mCodeMax, "违法的线程状态异常"));
		mMap.put("IndexOutOfBoundsException", new ErrType(++mCodeMax, "索引越界异常"));
		mMap.put("IOException", new ErrType(++mCodeMax, "输入输出异常"));
		mMap.put("NullPointerException", new ErrType(++mCodeMax, "空指针异常"));
		mMap.put("NumberFormatException", new ErrType(++mCodeMax, "字符串转换为数字异常"));
		mMap.put("OutOfMemoryError", new ErrType(++mCodeMax, "内存不足错误"));
		mMap.put("RuntimeException", new ErrType(++mCodeMax, "运行时异常"));
		mMap.put("SQLException", new ErrType(++mCodeMax, "操作数据库异常"));
		mMap.put("StackOverflowError", new ErrType(++mCodeMax, "堆栈溢出错误"));
		mMap.put("SecurityException", new ErrType(++mCodeMax, "安全异常"));
		mMap.put("SocketException", new ErrType(++mCodeMax, "连接被正常关闭异常"));
		mMap.put("StringIndexOutOfBoundsException", new ErrType(++mCodeMax, "字符串索引越界异常"));
		mMap.put("UnknownHostException", new ErrType(++mCodeMax, "未知服务器地址"));
		mMap.put("SQLiteConstraintException", new ErrType(++mCodeMax, "主键不能相同"));
	}

	public final ErrType getErrorType(Throwable e) {
		boolean isUnKnowError = e instanceof UnKnowException;
		ErrType mErr = parseError(e, isUnKnowError);
		if (mErr == null) {
			mErr = mMap.get(e.getClass().getSimpleName());
		}
		return mErr;
	}

	protected final boolean addToErrorMap(String key, int code, String des) {
		boolean result = false;
		if (!StrUtils.isEmpty(key) && !mMap.containsKey(key)) {
			mMap.put(key, new ErrType(++mCodeMax, des));
		}
		return result;
	}

	/**
	 * add custom common Exception map.
	 * 
	 * @param minCode
	 *            custom error code must not less than minCode,and be unique.one
	 *            by one add 1.
	 * @throws
	 */
	protected void initCustomErrorMap(int minCode) {
		// addToErrorMap("NullPointerException", minCode, "空指针异常");
		// addToErrorMap("UnknownHostException", ++minCode, "未知服务器地址");
	}

	/**
	 * @param e
	 *            Exception e not in Custom Maps or it's a UnKnowException.
	 * @param isUnKnowError
	 * @return ErrChild null for unhandled.
	 * @throws
	 */
	protected ErrType parseError(Throwable e, boolean isUnKnowError) {
		return null;
	}
}
