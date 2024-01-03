package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;
import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.junit.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteTaskServletTest {

    private static Tomcat tomcat;
    private static final String WEB_PORT = "8080";

    @BeforeClass
    public static void startServer() throws ServletException, LifecycleException {
        String webappDirLocation = "src/main/webapp/";
        tomcat = new Tomcat();

        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = WEB_PORT;
        }

        tomcat.setPort(Integer.parseInt(webPort));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        ctx.getServletContext().setAttribute(Globals.ALT_DD_ATTR, webappDirLocation + "WEB-INF/web.xml");
        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();
    }

    @AfterClass
    public static void stopServer() throws LifecycleException {
        tomcat.stop();
        tomcat.destroy();
    }

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    @InjectMocks
    private final DeleteTaskServlet deleteTaskServlet = new DeleteTaskServlet();

    @Before
    public void initialize() {
        MockitoAnnotations.openMocks(this);
        TaskRepository.getTaskRepository().deleteAll();
    }

    @Test
    public void testValidGetRequest() {

        WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + WEB_PORT)
                .build()
                .method(HttpMethod.GET)
                .uri("/delete-task?id=1")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().isEmpty();

    }

    @Test
    public void testInvalidGetRequest() {
        byte[] body = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + WEB_PORT)
                .build()
                .method(HttpMethod.GET)
                .uri("/delete-task?id=3")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("text/html;charset=UTF-8")
                .expectBody().returnResult().getResponseBody();

        assert body != null;
        Assert.assertTrue(body.length > 0);

        String strBody = new String(body);
        Assert.assertTrue("Expected message 'Task with ID '3' not found in To-Do List!'", strBody.contains("Task with ID '3' not found in To-Do List!"));
    }

    @Test
    public void testCorrectTaskDelete() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("3");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(taskRepository.delete(anyInt())).thenReturn(true);

        deleteTaskServlet.doGet(request, response);

        verify(taskRepository, times(1)).delete(anyInt());
    }
}
