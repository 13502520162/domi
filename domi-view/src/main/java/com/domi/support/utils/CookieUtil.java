package com.domi.support.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.domi.support.identification.BzLogger;
import com.domi.support.identification.NumberUtil;
import com.domi.support.identification.StringUtil;
import com.domi.support.utils.UrlUtil;

/**
 * Cookie util
 * 
 * @author tgf 2008/07/03
 */
public class CookieUtil
{
	private static final String P3P_HEADER = "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"";

	private static BzLogger logger = new BzLogger(CookieUtil.class);

	/**
	 * delete cookie set cookie's maxAge=0 mean delete the cookie
	 * 
	 */
	public static void deleteCookie(HttpServletResponse response, String key)
	{
		// cookie ֵΪnull��ʾɾ��cookie
		CookieInfo info = new CookieInfo(key, null);
		info.setCookieMaxAge(-1);
		addCookie(response, info);
	}

	/**
	 * delete cookie set cookie's maxAge=0 mean delete the cookie
	 * 
	 */
	public static void deleteCookie(HttpServletResponse response,
			CookieInfo info)
	{
		// cookie ֵΪnull��ʾɾ��cookie
		info.setCookieValue(null);
		addCookie(response, info);
	}

	/**
	 * add cookie
	 * 
	 * @param response
	 * @param key
	 * @param value
	 */
	public static Cookie addCookie(HttpServletResponse response, String key,
			String value)
	{
		CookieInfo info = new CookieInfo(key, value);

		return addCookie(response, info);
	}

	/**
	 * ����cookie
	 * 
	 * @param info
	 *            cookie����Ϣ
	 */
	public static Cookie addCookie(HttpServletResponse response, CookieInfo info)
	{
		if (info == null)
		{
			logger.error("cookie info is null");
			return null;
		}
		// �Ƿ���Ҫʹ��P3P,��Ҫ����set cookie
		checkP3P(response, info);
		Cookie cookie = info.toCookie();
		response.addCookie(cookie);

		return cookie;
	}

	/**
	 * ��httpservletRequest����Cookie����
	 * 
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String key)
	{
		if (StringUtil.isTrimEmpty(key))
		{
			return null;
		}

		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0)
		{
			for (Cookie cookie : cookies)
			{
				if (key.equals(cookie.getName()))
				{
					return cookie;
				}
			}
		}

		return null;
	}

	/**
	 * �ж��Ƿ���Ҫʹ��P3P
	 * 
	 * @param info
	 */
	private static void checkP3P(HttpServletResponse response, CookieInfo info)
	{
		if (info.isUseP3P())
		{
			response.setHeader("P3P", P3P_HEADER);
		}
	}

	/**
	 * ��cookie�з����ַ�ֵ
	 * 
	 * @return
	 */
	public static String getString(HttpServletRequest request, String key)
	{
		Cookie cookie = getCookie(request, key);
		if (cookie == null)
		{
			return null;
		}

		String value = cookie.getValue();
		if ("null".equals(value))
		{
			return null;
		}

		return UrlUtil.decode(value);
	}

	/**
	 * ��cookie�з���intֵ
	 * 
	 * @return �����ڷ���0
	 */
	public static int getInt(HttpServletRequest request, String key)
	{
		return NumberUtil.parseInt(getString(request, key), 0);
	}

	/**
	 * ��cookie�з���doubleֵ
	 * 
	 * @return �����ڷ���0
	 */
	public static double getDouble(HttpServletRequest request, String key)
	{
		return NumberUtil.parseDouble(getString(request, key), 0d);
	}

}
