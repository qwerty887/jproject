package org.jproject.domain;

import java.util.HashMap;
import java.util.Map;

public enum EFileStatus {
    UNKNOWN(0),
    VALID(1),
    INVALID(2),
    MOVE(3),
    DELETE(4),
    UPDATE(5),
    ;

    private final int id;
    private final static Map<Integer, EFileStatus> idToEnum = new HashMap<>(EFileStatus.values().length);

    static {
        for (EFileStatus val : values()) {
            idToEnum.put(val.getId(), val);
        }
    }

    EFileStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EFileStatus of(int id) {
        EFileStatus result = idToEnum.get(id);
        if (result == null) {
            throw  new IllegalArgumentException("Invalid file type id: " + id);
        }
        return result;
    }
}
