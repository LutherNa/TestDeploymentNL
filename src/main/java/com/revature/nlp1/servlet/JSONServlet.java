package com.revature.nlp1.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ormnl.*;
import com.revature.ormnl.persistance.GenericDao;
import com.revature.ormnl.service.ObjectService;
import com.revature.ormnl.util.ClassObjectInspector;


@WebServlet(value = "/Object")
public class JSONServlet extends HttpServlet {
    public ObjectService objectService;
    public ArrayList<String> tableNames;
    private String tableNamesJSON;
//    private ObjectMapper tableNamesJSON = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        objectService = new ObjectService();
        tableNames = objectService.getAllTableNames();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-Type", "application/json");
        resp.setStatus(200);
        PrintWriter out = resp.getWriter();
        ArrayList<String> tableNamesJSONURL = new ArrayList<>();
        for (int i = 0; i < tableNames.size(); i++) {
            tableNamesJSONURL.add(i, req.getRequestURL() + "/" + tableNames.get(i));
        }
        try {
            tableNamesJSON = new ObjectMapper().writeValueAsString(tableNamesJSONURL);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(tableNamesJSON);
        out.flush();
//        resp.getOutputStream().println("{\"username\":\"nluther\",\"password\":\"1234\",\"object\":{\"key\":\"value\"}}");
        System.out.println("Someone's accessing!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,String> objectMap = mapper.readValue(req.getInputStream(),HashMap.class);
        PrintWriter out = resp.getWriter();
        if (req.getParameter("class_name") != null) {
            if (objectService.add(objectMap,req.getParameter("class_name"))){
                out.print("Success!");
            } else {
                out.print("Failure to Post");
            }
        } else {
            out.print("Please Provide a Simple Class Name as Parameter \"class_name\".");
        }
        out.flush();

    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println(req.getParameter("username") +req.getParameter("password"));
//        resp.setHeader("Content-Type", "application/json");
//        resp.setStatus(200);
//        resp.getOutputStream().println("{\"username\":\""+req.getParameter("username")+"\",\"password\":\""+req.getParameter("password")+"\",\"object\":{\"key\":\"value\"}}");
//    }

}
