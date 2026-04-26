package org.jproject.domain;

import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public enum EFileAttribute {
    UNKNOWN(0, Object.class),
    PATH(1, Path.class),
    ABSOLUTE_PATH(2, String.class),
    CREATION_TIME(3, Instant.class),
    LAST_MODIFIED_TYPE(4, Instant.class),
    // TODO добавить Kb, Mb, Gb, Tb
    BYTES(5, Integer.class),
    CONTENT_TYPE(6, Instant.class),
    FILE_NAME(7, String.class),
    FILE_EXTENSION(8, String.class),
    ;

    private final int id;
    private final Class clazz;
    private final static Map<Integer, EFileAttribute> idToEnum = new HashMap<>(EFileAttribute.values().length);

    static {
        for (EFileAttribute val : values()) {
            idToEnum.put(val.getId(), val);
        }
    }

    EFileAttribute(int id, Class clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public Class getClazz() {
        return clazz;
    }

    public static EFileAttribute of(int id) {
        EFileAttribute result = idToEnum.get(id);
        if (result == null) {
            throw  new IllegalArgumentException("Invalid file attribute id: " + id);
        }
        return result;
    }
}
