package org.jproject.service.base;

import jakarta.transaction.NotSupportedException;
import org.jproject.domain.FlegFleh;
import org.jproject.domain.TFileGroup;

import java.util.List;

public interface IBaseGroupActionService {

    List<FlegFleh.PK> apply() throws NotSupportedException;
}
