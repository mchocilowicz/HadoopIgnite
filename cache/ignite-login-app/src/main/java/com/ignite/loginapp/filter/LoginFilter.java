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

import com.ignite.loginapp.constants.Constants;

@WebFilter(urlPatterns = { "/login", "/" }, filterName = "loginFilter")
public class LoginFilter implements Filter, Constants {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		Cookie[] cookies = httpRequest.getCookies();
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
			httpResponse.sendRedirect("home");
		}
		else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	public void destroy() {

	}
}
