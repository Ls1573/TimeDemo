package com.time.memory.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:验证手机号
 * @date 2016-8-17上午11:05:20
 * ==============================
 */
public class RegUtils {

	public static boolean checkPhone(String phone) {
		/**
		 * * 手机号:目前全国有27种手机号段。 *
		 * 移动有16个号段：134、135、136、137、138、139、147、150、151、152
		 * 、157、158、159、182、187、188。其中147、157、188是3G号段，其他都是2G号段。 *
		 * 联通有7种号段：130、131、132、155、156、185、186。其中186是3G（WCDMA）号段，其余为2G号段。 *
		 * 电信有4个号段：133、153、180、189。其中189是3G号段（CDMA2000），133号段主要用作无线网卡号。 *
		 * 150、151、152、153、155、156、157、158、159 九个; *
		 * 130、131、132、133、134、135、136、137、138、139 十个; *
		 * 180、182、185、186、187、188、189 七个; *
		 * 13、15、18三个号段共30个号段，154、170、181、183、184暂时没有，加上147共27个。
		 */
		Pattern p = Pattern
				.compile("^((13\\d{9}$)|(17\\d{9}$)|(15\\d{9}$)|(18\\d{9}$)|(14\\d{9})$)");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	//判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
}
