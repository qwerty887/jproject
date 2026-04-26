package org.jproject.parameters;

import org.jproject.exception.NotSupportExceptionApp;

import java.util.HashMap;

public class AppParameters {

    private final HashMap<EAppParameters, Object> map = new HashMap<>();

    public AppParameters() {
    }

    public void set(EAppParameters param, Object value) {
        map.put(param, value);
    }

    public <T> T get(EAppParameters paramName, Class<T> clazz) {
        final Object value = map.get(paramName);

        if (clazz.equals(Integer.class)) {
            return (T) (Integer) value;
        }

        if (clazz.equals(String.class)) {
            return (T) (String) value;
        }

        throw new NotSupportExceptionApp("Class " + clazz.getName() + " not support");
    }
}
