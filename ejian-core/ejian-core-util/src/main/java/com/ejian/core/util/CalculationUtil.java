package com.ejian.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CalculationUtil {

	/**
	 * 分转换成元
	 * 保留两位小数
	 * @param fen
	 */
	public static String fen2Yuan(int fen){
		
		return new DecimalFormat("0.00").format(new BigDecimal(fen).divide(new BigDecimal("100"), 2, RoundingMode.UP));
	}

	public static Integer yuan2fen(Integer yuan){
		if(yuan == null){
			return null;
		}
		return new BigDecimal(yuan).multiply(new BigDecimal("100")).intValue();
	}
	
	/**
	 * 分转换成万元
	 * 保留四位小数
	 * @param fen
	 */
	public static String fen2Wan(int fen){
		return new DecimalFormat("0.0000").format(new BigDecimal(fen).divide(new BigDecimal("1000000"), 2, RoundingMode.UP));
	}
	
	/**
	 * 元转换成万元
	 * 保留四位小数
	 * @param yuan
	 */
	public static String yuan2Wan(String yuan){
		return new DecimalFormat("0.0000").format(new BigDecimal(yuan).divide(new BigDecimal("10000"), 2, RoundingMode.UP));
	}

}
