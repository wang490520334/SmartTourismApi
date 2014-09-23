package iii.org.tw.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class AccessControlAllowOriginFilter implements Filter {

	public void destroy() {

		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		
		
		HttpServletRequest req = (HttpServletRequest)request;
		String clientOrigin = req.getHeader("origin");
		clientOrigin = StringUtils.isEmpty(clientOrigin) ? "*" : clientOrigin;
		

		HttpServletResponse hsr = (HttpServletResponse) response;
		hsr.setHeader("Access-Control-Allow-Origin", clientOrigin);
		hsr.setHeader("Access-Control-Allow-Credentials", "true");
		hsr.setHeader("Access-Control-Allow-Methods", "POST,PUT,DELETE,GET,OPTIONS");
		hsr.setHeader("Access-Control-Allow-Headers", ((HttpServletRequest)request).getHeader("Access-Control-Request-Headers"));//"X-Requested-With, Content-Type, Accept, usertoken, socialId, email, pass, apiKey, userKey, eventId, PoiId, language, oldPass, newPass, userToken, securityCode, poiList");
		hsr.setHeader("Allow", "POST,PUT,DELETE,GET,OPTIONS");

		filterChain.doFilter(request, response);
		
		
		DbUtil.closeLocalConnection();
		
	}

	public void init(FilterConfig filterChain) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
