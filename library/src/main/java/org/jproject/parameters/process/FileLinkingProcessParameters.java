package org.jproject.parameters.process;

import org.jproject.domain.EProcessType;
import org.jproject.parameters.process.base.BaseProcessParam;

public class FileLinkingProcessParameters extends BaseProcessParam {

    // TODO добавить контроль заполнения поля
    Integer fileHistId;

    public FileLinkingProcessParameters() {
        super(EProcessType.FILE_LINKING);
    }

    public FileLinkingProcessParameters(Integer fileHistId) {
        super(EProcessType.FILE_LINKING);
        this.fileHistId = fileHistId;
    }

    public Integer getFileHistId() {
        return fileHistId;
    }

    public void setFileHistId(Integer fileHistId) {
        this.fileHistId = fileHistId;
    }
}
