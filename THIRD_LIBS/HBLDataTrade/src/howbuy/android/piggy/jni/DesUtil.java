package howbuy.android.piggy.jni;

/**
 * 加密解密工具类
 * 
 * @author Administrator
 * 
 */
public class DesUtil {
	/**
	 * des加密
	 * 
	 * @param b
	 */
	public native byte[] encryptDes(byte[] sourceCode, byte[] key, boolean debugFlag);

	/**
	 * des解密
	 * 
	 * @param b
	 */
	public native byte[] dencryptDes(byte[] ciphertext, byte[] key, boolean debugFlag);

	/**
	 * rsa解密
	 * 
	 * @param b
	 */
	public native byte[] dencryptRsa(byte[] ciphertext, byte[] key, boolean debugFlag);
	
	
	
	
	
	
	

}
