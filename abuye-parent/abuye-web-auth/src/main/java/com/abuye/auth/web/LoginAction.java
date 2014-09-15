package com.abuye.auth.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.abuye.core.web.ControllerMappingAnnotation;

@ControllerMappingAnnotation("/login")
public class LoginAction {
  @ControllerMappingAnnotation("/tologin.do")
  public void toLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/login.html");
    rd.forward(request, response);
  }
}
