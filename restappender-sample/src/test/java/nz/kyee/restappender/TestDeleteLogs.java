package nz.kyee.restappender;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.gson.Gson;

public class TestDeleteLogs {

    @AfterEach
    public void initEach() {
        new Persistency().delete();
    }

    @BeforeEach
    public void initBeforeEach() {
        new Persistency().delete();
    }

    @Test
    public void testDelete() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent logevent = new LogEvent();
        logevent.setLogger("Logger1");
        logevent.setMessage("TEST");
        logevent.setThread("TestThread");
        logevent.setLevel("DEBUG");

        String json = new Gson().toJson(logevent);
        request.setContent(json.getBytes());

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals(201, response.getStatus());

        MockHttpServletRequest request2 = new MockHttpServletRequest();
        service.doDelete(request2, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testDelete2() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent logevent = new LogEvent();
        logevent.setLogger("Logger1");
        logevent.setMessage("TEST Log Deletion");
        logevent.setThread("main");
        logevent.setLevel("FATAL");

        String json = new Gson().toJson(logevent);
        request.setContent(json.getBytes());

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals(201, response.getStatus());

        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request.setParameter("limit", "10");
        request.setParameter("level", "ALL");
        service.doGet(request, response2);
        assertEquals(200, response2.getStatus());

        MockHttpServletResponse response3 = new MockHttpServletResponse();
        service.doDelete(request, response3);
        assertEquals(200, response3.getStatus());
    }

    @Test
    public void testDelete3() throws IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent logevent = new LogEvent();
        logevent.setLogger("Logger1");
        logevent.setMessage("TEST Log Deletion");
        logevent.setThread("main");
        logevent.setLevel("FATAL");

        String json = new Gson().toJson(logevent);
        request.setContent(json.getBytes());

        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals(201, response.getStatus());

        LogEvent logevent2 = new LogEvent();
        logevent2.setLogger("Logger2");
        logevent2.setMessage("Test Second Object for Deletion");
        logevent2.setThread("main");
        logevent2.setLevel("ERROR");

        String json2 = new Gson().toJson(logevent2);
        request.setContent(json2.getBytes());
        assertEquals(201, response.getStatus());

        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request.setParameter("limit", "10");
        request.setParameter("level", "ALL");
        service.doGet(request, response2);
        assertEquals(200, response2.getStatus());

        MockHttpServletResponse response3 = new MockHttpServletResponse();
        LogsServlet service3 = new LogsServlet();
        service3.doDelete(request, response);
        assertEquals(200, response3.getStatus());
    }
}
