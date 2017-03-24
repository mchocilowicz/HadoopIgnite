package com.ignite.loginapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ignite.loginapp.constants.Constants;

@Controller
public class HomeController implements Constants {

	@RequestMapping(method = RequestMethod.GET, value = { "/home" })
	public String home(ModelMap modelMap, HttpServletRequest httpServletRequest) {

		HttpSession httpSession = httpServletRequest.getSession(false);
		if (httpSession != null) {
			modelMap.addAttribute(LOGIN_SESSION_DATA_ATTRIBUTE,
					httpSession.getAttribute(LOGIN_SESSION_DATA_ATTRIBUTE) == null
							? "Sorry,Your session has expired"
							: httpSession.getAttribute(LOGIN_SESSION_DATA_ATTRIBUTE));
		} else {
			modelMap.addAttribute(LOGIN_SESSION_DATA_ATTRIBUTE, "Sorry,Your session has expired");
		}
		return "welcome";
	}
}
