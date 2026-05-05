package org.jproject.process.base;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.TProcess;

public interface IBaseProcessActionService {

    boolean apply();
    int action(DaoWorker dao, TProcess process);
}
