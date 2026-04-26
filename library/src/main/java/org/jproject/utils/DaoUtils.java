package org.jproject.utils;

import jakarta.persistence.EntityTransaction;
import org.jproject.dao.DaoWorker;
import org.jproject.dao.IPureCloseable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DaoUtils {

    public static <T, D extends DaoWorker> T withTransaction(Supplier<D> daoBuilder, Function<D, ? extends T> transactionCalculation) {
        try(D dao = daoBuilder.get()) {
            final EntityTransaction tx = dao.getEntityManager().getTransaction();
            if (!tx.isActive())  {
                final IPureCloseable conditionalRollback = () -> {
                    if (tx.isActive()) {
                        tx.rollback();
                    }
                };

                tx.begin();
                try (IPureCloseable unused2 = conditionalRollback) {
                    T result;
                    try {
                        result = transactionCalculation.apply(dao);
                        tx.commit();
                    } catch (RuntimeException e) {
                        throw e;
                    }
                    return result;
                }
            }
            else {
                try {
                    return transactionCalculation.apply(dao);
                } catch (RuntimeException e) {
                    throw e;
                }
            }
        }
    }

    public static <T, D extends DaoWorker> void withTransactionVoid(Supplier<D> daoBuilder, Consumer<D> transactionAction) {
        withTransaction(
                daoBuilder,
                dao -> {
                    transactionAction.accept(dao);
                    return null;
                }
        );
    }

}
