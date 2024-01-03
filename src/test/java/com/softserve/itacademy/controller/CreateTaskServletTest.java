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
import org.springframework.web.reactive.function.BodyInserters;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateTaskServletTest {

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
    private final CreateTaskServlet createTaskServlet = new CreateTaskServlet();

    @Before
    public void initialize() {
        MockitoAnnotations.openMocks(this);
        TaskRepository.getTaskRepository().deleteAll();
    }

    @Test
    public void testValidGetRequest() {

        byte[] body = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + WEB_PORT)
                .build()
                .method(HttpMethod.GET)
                .uri("/create-task")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/html;charset=UTF-8")
                .expectBody().returnResult().getResponseBody();

        assert body != null;
        Assert.assertTrue(body.length > 0);
    }

    @Test
    public void testValidPostRequest() throws ServletException, IOException {

        WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + WEB_PORT)
                .build()
                .method(HttpMethod.POST)
                .uri("/create-task")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("title", "Task #3").with("priority", "MEDIUM"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody().isEmpty();
    }

    @Test
    public void testInvalidPostRequest() {

        WebTestClient.RequestHeadersSpec<?> requestHeaders = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + WEB_PORT)
                .build()
                .method(HttpMethod.POST)
                .uri("/create-task")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("title", "Task #2").with("priority", "MEDIUM"));

        requestHeaders.exchange();

        byte[] body = requestHeaders.exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/html;charset=UTF-8")
                .expectBody().returnResult().getResponseBody();

        assert body != null;
        Assert.assertTrue(body.length > 0);

        String strBody = new String(body);
        Assert.assertTrue("Expected message 'Task with a given name already exists!'", strBody.contains("Task with a given name already exists!"));
        Assert.assertTrue("Expected value in input field but it was empty!", strBody.contains("value=\"Task #2\""));
        Assert.assertTrue("Expected value in drop-down list but it was empty!", strBody.contains("value=\"MEDIUM\" selected"));
    }

    @Test
    public void testCorrectTaskCreate() throws ServletException, IOException {
        when(request.getParameter("title")).thenReturn("Task #3");
        when(request.getParameter("priority")).thenReturn("MEDIUM");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(taskRepository.create(any(Task.class))).thenReturn(true);

        createTaskServlet.doPost(request, response);

        verify(taskRepository, times(1)).create(any(Task.class));
    }

}
