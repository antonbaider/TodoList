package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/read-task")
public class ReadTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init() {
        taskRepository = TaskRepository.getTaskRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String taskIdParam = request.getParameter("id");
        if (taskIdParam != null && !taskIdParam.isEmpty()) {
            try {
                int taskId = Integer.parseInt(taskIdParam);
                Task task = taskRepository.read(taskId);
                if (task != null) {
                    request.setAttribute("task", task);
                    response.setStatus(HttpServletResponse.SC_OK);
                    request.getRequestDispatcher("/WEB-INF/pages/read-task.jsp").forward(request, response);
                } else {
                    String errorMessage = "Task with ID '" + taskIdParam + "' not found in To-Do List!";
                    request.setAttribute("error", errorMessage);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                String errorMessage = "Invalid ID: " + taskIdParam;
                request.setAttribute("error", errorMessage);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
                String errorMessage = "Internal Server Error";
                request.setAttribute("error", errorMessage);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            }
        } else {
            String errorMessage = "No task ID provided in the request.";
            request.setAttribute("error", errorMessage);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
