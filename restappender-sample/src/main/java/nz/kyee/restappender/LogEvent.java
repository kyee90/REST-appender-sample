package nz.kyee.restappender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

enum Level {
    ALL,
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL,
    OFF
}

public class LogEvent {
    private String id;
    private String message;
    private String timestamp;
    private String thread;
    private String logger;
    private String level;
    private String errorDetails;
    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public LogEvent() {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();

        LocalDateTime date = LocalDateTime.now();
        String datetime = formatter.format(date);
        timestamp = datetime;

        this.level = "ALL";
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getThread() {
        return this.thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getLogger() {
        return this.logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getErrorDetails() {
        return this.errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
