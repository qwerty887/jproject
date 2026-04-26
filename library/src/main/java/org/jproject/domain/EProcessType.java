package org.jproject.domain;

import java.util.HashMap;
import java.util.Map;

public enum EProcessType {
    UNKNOWN(0),
    FILE_FETCHING(1),
    FILE_SCANNING(2),
    FILE_VERIFICATION(3),
    FILE_GROUPING(4),
    FILE_LINKING(5),
    ;

    private final int id;
    private final static Map<Integer, EProcessType> idToEnum = new HashMap<>(EProcessType.values().length);

    static {
        for (EProcessType val : values()) {
            idToEnum.put(val.getId(), val);
        }
    }

    EProcessType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EProcessType of(int id) {
        EProcessType result = idToEnum.get(id);
        if (result == null) {
            throw  new IllegalArgumentException("Invalid process type id: " + id);
        }
        return result;
    }
}
