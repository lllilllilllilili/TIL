package hello.advanced.trace;

import java.util.UUID;

public class TraceId {

    private String id;
    private int level;
    private static final int start = 0;
    private static final int limit = 8;

    private TraceId() {
        this.id = id;
        this.level = level;
    }

    public TraceId(String id, int level) {
        this.id = createId();
        this.level = 0;
    }

    private String createId() {
        return UUID.randomUUID().toString().substring(start, limit);
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
