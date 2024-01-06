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
import java.util.Objects;

@WebServlet("/edit-task")
public class UpdateTaskServlet extends HttpServlet {

    private TaskRepository taskRepository;
    private Task task;
    private String taskId;

    @Override
    public void init() {
        taskRepository = TaskRepository.getTaskRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        taskId = request.getParameter("id");
        task = taskRepository.read(Integer.parseInt(taskId));
        if (task != null) {
            request.setAttribute("task", task);
            request.setAttribute("priorities", Priority.values());
            request.getRequestDispatcher("/WEB-INF/pages/edit-task.jsp").forward(request,response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("error", "Task with ID '" +
                    taskId +"' not found in To-Do List!");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request,response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (task != null) {
            String title = request.getParameter("name");
            if (taskRepository.all().stream()
                    .filter(t -> t.getId() != task.getId()) // Виключаємо поточний таск
                    .anyMatch(t -> t.getTitle().equals(title))) {
                response.sendRedirect("/edit-task?id=" + taskId + "&error=duplicate");
            } else {
                task.setTitle(title);
                task.setPriority(Priority.valueOf(request.getParameter("priority").toUpperCase()));
                taskRepository.update(task);
                response.sendRedirect("/tasks-list");
            }
        } else {
            response.sendRedirect("/edit-task?id=" + taskId + "&error=notfound");
        }
    }
}
