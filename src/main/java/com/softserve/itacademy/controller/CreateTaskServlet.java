package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/create-task")
public class CreateTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;

    @Override
    public void init() {
        taskRepository = TaskRepository.getTaskRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("priorities", Priority.values());
        request.getRequestDispatcher("/WEB-INF/pages/create-task.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String priorityParam = request.getParameter("priority");

        if (name != null && !name.isEmpty() && priorityParam != null && !priorityParam.isEmpty()) {
            try {
                Priority priority = Priority.valueOf(priorityParam.toUpperCase());
                Task newTask = new Task(name, priority);

                if (taskRepository.create(newTask)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Task created successfully");
                    response.sendRedirect("/create-task?ok=success");
                    return;
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("Task with a given name already exists!");
                    response.sendRedirect("/create-task?error=duplicate");
                    return;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("/create-task?error=invalid");
    }
}
