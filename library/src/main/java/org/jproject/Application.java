package org.jproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

/*

INSERT INTO process
        (prcd_id, prcd_prcd_id, pctp_pctp_id, pcst_pcst_id, object_count, param, create_date, start_date, end_date, err_err_id)
VALUES(nextval('prcd_seq'), NULL, 1, 1, NULL, '{"processType":"FILE_FETCHING","path":"file:///C:/Users/Рустам/Downloads"}', now(), NULL, NULL, NULL);


INSERT INTO process
(prcd_id, prcd_prcd_id, pctp_pctp_id, pcst_pcst_id, object_count, param, create_date, start_date, end_date, err_err_id)
VALUES(nextval('prcd_seq'), NULL, 4, 1, NULL, '{"processType": "FILE_GROUPING","files": [{"md5": "4a00fe2695728efb9390ae1902cc7bfb","path": "file:///C:/Users/Рустам/Downloads/gtptabs.com/var/www/gtptabs/data/www/gtptabs.com/storage/tabs/18/rammstein/keine_lust_3.gp5"},{"md5": "4a00fe2695728efb9390ae1902cc7bfb","path": "file:///C:/Users/Рустам/Downloads/gtptabs.com/var/www/gtptabs/data/www/gtptabs.com/storage/tabs/18/rammstein/keine_lust_4.gp5"}]}', now(), NULL, NULL, NULL);



select pt."name", ps."name" ,  count(*)
from process p
	inner join process_type pt
		on pt.pctp_id = p.pctp_pctp_id
	inner join process_status ps
		on ps.pcst_id = p.pcst_pcst_id
-- where pt.name = 'file scanning'
group by  pt."name", ps."name";

*/

// https://sendel.ru/posts/liquibase-and-spring-boot/
// https://www.baeldung.com/hibernate-entitymanager
// https://dzone.com/articles/removing-duplicate-code-with-lambda-expressions

// https://www.geeksforgeeks.org/advance-java/jpa-hibernate-entity-manager/


/*
jakarta.persistence.EntityExistsException: A different object with the same identifier value was already associated with this persistence context for entity [org.jproject.domain.FlegFleh with id 'org.jproject.domain.FlegFleh$PK@6e3b']
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:112)
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:168)
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:174)
	at org.hibernate.internal.SessionImpl.firePersist(SessionImpl.java:699)
	at org.hibernate.internal.SessionImpl.persist(SessionImpl.java:675)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
	at java.base/java.lang.reflect.Method.invoke(Method.java:578)
	at org.springframework.orm.jpa.ExtendedEntityManagerCreator$ExtendedEntityManagerInvocationHandler.invoke(ExtendedEntityManagerCreator.java:364)
	at jdk.proxy2/jdk.proxy2.$Proxy122.persist(Unknown Source)
	at org.jproject.dao.DaoBase.persist(DaoBase.java:58)
	at org.jproject.service.base.BaseGroupActionService.apply(BaseGroupActionService.java:60)
	at org.jproject.service.FileGroupService.lambda$action$1(FileGroupService.java:48)
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
	at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:575)
	at java.base/java.util.stream.AbstractPipeline.evaluateToArrayNode(AbstractPipeline.java:260)
	at java.base/java.util.stream.ReferencePipeline.toArray(ReferencePipeline.java:616)
	at java.base/java.util.stream.ReferencePipeline.toArray(ReferencePipeline.java:622)
	at java.base/java.util.stream.ReferencePipeline.toList(ReferencePipeline.java:627)
	at org.jproject.service.FileGroupService.action(FileGroupService.java:56)
	at org.jproject.service.base.BaseProcessActionService.lambda$apply$6(BaseProcessActionService.java:52)
	at org.jproject.utils.DaoUtils.withTransaction(DaoUtils.java:27)
	at org.jproject.service.base.BaseProcessActionService.apply(BaseProcessActionService.java:35)
	at org.jproject.service.FileGroupService.run(FileGroupService.java:64)
	at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54)
	at org.springframework.scheduling.concurrent.ReschedulingRunnable.run(ReschedulingRunnable.java:96)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:577)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	at java.base/java.lang.Thread.run(Thread.java:1623)
Caused by: org.hibernate.NonUniqueObjectException: A different object with the same identifier value was already associated with this persistence context for entity [org.jproject.domain.FlegFleh with id 'org.jproject.domain.FlegFleh$PK@6e3b']
	at org.hibernate.event.internal.AbstractSaveEventListener.entityKey(AbstractSaveEventListener.java:227)
	at org.hibernate.event.internal.AbstractSaveEventListener.performSave(AbstractSaveEventListener.java:214)
	at org.hibernate.event.internal.AbstractSaveEventListener.saveWithGeneratedId(AbstractSaveEventListener.java:133)
	at org.hibernate.event.internal.DefaultPersistEventListener.entityIsTransient(DefaultPersistEventListener.java:144)
	at org.hibernate.event.internal.DefaultPersistEventListener.persist(DefaultPersistEventListener.java:90)
	at org.hibernate.event.internal.DefaultPersistEventListener.onPersist(DefaultPersistEventListener.java:74)
	at org.hibernate.event.internal.DefaultPersistEventListener.onPersist(DefaultPersistEventListener.java:50)
	at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:138)
	at org.hibernate.internal.SessionImpl.firePersist(SessionImpl.java:692)
	... 30 more
 */

/*
select json_array_elements(files)
from (
		select param::json->'files' as files
		from process p
		where p.pctp_pctp_id  = 4
			and p.prcd_id = 87
	)

select json_array_elements(files)::json -> 'path'
from (
		select param::json->'files' as files
		from process p
		where p.pctp_pctp_id  = 4
			and p.prcd_id = 87
	)
 */