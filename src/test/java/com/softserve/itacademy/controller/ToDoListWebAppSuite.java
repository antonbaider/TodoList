package com.softserve.itacademy.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateTaskServletTest.class,
        ReadTaskServletTest.class,
        UpdateTaskServletTest.class,
        DeleteTaskServletTest.class,
        TasksListServletTest.class
})
public class ToDoListWebAppSuite {  }
