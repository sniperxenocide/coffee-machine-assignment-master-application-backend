package com.cgd.coffee_machine.config;

import com.cgd.coffee_machine.model.User;
import com.cgd.coffee_machine.service.SeCommon;
import com.cgd.coffee_machine.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private SeCommon seCommon;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String requestTokenHeader = request.getHeader("Authorization");
		//Getting Token From Cookie //Fahim
		if (requestTokenHeader==null) requestTokenHeader= seCommon.getTokenFromCookie(request);

		//fahim logging
		boolean isAuthorized = logTrace(request);

		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
			if (
					jwtTokenUtil.validateToken(jwtToken, userDetails)
					&& isAuthorized //Fahim Custom Condition
			) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	public boolean logTrace(HttpServletRequest request){
		boolean isAuthorized = true;
		if(!(request.getRequestURI().startsWith("/css") ||
				request.getRequestURI().startsWith("/js") ||
				request.getRequestURI().startsWith("/favicon.ico") ))
		{
			User user = seCommon.getUser(request);
			// Checking API with user role
			if(user!=null)
				isAuthorized = seCommon.isAuthorizedRequest(user.getId(),request.getRequestURI());

			logger.info(request.getRemoteAddr()+" "+
							(user==null?"":user.getUsername())+" "+
					"Authorized:"+Boolean.toString(isAuthorized).toUpperCase()+" "+
					request.getMethod()+" "+
					request.getRequestURI()+" "+
					(request.getQueryString()!=null?request.getQueryString():""));
		}
		return isAuthorized;
	}


}