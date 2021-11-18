package com.nlp1.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/json")
public class JSONServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-Type", "application/json");
        resp.setStatus(200);
        resp.getOutputStream().println("{\"username\":\"nluther\",\"password\":\"1234\",\"object\":{\"key\":\"value\"}}");
        System.out.println("Someone's accessing!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("username") +req.getParameter("password"));
        resp.setHeader("Content-Type", "application/json");
        resp.setStatus(200);
        resp.getOutputStream().println("{\"username\":\""+req.getParameter("username")+"\",\"password\":\""+req.getParameter("password")+"\",\"object\":{\"key\":\"value\"}}");
    }

}
