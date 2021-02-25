package com.co.evertec.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.co.evertec.entity.DeudaCliente;

public class DaudaClienteITemReader implements ItemReader<DeudaCliente> {

	@Override
	public DeudaCliente read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		// TODO Auto-generated method stub
		return null;
	}

}
