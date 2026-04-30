package org.jproject.service.base;

import org.jproject.domain.TFile;

public interface IBaseFileActionService {

    TFile apply();
    void action(TFile tfile);
}
