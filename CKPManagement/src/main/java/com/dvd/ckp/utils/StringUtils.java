/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.utils;

import java.text.DecimalFormat;

/**
 *
 * @author dmin
 */
public class StringUtils {

	public static boolean isValidString(String input) {
		return input != null && !"".equals(input);
	}

	public static String formatPrice(Double price) {
		DecimalFormat formatter = new DecimalFormat("###,###,###.00");
		if (price == null || price == 0d) {
			return "0";
		}
		return formatter.format(price);
	}

}
