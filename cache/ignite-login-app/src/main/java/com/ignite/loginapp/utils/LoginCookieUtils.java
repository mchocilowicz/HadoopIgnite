package com.ignite.loginapp.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.ignite.loginapp.constants.Constants;

@Component
public class LoginCookieUtils implements Constants {

	public void deleteLoginCookie(HttpServletResponse httpServletResponse) {
		Cookie cookie = new Cookie(LOGIN_COOKIE, "");
		cookie.setMaxAge(0);
		httpServletResponse.addCookie(cookie);
	}

	public void addLoginCookie(HttpServletResponse httpServletResponse, String username) {
		Cookie cookie = new Cookie(LOGIN_COOKIE, username);
		httpServletResponse.addCookie(cookie);
	}
}
