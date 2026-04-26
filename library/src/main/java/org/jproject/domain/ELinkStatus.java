package org.jproject.domain;

import java.util.HashMap;
import java.util.Map;

public enum ELinkStatus {
    UNKNOWN(0),
    PROCESS(1),
    VALID(2),
    INVALID(3),
    DELETE(4),
    ;

    private final int id;
    private final static Map<Integer, ELinkStatus> idToEnum = new HashMap<>(ELinkStatus.values().length);

    static {
        for (ELinkStatus val : values()) {
            idToEnum.put(val.getId(), val);
        }
    }

    ELinkStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ELinkStatus of(int id) {
        ELinkStatus result = idToEnum.get(id);
        if (result == null) {
            throw  new IllegalArgumentException("Invalid file type id: " + id);
        }
        return result;
    }
}
