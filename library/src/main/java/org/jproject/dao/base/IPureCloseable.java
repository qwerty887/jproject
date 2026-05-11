package org.jproject.dao.base;

public interface IPureCloseable extends AutoCloseable {

    @Override
    void close();
}
