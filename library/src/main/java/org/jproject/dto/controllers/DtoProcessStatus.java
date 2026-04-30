package org.jproject.dto.controllers;

import org.jproject.domain.EProcessStatus;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TProcess;

public class DtoProcessStatus {

    Integer id;
    EProcessType processType;
    EProcessStatus processStatus;

    public DtoProcessStatus() {

    }

    public DtoProcessStatus(Integer id, EProcessType processType, EProcessStatus processStatus) {
        this.id = id;
        this.processType = processType;
        this.processStatus = processStatus;
    }

    public static DtoProcessStatus of(TProcess process) {
        return new DtoProcessStatus(process.getId(), process.getProcessType(), process.getProcessStatus());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(EProcessType processType) {
        this.processType = processType;
    }

    public EProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(EProcessStatus processStatus) {
        this.processStatus = processStatus;
    }
}
