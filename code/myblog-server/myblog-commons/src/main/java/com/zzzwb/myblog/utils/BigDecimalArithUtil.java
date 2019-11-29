package com.zzzwb.myblog.utils;

import java.math.BigDecimal;

/**
 * BigDecimal运算工具
 *
 * @author zeng wenbin
 * @date Created in 2019/7/17
 */
public final class BigDecimalArithUtil {

	/**
	 * 这个类不能实例化
	 */
	private BigDecimalArithUtil(){
	}

	/**
	 * 提供精确的加法运算。
	 * @param v1 被加数
	 * @param v2 加数 可变参数
	 * @return n个参数的和
	 */
	public static double add(double v1,double ... v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		for (double v : v2) {
			b1 = b1.add(BigDecimal.valueOf(v));
		}
		return b1.doubleValue();
	}
	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param v2 减数 可变参数
	 * @return 差
	 */
	public static double sub(double v1,double ... v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		for (double v : v2) {
			b1 = b1.subtract(BigDecimal.valueOf(v));
		}
		return b1.doubleValue();
	}
	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2 乘数 可变参数
	 * @return 积
	 */
	public static double mul(double v1,double ... v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		for (double v : v2) {
			b1 = b1.multiply(BigDecimal.valueOf(v));
		}
		return b1.doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数  可变参数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 商
	 */
	public static double div(int scale,double v1,double ... v2){
		if(scale<0){
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		for (double v : v2) {
			b1 = b1.divide(BigDecimal.valueOf(v),scale,BigDecimal.ROUND_HALF_UP);
		}
		return b1.doubleValue();
	}
	/**
	 * 提供精确的小数位四舍五入处理。
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v,int scale){
		if(scale<0){
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
