package org.jproject.domain;

import java.util.HashMap;
import java.util.Map;

public enum EFileType {
    UNKNOWN(0),
    FILE(1),
    DIRECTORY(2),
    LINK(3),
    ;

    private final int id;
    private final static Map<Integer, EFileType> idToEnum = new HashMap<>(EFileType.values().length);

    static {
        for (EFileType val : values()) {
            idToEnum.put(val.getId(), val);
        }
    }

    EFileType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EFileType of(int id) {
        EFileType result = idToEnum.get(id);
        if (result == null) {
            throw  new IllegalArgumentException("Invalid file type id: " + id);
        }
        return result;
    }
}
