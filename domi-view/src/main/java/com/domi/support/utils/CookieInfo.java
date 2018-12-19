package com.domi.support.utils;

import java.io.Serializable;

import javax.servlet.http.Cookie;

import com.domi.support.utils.UrlUtil;

/**
 * cookie��Ϣ
 * 
 * @author tgf(Jan 29, 2011)
 * 
 */
public class CookieInfo
{
	/** cookie��� */
	private String cookieName;
	/** cookieֵ */
	private Serializable cookieValue;
	/** �����Чʱ��(��), �����ʾ��������(Ĭ��), 0��ʾɾ��cookie */
	private int cookieMaxAge = -1;
	/** path where cookie only can see */
	private String cookiePath = "/";
	/** domain,only server that set the cookie */
	private String cookieDomain;
	/** �Ƿ���ʹ��P3P���� */
	private boolean useP3P;

	public CookieInfo()
	{
	}

	public CookieInfo(String cookieName, Serializable cookieValue)
	{
		this.cookieName = cookieName;
		this.cookieValue = cookieValue;
		// ������Чʱ��
		if (this.cookieValue == null)
		{
			// ��ʾɾ��
			this.cookieMaxAge = 0;
		}
	}

	public CookieInfo(String key, Serializable value, int expires)
	{
		this.cookieName = key;
		this.cookieValue = value;
		// ������Чʱ��
		if (this.cookieValue == null)
		{
			// ��ʾɾ��
			this.cookieMaxAge = 0;
		}
		else if (expires > 0)
		{
			// ������Ч��
			this.cookieMaxAge = expires;
		}
	}

	public void setCookieValue(Serializable cookieValue)
	{
		this.cookieValue = cookieValue;
		if (this.cookieValue == null)
		{
			// ��ʾɾ��
			this.cookieMaxAge = 0;
		}
	}

	/**
	 * ת��Ϊ��׼Cookie����
	 * 
	 * @return
	 */
	public Cookie toCookie()
	{
		String value = "";
		if (cookieValue == null)
		{
			// ɾ��cookie
			this.cookieMaxAge = 0;
		}
		else
		{
			value = UrlUtil.encode(cookieValue.toString());
		}
		Cookie cookie = new Cookie(cookieName, value);
		cookie.setMaxAge(cookieMaxAge);
		cookie.setPath(cookiePath);
		// if(StringUtil.isNotEmpty(cookieDomain)){
		// cookie.setDomain(cookieDomain);
		// }
		return cookie;
	}

	public static CookieInfo toCookieInfo(Cookie cookie)
	{
		if (cookie == null)
		{
			return null;
		}

		CookieInfo info = new CookieInfo();
		info.setCookieName(cookie.getName());
		info.setCookieValue(cookie.getValue());
		info.setCookieMaxAge(cookie.getMaxAge());
		info.setCookiePath(cookie.getPath());
		info.setCookieDomain(cookie.getDomain());
		return info;
	}

	/**
	 * copy
	 * 
	 * @return
	 */
	public CookieInfo copy()
	{
		CookieInfo info = new CookieInfo(cookieName, cookieValue);
		info.setCookieMaxAge(cookieMaxAge);
		info.setCookieDomain(cookieDomain);
		info.setCookiePath(cookiePath);
		info.setUseP3P(useP3P);
		return info;
	}

	public String getCookieName()
	{
		return cookieName;
	}

	public void setCookieName(String cookieName)
	{
		this.cookieName = cookieName;
	}

	public String getCookieValue()
	{
		return cookieValue.toString();
	}

	public int getCookieMaxAge()
	{
		return cookieMaxAge;
	}

	public void setCookieMaxAge(int cookieMaxAge)
	{
		this.cookieMaxAge = cookieMaxAge;
	}

	public String getCookiePath()
	{
		return cookiePath;
	}

	public void setCookiePath(String cookiePath)
	{
		this.cookiePath = cookiePath;
	}

	public String getCookieDomain()
	{
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain)
	{
		this.cookieDomain = cookieDomain;
	}

	public boolean isUseP3P()
	{
		return useP3P;
	}

	public void setUseP3P(boolean useP3P)
	{
		this.useP3P = useP3P;
	}
}
