package com.ignite.loginapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import com.ignite.loginapp.constants.Constants;
import com.ignite.loginapp.utils.LoginCookieUtils;

@WebFilter(urlPatterns = { "/home" }, filterName = "loginCheckFilter")
public class LoginCheckFilter implements Filter, Constants, ApplicationContextAware {

	LoginCookieUtils loginCookieUtils=new LoginCookieUtils();

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		Cookie[] cookies = httpRequest.getCookies();
		httpResponse.setHeader("Cache-Control", "no-cache");
		httpResponse.setHeader("Cache-Control", "no-store");
		httpResponse.setDateHeader("Expires", 0);
		httpResponse.setHeader("Pragma", "no-cache");
		boolean isLoggedIn = false;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(LOGIN_COOKIE)) {
					if (cookie.getValue().equals(SAMPLE_USERNAME_PASSWORD[0])) {
						isLoggedIn = true;
						break;
					}
				}
			}
		}
		if (isLoggedIn) {
			filterChain.doFilter(servletRequest, servletResponse);

		}
		else {
			loginCookieUtils.deleteLoginCookie(httpResponse);
			httpResponse.sendRedirect("login");
		}
	}

	public void destroy() {
		loginCookieUtils = null;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (applicationContext instanceof WebApplicationContext) {
			((WebApplicationContext) applicationContext).getServletContext().addFilter("loginCheckFilter", this);
		} else {
			throw new RuntimeException("######Must be inside a web application context");
		}
	}
}
