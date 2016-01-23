package com.ligq.shoe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class TokenAuthFilter extends GenericFilterBean {

	private List<String> ignoreURIs;

	@Autowired
	private Environment env;

	Logger logger = LoggerFactory.getLogger(getClass());

	@PostConstruct
	public void postConstruc() {
		ignoreURIs = new ArrayList<>();
		ignoreURIs.add("/reputation/index");
		ignoreURIs.add("/reputation/loading");
		ignoreURIs.add("/reputation/errors");
		ignoreURIs.add("/reputation/expire");
		ignoreURIs.add("/reputation/index/css");
		ignoreURIs.add("/reputation/index/fonts");
		ignoreURIs.add("/reputation/index/images");
		ignoreURIs.add("/reputation/index/js");
		ignoreURIs.add("/reputation/index/templates");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;

		String requestURI = httpServletRequest.getRequestURI();

		if (httpServletRequest.getCookies() != null) {
			for (Cookie cookie : httpServletRequest.getCookies()) {
				if (cookie.getName().equals("X-Token")
						&& !StringUtils.isEmpty(cookie.getValue())) {
					chain.doFilter(request, response);
					return;
				}
			}
		}

		for (String ignoreURI : ignoreURIs) {
			if (requestURI.startsWith(ignoreURI)) {
				chain.doFilter(request, response);
				return;
			}
		}
		servletResponse.sendRedirect(env.getRequiredProperty("login.endpoint"));
	}

	@Override
	public void destroy() {
	}

}
