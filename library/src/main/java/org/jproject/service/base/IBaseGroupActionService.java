package org.jproject.service.base;

import jakarta.transaction.NotSupportedException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jproject.domain.FlegFleh;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;

import java.util.List;
import java.util.Map;

public interface IBaseGroupActionService {

    List<TFileGroup> apply() throws NotSupportedException;
}
