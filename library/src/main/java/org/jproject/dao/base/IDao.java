package org.jproject.dao.base;

import jakarta.persistence.EntityManager;

public interface IDao extends IPureCloseable {
    EntityManager getEntityManager();

    void flush();

}
