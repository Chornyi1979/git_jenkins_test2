package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HelloWorldServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Test
    public void testDoPost() throws ServletException, IOException {
    // Arrange
    when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

    // Act
    new HelloWorldServlet().doPost(request, response);

    // Assert
    verify(requestDispatcher).forward(request, response);
    }
}
