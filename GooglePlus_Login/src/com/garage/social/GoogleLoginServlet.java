package com.demo.social;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GoogleLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String code = "";

	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		code = req.getParameter("code");
		if (code == null || code.equals("")) {
			throw new RuntimeException("ERROR: Didn't get code parameter in callback.");
		}
		String userProfile = GoogleAuthHelper.getGoogleAuthHelper().getUserInfoJson(req.getParameter("code"));
		ServletOutputStream out = res.getOutputStream();
		String email = "";
		if (email != null)
			email.replace("\u0040", "@");
		out.println("<h1>Google Login using Java</h1>");
		out.println("<div>UserProfile: " + userProfile);
	}
}
