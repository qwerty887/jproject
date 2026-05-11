package org.jproject.process.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import org.jproject.dao.Dao;
import org.jproject.domain.EProcessStatus;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TProcess;
import org.jproject.parameters.process.base.BaseProcessParam;
import org.jproject.exception.NotSupportExceptionApp;
import org.jproject.utils.DaoUtils;
import org.jproject.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseProcessActionService implements IBaseProcessActionService {

    private static final Logger logger = LoggerFactory.getLogger(BaseProcessActionService.class);

    private final EntityManagerFactory entityManagerFactory;
    private final EProcessType processType;

    public BaseProcessActionService(EntityManagerFactory entityManagerFactory, EProcessType processType) {
        this.entityManagerFactory = entityManagerFactory;
        this.processType = processType;
    }

    @Override
    public boolean apply() {
        boolean result = true;

        while (result) {
            result = DaoUtils.withTransaction(
                    () -> new Dao(entityManagerFactory),
                    dao -> {
                        final Integer prcdIdLock = DaoUtils.withTransaction(
                                () -> new Dao(entityManagerFactory),
                                daoIn -> daoIn.getPrcdLock(this.processType)
                                );

                        if (prcdIdLock < 0) { // если не нашли процесс для блокировки, выходим из цикла
                            return false;
                        }

                        logger.info("Process service: lock: prcd_id = " + prcdIdLock);
                        final TProcess process = dao.getProcess(prcdIdLock, EProcessStatus.LOCK)
                                .orElseThrow(() -> new RuntimeException("Process not found: prcd_id = " + prcdIdLock));

                        try {
                            final Integer objectCount = action(dao, process);
                            dao.updateProcessStatus(process, EProcessStatus.COMPLETE, objectCount, null);
                            dao.deleteProcessLock(process);
                            logger.info("Process service: complete: prcd_id = " + prcdIdLock);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("Process service: error: prcd_id = " + prcdIdLock);
                            DaoUtils.withTransactionVoid(
                                    () -> new Dao(entityManagerFactory),
                                    daoIn -> {
                                        daoIn.updateProcessStatus(process, EProcessStatus.ERROR, 0, e.getMessage());
                                        daoIn.deleteProcessLock(process);
                                    }
                            );
                            return true;
                        }
                    }
            );
        }

        return result;
    }

    @Override
    public int action(Dao dao, TProcess process) {
        throw new RuntimeException("You need to override the run action method");
    }

    protected <T extends BaseProcessParam> T getParam(String json, Class<T> clazz) {
        try {
            return (new ObjectMapper()).readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new NotSupportExceptionApp(e.getMessage());
        }
    }

    public <T extends BaseProcessParam> TProcess add(Dao dao, T param) {
        return add(dao, null, param);
    }

    public <T extends BaseProcessParam> TProcess add(Dao dao, TProcess parentProcess, T param) {
        try {
            final TProcess process = new TProcess();
            process.setProcess(parentProcess);
            process.setProcessStatus(EProcessStatus.WAIT);
            process.setProcessType(param.getProcessType());
            process.setParam((new ObjectMapper()).writeValueAsString(param));
            process.setCreateDate(TimeUtils.getCurrentTime());
            process.setAttemptsRemaining(5); // количество попыток обработки - 5
            dao.persist(process);
            return process;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new NotSupportExceptionApp(e.getMessage());
        }
    }

    protected EProcessType getProcessType() {
        return processType;
    }
}
