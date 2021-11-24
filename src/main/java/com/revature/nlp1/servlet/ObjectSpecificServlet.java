package com.revature.nlp1.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ormnl.service.ObjectService;

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


@WebServlet(value = "/Object/*")
public class ObjectSpecificServlet extends HttpServlet {
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
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        ArrayList<String> jsons = new ArrayList<>();
        PrintWriter out = resp.getWriter();
        String classSimpleName = req.getPathInfo().substring(1);
        ArrayList<HashMap<String,String>> objects = new ArrayList<>();
        try {
            objects = objectService.get(classSimpleName);
        } catch (ClassNotFoundException e) {
            resp.setStatus(404);
            out.print("Class Not Found In Database");
            return;
        }
        int pid;
        try {
            pid = Integer.parseInt(req.getParameter("pid"));
        } catch (NumberFormatException e) {
            pid = 0;
        }
        if (pid < 1) {
            for (HashMap<String, String> object : objects) {
                jsons.add(new ObjectMapper().writeValueAsString(object));
            }
            out.print(jsons);
        } else {
            try {
                out.print(new ObjectMapper().writeValueAsString(objects.get(Integer.parseInt(req.getParameter("pid"))-1)));
            } catch (IndexOutOfBoundsException e) {
                resp.setStatus(404);
                out.print("Object Corresponding to PID Not Found In Database");
                return;
            }
        }
        out.flush();
        System.out.println("Someone's accessing on " + classSimpleName + "!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,String> objectMap = mapper.readValue(req.getInputStream(),HashMap.class);
        PrintWriter out = resp.getWriter();
        if (tableNames.contains(req.getPathInfo().substring(1))) {
            if (objectService.add(objectMap,req.getPathInfo().substring(1))){
                out.print("Success!");
            } else {
                out.print("Failure to Post");
            }
        } else {
            out.print("Class not found in database. Post new classes to /Object with Parameter \"class_name\".");
        }
        out.flush();

    }
}
