package org.jproject.parameters.process;

import org.jproject.domain.EProcessType;
import org.jproject.dto.parameters.DtoScanFileParameters;
import org.jproject.parameters.process.base.BaseProcessParam;

import java.util.List;

public class VacuumProcessParameters extends BaseProcessParam {

    public VacuumProcessParameters() {
        super(EProcessType.VACUUM);
    }

}
