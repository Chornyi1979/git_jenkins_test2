package com.example;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;


public class HelloWorldServlet extends HttpServlet {

    @Override


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            request.setAttribute("message", "Hello, World!");

            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } catch (ServletException | IOException e) {

            // Обробка виключень

        }

    }
}
