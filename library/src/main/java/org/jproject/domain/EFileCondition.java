package org.jproject.domain;

import java.util.HashMap;
import java.util.Map;

public enum EFileCondition {
    UNKNOWN(0),
    REGEXP(1),
    EQUAL(2),
    NOT_EQUAL(3),
    LESS(4),
    GREATER(5),
    ;
    //FILENAME
    //EXT

    // TODO добавить REGEXP сюда

    private final int id;
    private final static Map<Integer, EFileCondition> idToEnum = new HashMap<>(EFileCondition.values().length);

    static {
        for (EFileCondition val : values()) {
            idToEnum.put(val.getId(), val);
        }
    }

    EFileCondition(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EFileCondition of(int id) {
        EFileCondition result = idToEnum.get(id);
        if (result == null) {
            throw  new IllegalArgumentException("Invalid file condition id: " + id);
        }
        return result;
    }
}
