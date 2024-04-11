package nz.kyee.restappender;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LogsServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String limit = req.getParameter("limit");
        String loglevel = req.getParameter("level");

        // null handling
        if (limit == null || loglevel == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int limitnum = Integer.parseInt(limit);
        loglevel = loglevel.toUpperCase();

        // return 400: bad input if invalid level
        String[] levels = { "OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL" };
        Arrays.parallelSort(levels);
        int ind = Arrays.binarySearch(levels, loglevel);
        if (ind < 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // return 400: bad input if limit value is too low/high
        if (limitnum < 1 || limitnum > Integer.MAX_VALUE) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] weblogs;
        weblogs = new Persistency().read(limitnum, loglevel);

        PrintWriter out = resp.getWriter();

        if (weblogs.length < 1) {
            out.write("[]");
            out.close();
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        out.write("[");

        if (weblogs.length == 1) {
            out.write(weblogs[0] + "]");
            out.close();
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        for (int i = 0; i < weblogs.length; i++) {
            out.write(weblogs[i]);
            if (i == weblogs.length - 1) {
                break;
            }
            out.write(",");
        }

        out.write("]");
        out.close();
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletInputStream logpost = req.getInputStream();
        String json = IOUtils.toString(logpost, "UTF-8");
        JsonElement jelem = JsonParser.parseString(json);

        if (!jelem.isJsonObject()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        JsonObject jobj = jelem.getAsJsonObject();

        String level = jobj.get("level").getAsString().toUpperCase();;
        String[] levels = Arrays.stream(Level.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
        boolean contains = Arrays.stream(levels).anyMatch(level::equals);

        if (!contains) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (new Persistency().contains(jobj)) {
            resp.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }

        new Persistency().create(jobj);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        new Persistency().delete();
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}