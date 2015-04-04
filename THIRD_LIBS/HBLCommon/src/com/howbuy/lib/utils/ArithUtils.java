package com.howbuy.lib.utils;

import java.math.BigDecimal;

/**
 * 
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精
 * 
 * 确的浮点数运算，包括加减乘除和四舍五入。
 */

public class ArithUtils {

	// 默认除法运算精度

	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化
	private ArithUtils() {
	}

	/**
	 * 
	 * 提供精确的加法运算。
	 * 
	 * @param mTvSpinBackgroud
	 *            被加数
	 * 
	 * @param mTvSpinBasefund
	 *            加数
	 * 
	 * @return 两个参数的和
	 */

	public static double addDouble(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 
	 * 提供精确的减法运算。
	 * 
	 * @param mTvSpinBackgroud
	 *            被减数
	 * @param mTvSpinBasefund
	 *            减数
	 * @return 两个参数的差
	 */

	public static double subDouble(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 
	 * 提供精确的乘法运算。
	 * 
	 * @param mTvSpinBackgroud
	 *            被乘数
	 * 
	 * @param mTvSpinBasefund
	 *            乘数
	 * 
	 * @return 两个参数的积
	 */

	public static double mulDouble(double v1, double v2) {

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.multiply(b2).doubleValue();

	}

	/**
	 * 
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 * 
	 * 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param mTvSpinBackgroud
	 *            被除数
	 * 
	 * @param mTvSpinBasefund
	 *            除数
	 * 
	 * @return 两个参数的商
	 */

	public static double divDouble(double v1, double v2) {
		return divDouble(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 
	 * 定精度，以后的数字四舍五入。
	 * 
	 * @param mTvSpinBackgroud
	 *            被除数
	 * 
	 * @param mTvSpinBasefund
	 *            除数
	 * 
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * 
	 * @return 两个参数的商
	 */

	public static double divDouble(double v1, double v2, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(

			"The scale must be a positive integer or zero");

		}

		BigDecimal b1 = new BigDecimal(Double.toString(v1));

		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * 
	 * @param scale
	 *            小数点后保留几位
	 * 
	 * @return 四舍五入后的结果
	 */

	public static double roundDouble(double v, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(

			"The scale must be a positive integer or zero");

		}

		BigDecimal b = new BigDecimal(Double.toString(v));

		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	/**
	 * 
	 * 提供精确的加法运算。
	 * 
	 * @param mTvSpinBackgroud
	 *            被加数
	 * 
	 * @param mTvSpinBasefund
	 *            加数
	 * 
	 * @return 两个参数的和
	 */

	public static float addFloat(Float v1, Float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.add(b2).floatValue();
	}

	/**
	 * 
	 * 提供精确的减法运算。
	 * 
	 * @param mTvSpinBackgroud
	 *            被减数
	 * @param mTvSpinBasefund
	 *            减数
	 * @return 两个参数的差
	 */

	public static float subFloat(Float v1, Float v2) {
		BigDecimal b1 = new BigDecimal(Float.toString(v1));
		BigDecimal b2 = new BigDecimal(Float.toString(v2));
		return b1.subtract(b2).floatValue();
	}

	/**
	 * 
	 * 提供精确的乘法运算。
	 * 
	 * @param mTvSpinBackgroud
	 *            被乘数
	 * 
	 * @param mTvSpinBasefund
	 *            乘数
	 * 
	 * @return 两个参数的积
	 */

	public static float mulFloat(Float v1, Float v2) {

		BigDecimal b1 = new BigDecimal(Float.toString(v1));

		BigDecimal b2 = new BigDecimal(Float.toString(v2));

		return b1.multiply(b2).floatValue();

	}

	/**
	 * 
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 * 
	 * 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param mTvSpinBackgroud
	 *            被除数
	 * 
	 * @param mTvSpinBasefund
	 *            除数
	 * 
	 * @return 两个参数的商
	 */

	public static float divFloat(Float v1, Float v2) {

		return divFloat(v1, v2, DEF_DIV_SCALE);

	}

	/**
	 * 
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 
	 * 定精度，以后的数字四舍五入。
	 * 
	 * @param mTvSpinBackgroud
	 *            被除数
	 * 
	 * @param mTvSpinBasefund
	 *            除数
	 * 
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * 
	 * @return 两个参数的商
	 */

	public static float divFloat(Float v1, Float v2, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(

			"The scale must be a positive integer or zero");

		}

		BigDecimal b1 = new BigDecimal(Float.toString(v1));

		BigDecimal b2 = new BigDecimal(Float.toString(v2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();

	}

	/**
	 * 
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * 
	 * @param scale
	 *            小数点后保留几位
	 * 
	 * @return 四舍五入后的结果
	 */

	public static float roundFloat(Float v, int scale) {

		if (scale < 0) {

			throw new IllegalArgumentException(

			"The scale must be a positive integer or zero");

		}

		BigDecimal b = new BigDecimal(Float.toString(v));

		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();

	}

	/**
	 * @param num
	 *            the num of the generate random values.
	 * @param range
	 *            the range of the random values.
	 * @return return the generated ramdom values.
	 */
	public int[] random(int num, int range) {
		int[] x = new int[num];
		x[0] = (int) (Math.random() * range) + 1;

		for (int j = 1; j < num;) {
			x[j] = (int) (Math.random() * range) + 1;
			int t = 0;
			for (int i = 0; i < j; i++) {
				if (x[j] != x[i])
					t++;
				else
					t = 0;
			}
			if (t == j)
				j++;
		}
		return x;
	}

	/**
	 * short value to bytes;
	 */
	public static byte[] toBytes(short number) {
		int temp = number;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}

	/**
	 * bytes to short value
	 */
	public static short toShort(byte[] b) {
		short s = 0;
		short s0 = (short) (b[0] & 0xff);
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	/**
	 * integer value to bytes;
	 */
	public static byte[] toBytes(int i) {
		byte[] bt = new byte[4];
		bt[0] = (byte) (0xff & i);
		bt[1] = (byte) ((0xff00 & i) >> 8);
		bt[2] = (byte) ((0xff0000 & i) >> 16);
		bt[3] = (byte) ((0xff000000 & i) >> 24);
		return bt;
	}

	/**
	 * bytes to integer value.
	 */
	public static int toInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		num |= ((bytes[1] << 8) & 0xFF00);
		num |= ((bytes[2] << 16) & 0xFF0000);
		num |= ((bytes[3] << 24) & 0xFF000000);
		return num;
	}

	/**
	 * long value to bytes.
	 */
	public static byte[] toBytes(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}

	/**
	 * bytes to long value.
	 */
	public static long toLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

};