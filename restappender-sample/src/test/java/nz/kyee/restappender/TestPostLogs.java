package nz.kyee.restappender;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.gson.Gson;

public class TestPostLogs {

    @AfterEach
    public void initEach() {
        new Persistency().delete();
    }

    @BeforeEach
    public void initBeforeEach() {
        new Persistency().delete();
    }

    @Test
    public void testValidRequestResponseCode1() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent logevent = new LogEvent();
        logevent.setLogger("TestValidLogger");
        logevent.setLevel("ALL");
        logevent.setMessage("Test Valid Post Logs");
        logevent.setThread("main");

        String json = new Gson().toJson(logevent);
        request.setContent(json.getBytes());

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals(201, response.getStatus());
    }

    @Test
    public void testInvalidRequestResponseCode1() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInvalidRequestResponseCode2() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent logevent1 = new LogEvent();
        LogEvent logevent2 = new LogEvent();
        logevent1.setId("TestConflict");
        logevent1.setLogger("Logger1");
        logevent1.setLevel("ALL");
        logevent2.setId("TestConflict");
        logevent2.setLogger("Logger2");
        logevent2.setLevel("ALL");

        String jlog1 = new Gson().toJson(logevent1);
        String jlog2 = new Gson().toJson(logevent2);

        request.setContent(jlog1.getBytes());
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        request.setContent(jlog2.getBytes());
        service.doPost(request, response);
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testInvalidRequestResponseCode3() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        LogEvent logevent = new LogEvent();
        logevent.setMessage("TEST");
        logevent.setLevel("InvalidLevel");
        String json = new Gson().toJson(logevent);
        request.setContent(json.getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testValidRequestResponseCode2() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent logevent = new LogEvent();
        logevent.setLogger("Logger1");
        logevent.setLevel("FATAL");
        logevent.setMessage("Test Valid Post Logs");
        logevent.setThread("main");
        String jsonlog1 = new Gson().toJson(logevent);

        LogEvent logevent2 = new LogEvent();
        logevent2.setLogger("Logger2");
        logevent2.setLevel("ERROR");
        logevent2.setMessage("Test second logger");
        logevent.setThread("main");
        String jsonlog2 = new Gson().toJson(logevent2);

        LogEvent logevent3 = new LogEvent();
        logevent3.setLogger("Logger3");
        logevent3.setLevel("INFO");
        logevent3.setMessage("Test third logger");
        logevent.setThread("main");
        String jsonlog3 = new Gson().toJson(logevent3);

        request.setContent(jsonlog1.getBytes());
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals(201, response.getStatus());

        request.setContent(jsonlog2.getBytes());
        service.doPost(request, response);
        assertEquals(201, response.getStatus());

        request.setContent(jsonlog3.getBytes());
        service.doPost(request, response);
        assertEquals(201, response.getStatus());

        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit", "10");
        request2.setParameter("level", "FATAL");

        service.doGet(request2, response2);
        request2.setParameter("level", "ERROR");
        service.doGet(request2, response2);
        request2.setParameter("level", "INFO");
        service.doGet(request2, response2);
        request2.setParameter("level", "OFF");
        service.doGet(request2, response2);
        request2.setParameter("level", "TRACE");
        service.doGet(request2, response2);

        assertEquals(200, response2.getStatus());
    }
}
