package com.time.memory.util.pinyin;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by YoKeyword on 16/3/20.
 */
public class PinyinUtil {
	public static final String PATTERN_POLYPHONE = "^#[a-zA-Z]+#.+";

	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 */
	public static String getPingYin(String inputString) {
		if (TextUtils.isEmpty(inputString)) return "";
		char[] input = inputString.trim().toCharArray();
		String output = "";
		for (int i = 0; i < input.length; i++) {
			output += Pinyin.toPinyin(input[i]);
		}
		return output.toLowerCase();
	}

	/**
	 * 获取拼音的首字母（大写）
	 */
	public static String getFirstLetter(final String pinyin) {
		if (TextUtils.isEmpty(pinyin)) return "#";
		String c = pinyin.substring(0, 1);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c).matches()) {
			return c.toUpperCase();
		} else
			return "#";
	}


	/**
	 * 是否以%[a-zA-Z]%开头,用以处理多音字
	 */
	public static boolean matchingPolyphone(String inputString) {
		return Pattern.matches(PATTERN_POLYPHONE, inputString);
	}

	/**
	 * 获取多音字的 开头字母
	 */
	public static String getMatchingFirstPinyin(String inputString) {
		return inputString.substring(1, 2);
	}

	/**
	 * 获取多音字的 真实拼音
	 */
	public static String getMatchingPinyin(String inputString) {
		String[] splits = inputString.split("#");
		return splits[1];
	}

	/**
	 * 获取多音字的 真实汉字
	 */
	public static String getMatchingHanzi(String inputString) {
		String[] splits = inputString.split("#");
		return splits[2];
	}
}
