package org.jproject.service.base;

import jakarta.transaction.NotSupportedException;
import org.jproject.domain.TFileGroup;

import java.util.List;

public interface IBaseGroupActionService {

    List<TFileGroup> apply() throws NotSupportedException;
}
