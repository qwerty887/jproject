package org.jproject.service.base;

import org.jproject.domain.TLink;

public interface IBaseLinkActionService {

    TLink apply();
    boolean action(String linkPath);
}
