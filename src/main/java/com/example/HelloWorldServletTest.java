import org.junit.Test;
import org.mockito.Mockito;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.mockito.Mockito.*;

public class HelloWorldServletTest {

    @Test
    public void testDoGet() throws ServletException, IOException {
        // Создание мок-объектов для HttpServletRequest, HttpServletResponse и RequestDispatcher
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        // Установка ожидаемого поведения для методов мок-объектов
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        // Создание экземпляра HelloWorldServlet и вызов метода doGet()
        HelloWorldServlet servlet = new HelloWorldServlet();
        servlet.doGet(request, response);

        // Проверка, что методы были вызваны с правильными аргументами
        verify(request).setAttribute("message", "Hello, World!");
        verify(dispatcher).forward(request, response);
    }
}
