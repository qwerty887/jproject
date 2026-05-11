package org.jproject.process.base;

import org.jproject.dao.Dao;
import org.jproject.domain.TProcess;

public interface IBaseProcessActionService {

    boolean apply();
    int action(Dao dao, TProcess process);
}
