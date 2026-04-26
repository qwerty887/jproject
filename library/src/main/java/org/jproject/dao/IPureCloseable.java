package org.jproject.dao;

public interface IPureCloseable extends AutoCloseable {

    @Override
    void close();
}
