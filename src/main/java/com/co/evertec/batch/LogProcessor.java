package com.co.evertec.batch;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.co.evertec.entity.DeudaCliente;

public class LogProcessor implements ItemProcessor<DeudaCliente, DeudaCliente> {

	public Logger logger = LoggerFactory.getLogger(LogProcessor.class);

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	public DeudaCliente process(DeudaCliente deuda) throws Exception {
		/* validacion de restricciones de los datos leidos */
		Set<ConstraintViolation<DeudaCliente>> errores = validator.validate(deuda);
		/* valida campos de la deuda cliente */
		Boolean valido = errores.isEmpty();
		if(!valido) {
			logger.error("Existen campos vacios/nulos o con formato errado : ");
			logger.error("Deuda con errores :: " + deuda.toString());
			errores.stream()
            	.forEach(violation -> logger.error(violation.getMessage()));
			return null;
		}else {
			logger.info("Deuda a insertar :: " + deuda.toString());
		}
		return deuda;
	}
	
	public void onProcessError(DeudaCliente deuda, Exception e) {
		System.out.println(deuda + " is invalid due to " + e.getMessage() );
	}
	

}
