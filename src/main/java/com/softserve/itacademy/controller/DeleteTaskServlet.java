package com.softserve.itacademy.controller;

import com.softserve.itacademy.repository.TaskRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete-task")
public class DeleteTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init()  {
        taskRepository = TaskRepository.getTaskRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String taskId = request.getParameter("id");

        if (taskId != null && !taskId.isEmpty() && (taskRepository.read(Integer.parseInt(taskId)) != null)) {
            taskRepository.delete(Integer.parseInt(taskId));
            try {
                response.sendRedirect("/task-list");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }   else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("error", "Task with ID '" +
                    taskId +"' not found in To-Do List!");
            try {
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request,response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
