package com.co.evertec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CargarDatosComponent {

	Logger logger = LoggerFactory.getLogger(CargarDatosComponent.class);
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("leerArchivoDeudaJob")
	Job job;
	
	@EventListener(ApplicationReadyEvent.class)
	public String ejecutarJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		logger.info("inicia job para carga de datos - deuda cliente");
		JobParametersBuilder paramsBuilder = new JobParametersBuilder();
		JobExecution jobExecution = jobLauncher.run(job, paramsBuilder.toJobParameters());
		if(jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
			logger.info("job finalizado correctamente - deuda cliente");
			
		}else {
			logger.info("job finalizado con errores - deuda cliente");
		}
		logger.info(jobExecution.getStatus().toString());
		logger.info("job finalizado - deuda cliente");
		return jobExecution.getStatus().toString();
	}
}
