package com.ignite.loginapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ignite.loginapp.constants.Constants;
import com.ignite.loginapp.utils.LoginCookieUtils;

@Controller
public class LoginController implements Constants {

	@Autowired
	LoginCookieUtils loginCookieUtils;

	@RequestMapping(method = RequestMethod.GET, value = { "/", "/login" })
	public String login(HttpServletRequest httpServletRequest, ModelMap modelMap) {
		HttpSession httpSession = httpServletRequest.getSession(false);
		if (httpSession.getAttribute("errorMessage") != null) {
			modelMap.addAttribute("errorMessage", httpSession.getAttribute("errorMessage"));
			httpSession.removeAttribute("errorMessage");
		}
		return "login";
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/logout" })
	public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		HttpSession httpSession = httpServletRequest.getSession(false);
		httpSession.invalidate();
		loginCookieUtils.deleteLoginCookie(httpServletResponse);
		return "redirect:/login";
	}

	@RequestMapping(method = RequestMethod.POST, value = { "/login" })
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		HttpSession httpSession = null;
		try {
			if (SAMPLE_USERNAME_PASSWORD[0].equals(username) && SAMPLE_USERNAME_PASSWORD[1].equals(password)) {
				httpSession = httpServletRequest.getSession(true);
				loginCookieUtils.addLoginCookie(httpServletResponse, username);
				httpSession.setAttribute(LOGIN_SESSION_DATA_ATTRIBUTE, new StringBuilder("Welcome ").append(username)
						.append(".<br/>This message is stored in HttpSession."));
				return "redirect:/home";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		httpSession=httpServletRequest.getSession(false);
		httpSession.setAttribute("errorMessage", "Invalid username or password");
		return "redirect:/login";
	}

}
