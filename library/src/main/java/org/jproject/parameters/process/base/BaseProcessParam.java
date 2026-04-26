package org.jproject.parameters.process.base;

import org.jproject.domain.EProcessType;

public class BaseProcessParam implements IProcessParam {

    private final EProcessType processType;

    public BaseProcessParam(EProcessType processType) {
        this.processType = processType;
    }

    public EProcessType getProcessType() {
        return processType;
    }

}
