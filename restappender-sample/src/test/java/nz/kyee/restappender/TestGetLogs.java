package nz.kyee.restappender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class TestGetLogs {

    @AfterEach
    public void initEach() {
        new Persistency().delete();
    }

    @BeforeEach
    public void initBeforeEach() {
        new Persistency().delete();
    }

    @Test
    public void testValidRequestResponseCode() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "5");
        request.setParameter("level", "OFF");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testInvalidRequestResponseCode1() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet logs_servlet = new LogsServlet();
        logs_servlet.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInvalidRequestResponseCode2() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "0");
        request.setParameter("level", "DEBUG");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet logs_servlet = new LogsServlet();
        logs_servlet.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInvalidRequestResponseCode3() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "10");
        request.setParameter("level", "INVALID");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet logs_servlet = new LogsServlet();
        logs_servlet.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInvalidRequestResponseCode4() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "10");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet logs_servlet = new LogsServlet();
        logs_servlet.doGet(request, response);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testValidContentType() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "10");
        request.setParameter("level", "WARN");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        String type = "application/json";
        assertEquals(type, response.getContentType());
    }

    @Test
    public void testReturnedValues1() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("limit", "1");
        request.setParameter("level", "ALL");
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        String result = response.getContentAsString();

        assertEquals("[]", result);
    }

    @Test
    public void testReturnedValues2() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("limit", "1");
        request.setParameter("level", "FATAL");

        LogsServlet service = new LogsServlet();
        service.doGet(request, response);

        request.setParameter("level", "ERROR");
        service.doGet(request, response);
        request.setParameter("level", "INFO");
        service.doGet(request, response);
        request.setParameter("level", "WARN");
        service.doGet(request, response);
        request.setParameter("level", "DEBUG");
        service.doGet(request, response);
        request.setParameter("level", "OFF");
        service.doGet(request, response);
        request.setParameter("level", "TRACE");
        service.doGet(request, response);

        String result = response.getContentAsString();
        assertEquals("[]", result);
        assertEquals(200, response.getStatus());
    }
}
