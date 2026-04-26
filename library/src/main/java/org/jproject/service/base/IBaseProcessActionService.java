package org.jproject.service.base;

import org.jproject.dao.DaoWorker;
import org.jproject.domain.TProcess;

public interface IBaseProcessActionService {

    boolean apply();
    int action(DaoWorker dao, TProcess process);
}
