package org.jproject.domain;

import java.util.HashMap;
import java.util.Map;

public enum EProcessStatus {
    UNKNOWN(0),
    WAIT(1),
    LOCK(2),
    PROCESS(3),
    ERROR(4),
    COMPLETE(5),
    ;

    private final int id;
    private final static Map<Integer, EProcessStatus> idToEnum = new HashMap<>(EProcessStatus.values().length);

    static {
        for (EProcessStatus val : values()) {
            idToEnum.put(val.getId(), val);
        }
    }

    EProcessStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EProcessStatus of(int id) {
        EProcessStatus result = idToEnum.get(id);
        if (result == null) {
            throw  new IllegalArgumentException("Invalid process status id: " + id);
        }
        return result;
    }
}
