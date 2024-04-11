package nz.kyee.restappender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonObject;

public class Persistency {
    public static List<JsonObject> DB = new ArrayList<JsonObject>();

    public String[] read(int limit, String level) {
        String[] getLogs = new String[0];
        if (DB.isEmpty()) {
            return getLogs;
        }

        List<JsonObject> objlist = new ArrayList<>();

        for (JsonObject obj : DB) {
            String objlev = obj.get("level").getAsString();
            if (level.toUpperCase().equals("FATAL")) {
                if (objlev.equals("FATAL")) {
                    objlist.add(obj);
                }
            }
            if (level.toUpperCase().equals("ERROR")) {
                if (objlev.equals("FATAL") || objlev.equals("ERROR")) {
                    objlist.add(obj);
                }
            }
            if (level.toUpperCase().equals("WARN")) {
                if (objlev.equals("FATAL") || objlev.equals("ERROR") || objlev.equals("WARN")) {
                    objlist.add(obj);
                }
            }
            if (level.toUpperCase().equals("INFO")) {
                if (objlev.equals("FATAL") || objlev.equals("ERROR") || objlev.equals("WARN")
                        || objlev.equals("INFO")) {
                    objlist.add(obj);
                }
            }
            if (level.toUpperCase().equals("DEBUG")) {
                if (objlev.equals("FATAL") || objlev.equals("ERROR") || objlev.equals("WARN") || objlev.equals("INFO")
                        || objlev.equals("DEBUG")) {
                    objlist.add(obj);
                }
            }
            if (level.toUpperCase().equals("TRACE")) {
                if (objlev.equals("ALL")) {
                    continue;
                }
                objlist.add(obj);
            }
            if (level.toUpperCase().equals("ALL")) {
                objlist.add(obj);
            }

        }

        if (limit <= objlist.size()) {
            getLogs = new String[limit];
        } else {
            getLogs = new String[objlist.size()];
        }

        for (int i = 0; i < limit; i++) {
            if (i == objlist.size()) {
                break;
            }
            JsonObject jobj = objlist.get(i);
            getLogs[i] = jobj.toString();
        }
        return getLogs;
    }

    public void create(JsonObject log) {
        DB.add(log);
    }

    public void delete() {
        DB = new ArrayList<JsonObject>();
    }

    public boolean contains(JsonObject jsonobject) {
        return DB.stream().filter(obj -> obj.get("id").getAsString().equals(jsonobject.get("id").getAsString()))
                .findFirst().isPresent();
    }

    public Table<String, String, Integer> stats() {
        Table<String, String, Integer> stats = HashBasedTable.create();

        ArrayList<String> loggers = new ArrayList<>();

        for (JsonObject jobj : DB) {
            loggers.add(jobj.get("logger").getAsString());
        }

        for (String logger : loggers) {
            for (Level level : Level.values()) {
                stats.put(logger, level.toString(), 0);
            }
        }

        for (JsonObject jobj : DB) {
            String templog = jobj.get("logger").getAsString();
            String lev = jobj.get("level").getAsString();
            Integer tempint = stats.get(templog, lev);
            tempint = tempint + 1;
            stats.put(templog, lev, tempint);
        }

        return stats;
    }
}