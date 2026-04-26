package org.jproject.dao;

import jakarta.persistence.EntityManager;

public interface IDao extends IPureCloseable {
    EntityManager getEntityManager();

    void flush();

}
