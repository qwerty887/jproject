package org.jproject.parameters.process;

import org.jproject.domain.EProcessType;
import org.jproject.parameters.process.base.BaseProcessParam;

public class FileVerificationProcessParameters extends BaseProcessParam {

    Integer fileHistId;

    public FileVerificationProcessParameters() {
        super(EProcessType.FILE_VERIFICATION);
    }

    public FileVerificationProcessParameters(Integer fileHistId) {
        super(EProcessType.FILE_VERIFICATION);
        this.fileHistId = fileHistId;
    }

    public Integer getFileHistId() {
        return fileHistId;
    }

    public void setFileHistId(Integer fileHistId) {
        this.fileHistId = fileHistId;
    }
}
