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
java.lang.IllegalStateException: org.hibernate.TransientObjectException: object references an unsaved transient instance of 'org.jproject.domain.TFileHist' (persist the transient instance before flushing)
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:143)
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:168)
	at org.hibernate.query.sqm.internal.SqmQueryImpl.executeUpdate(SqmQueryImpl.java:489)
	at org.jproject.dao.DaoWorker.deleteFlegFleh(DaoWorker.java:290)
	at org.jproject.service.FileAddService.preAction(FileAddService.java:20)
	at org.jproject.service.base.BaseFileActionService.updateFile(BaseFileActionService.java:181)
	at org.jproject.service.base.BaseFileActionService.apply(BaseFileActionService.java:77)
	at org.jproject.service.FileScanService.lambda$action$3(FileScanService.java:44)
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
	at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:575)
	at java.base/java.util.stream.AbstractPipeline.evaluateToArrayNode(AbstractPipeline.java:260)
	at java.base/java.util.stream.ReferencePipeline.toArray(ReferencePipeline.java:616)
	at java.base/java.util.stream.ReferencePipeline.toArray(ReferencePipeline.java:622)
	at java.base/java.util.stream.ReferencePipeline.toList(ReferencePipeline.java:627)
	at org.jproject.service.FileScanService.action(FileScanService.java:47)
	at org.jproject.service.base.BaseProcessActionService.lambda$apply$6(BaseProcessActionService.java:52)
	at org.jproject.utils.DaoUtils.withTransaction(DaoUtils.java:27)
	at org.jproject.service.base.BaseProcessActionService.apply(BaseProcessActionService.java:35)
	at org.jproject.service.FileScanService.run(FileScanService.java:56)
	at org.springframework.scheduling.support.DelegatingErrorHandlingRunnable.run(DelegatingErrorHandlingRunnable.java:54)
	at org.springframework.scheduling.concurrent.ReschedulingRunnable.run(ReschedulingRunnable.java:96)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:577)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
	at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	at java.base/java.lang.Thread.run(Thread.java:1623)
Caused by: org.hibernate.TransientObjectException: object references an unsaved transient instance of 'org.jproject.domain.TFileHist' (persist the transient instance before flushing)
	at org.hibernate.metamodel.mapping.EntityIdentifierMapping.getIdentifierIfNotUnsaved(EntityIdentifierMapping.java:117)
	at org.hibernate.metamodel.mapping.internal.SimpleForeignKeyDescriptor.getAssociationKeyFromSide(SimpleForeignKeyDescriptor.java:503)
	at org.hibernate.metamodel.mapping.ForeignKeyDescriptor.getAssociationKeyFromSide(ForeignKeyDescriptor.java:147)
	at org.hibernate.query.sqm.internal.SqmUtil.getIdentifier(SqmUtil.java:735)
	at org.hibernate.query.sqm.internal.SqmUtil.bindValue(SqmUtil.java:722)
	at org.hibernate.query.sqm.internal.SqmUtil.createValueBindings(SqmUtil.java:704)
	at org.hibernate.query.sqm.internal.SqmUtil.createJdbcParameterBinding(SqmUtil.java:649)
	at org.hibernate.query.sqm.internal.SqmUtil.createJdbcParameterBindings(SqmUtil.java:526)
	at org.hibernate.query.sqm.internal.SimpleNonSelectQueryPlan.buildInterpretation(SimpleNonSelectQueryPlan.java:178)
	at org.hibernate.query.sqm.internal.SimpleDeleteQueryPlan.buildInterpretation(SimpleDeleteQueryPlan.java:65)
	at org.hibernate.query.sqm.internal.SimpleNonSelectQueryPlan.getInterpretation(SimpleNonSelectQueryPlan.java:117)
	at org.hibernate.query.sqm.internal.SimpleNonSelectQueryPlan.executeUpdate(SimpleNonSelectQueryPlan.java:54)
	at org.hibernate.query.sqm.internal.SqmQueryImpl.doExecuteUpdate(SqmQueryImpl.java:509)
	at org.hibernate.query.sqm.internal.SqmQueryImpl.executeUpdate(SqmQueryImpl.java:481)
	... 27 more


 */