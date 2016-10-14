package com.gnet.utils.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额处理工具类
 * 提供加减乘除、运算比较等等
 * 默认进度为2，可以指定特定精度
 * 所有运算返回BigDeciaml类型，确保精度
 * @author xuq
 * @date 2014年7月15日
 */
public class PriceUtils {
	
	protected static final int SCALE = 2;

	/**
	 * 格式化货币为小数点两位的银行家算法
	 * @param need_format	需要格式化的数字
	 * @return
	 */
	public static BigDecimal currency(BigDecimal need_format) {
		return currency(need_format, SCALE);
	}
	
	/**
	 * 格式化货币为小数点两位的银行家算法
	 * @param need_format	需要格式化的数字
	 * @param scale		精度
	 * @return
	 */
	public static BigDecimal currency(BigDecimal need_format, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		
		return need_format.setScale(scale, RoundingMode.HALF_EVEN);		// 使用银行家算法
	}
	
	/**
	 * 提供精准的金额加法计算
	 * @param number1	第一个数字
	 * @param number2	第二个数字
	 * @return
	 */
	public static BigDecimal add(BigDecimal number1, BigDecimal number2) {
		return add(number1, number2, SCALE);
	}
	
	/**
	 * 提供精准的金额加法计算
	 * @param number1	第一个数字
	 * @param number2	第二个数字
	 * @param scale		精准度
	 * @return
	 */
	public static BigDecimal add(BigDecimal number1, BigDecimal number2, int scale) {
		return currency(_add(number1, number2), scale);
	}
	
	/**
	 * 提供精准的金额加法计算(不设置精准度)
	 * @param number1	第一个数字
	 * @param number2	第二个数字
	 * @return
	 */
	public static BigDecimal _add(BigDecimal number1, BigDecimal number2) {
		return number1.add(number2);
	}
	
	/**
	 * 提供精准的金额减法计算
	 * @param number1	第一个数字
	 * @param number2	第二个数字
	 * @return
	 */
	public static BigDecimal sub(BigDecimal number1, BigDecimal number2) {
		return sub(number1, number2, SCALE);
	}
	
	/**
	 * 提供精准的金额减法计算
	 * @param number1	第一个数字
	 * @param number2	第二个数字
	 * @param scale		精准度
	 * @return
	 */
	public static BigDecimal sub(BigDecimal number1, BigDecimal number2, int scale) {
		return currency(_sub(number1, number2), scale);
	}
	
	/**
	 * 提供精准的金额减法计算(不设置精准度)
	 * @param number1	第一个数字
	 * @param number2	第二个数字
	 * @return
	 */
	public static BigDecimal _sub(BigDecimal number1, BigDecimal number2) {
		return number1.subtract(number2);
	}
	
	/**
	 * 提供精准的金额乘法计算
	 * @param number1	第一个数字
	 * @param number2	第二个数字
	 * @return
	 */
	public static BigDecimal mul(BigDecimal number1, BigDecimal number2) {
		return mul(number1, number2, SCALE);
	}
	
	/**
	 * 提供精准的金额乘法计算
	 * @param number1	第一个数字
	 * @param number2	第二个数字
	 * @param scale		精准度
	 * @return
	 */
	public static BigDecimal mul(BigDecimal number1, BigDecimal number2, int scale) {
		return currency(_mul(number1, number2), scale);
	}
	
	/**
	 * 提供精准的金额乘法计算(不设置精准度)
	 * @param number1	第一个数字
	 * @param number2	第二个数字
	 * @return
	 */
	public static BigDecimal _mul(BigDecimal number1, BigDecimal number2) {
		return number1.multiply(number2);
	}
	
	/**
	 * 提供精准的金额出发运算，采用了银行家算法进行四舍五入
	 * @param number1	除数
	 * @param number2	被除数
	 * @return
	 */
	public static BigDecimal div(BigDecimal number1, BigDecimal number2) {
		return div(number1, number2, SCALE);
	}
	
	/**
	 * 提供精准的金额出发运算，采用了银行家算法进行四舍五入
	 * @param number1	除数
	 * @param number2	被除数
	 * @param scale		精准度
	 * @return
	 */
	public static BigDecimal div(BigDecimal number1, BigDecimal number2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		return number1.divide(number2, scale, RoundingMode.HALF_EVEN);
	}
	
	/**
	 * 将数值取负数
	 * @param number
	 * @return
	 */
	public static BigDecimal negative(BigDecimal number) {
		return number.negate();
	}
	
	/**
	 * 提供精准的金额比较大小的方法<br/>
	 * number1 > number2 return true<br/>
	 * number1 <= number2 return false<br/>
	 * @param number1
	 * @param number2
	 * @return
	 */
	public static boolean greaterThan(BigDecimal number1, BigDecimal number2) {
		return number1.compareTo(number2) == 1;
	}
	
	/**
	 * 提供精准的金额比较大小的方法<br/>
	 * number1 < number2 return true<br/>
	 * number1 >= number2 return false<br/>
	 * @param number1
	 * @param number2
	 * @return
	 */
	public static boolean greaterEqThan(BigDecimal number1, BigDecimal number2) {
		return number1.compareTo(number2) == -1;
	}
	
	/**
	 * 提供精准的金额为0的判断方法
	 * @param number
	 * @return
	 */
	public static boolean isZero(BigDecimal number) {
		return number.compareTo(new BigDecimal("0")) == 0;
	}
	
	/**
	 * 提供精准的金额小于等于0的判断方法
	 * @param number
	 * @return
	 */
	public static boolean isLessZero(BigDecimal number) {
		return number.compareTo(new BigDecimal("0")) < 1 ;
	}
	
	/**
	 * 获得0
	 * @return
	 */
	public static BigDecimal getZero() {
		return new BigDecimal("0");
	}
	
	/**
	 * 不可实例化
	 */
	private PriceUtils() {}
	
}
