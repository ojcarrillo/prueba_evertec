package com.co.evertec.batch;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.co.evertec.entity.DeudaCliente;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	Logger logger = LoggerFactory.getLogger(BatchConfig.class);
	
	@Autowired
    private JobBuilderFactory jobBuilderFactory;
 
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Value("${ruta.origen.archivo}")    
	private String archivoFuente;
    
	@Value("${cantidad_registro_procesados}")
	private Integer CANTIDAD_REGISTROS_PROCESADOS;
	
	@Value("${cantidad_registro_chunk}")
	private Integer CANTIDAD_REGISTRO_CHUNK; 
	
	@Value("${caracter_reparador}")
	private String SEPARADOR;
 
	@Autowired
	@PersistenceContext(unitName = "masterAEntityManagerFactory")
	private EntityManager entityManagerDestino;
	
	@Autowired
	@Qualifier("masterATransactionManager")
	private PlatformTransactionManager transactionManager;
	
	/* configuracion del job */
    @Bean
    public Job leerArchivoDeudaJob() throws IOException {
        return jobBuilderFactory
                .get("leerArchivoDeudaJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }
 
    /* configuracion unico paso para leer, procesar y escribir las lineas del archivo */
    @Bean
    public Step step() throws IOException {
        return stepBuilderFactory
                .get("step")
                .transactionManager(transactionManager)
                .<DeudaCliente, DeudaCliente>chunk(CANTIDAD_REGISTRO_CHUNK)
                .reader(archivoReader())
                .processor(processor())
                .writer(writer())
                .build();
    }
     
    /* configuracion log de los objetos leidos */
    @Bean
    public ItemProcessor<DeudaCliente, DeudaCliente> processor() {
        return new LogProcessor();
    }
     
    /* configuracion lector de registros */
    @Bean
    public FlatFileItemReader<DeudaCliente> archivoReader() throws IOException {
    	PathResource path = new PathResource(archivoFuente);
        FlatFileItemReader<DeudaCliente> itemReader = new FlatFileItemReader<DeudaCliente>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setResource(path);
        return itemReader;
    }
 
    /* mapper para leer cada linea del archivo */
    @Bean
    public LineMapper<DeudaCliente> lineMapper() {
        DefaultLineMapper<DeudaCliente> lineMapper = new DefaultLineMapper<DeudaCliente>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setStrict(false);
        lineTokenizer.setDelimiter(SEPARADOR);
        lineTokenizer.setNames(new String[] { "idCliente", "nombreCliente", "correoCliente", "montoDeuda", "idDeuda", "fechaVencimiento" });
        BeanWrapperFieldSetMapper<DeudaCliente> fieldSetMapper = new BeanWrapperFieldSetMapper<DeudaCliente>();
        fieldSetMapper.setTargetType(DeudaCliente.class);
        fieldSetMapper.setConversionService(createConversionService());
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
    
    /* para convertir la fecha del formato del archivo */
    public ConversionService createConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(conversionService);
        conversionService.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(String text) {
            	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            	dateFormat.setLenient(false);
                try {
                	/* valida formato de la fecha de vencimiento */
                	if(!text.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})")) {
                		return null;
                	}
                	return dateFormat.parse(text);
				} catch (ParseException e) {
					return null;
				}
            }
        });
        conversionService.addConverter(new Converter<String, BigDecimal>() {
            @Override
            public BigDecimal convert(String text) {
            	/* valida solo numeros positivos para el valor de la deuda */
            	if(!text.matches("[+]?\\d{1,20}(\\.\\d{0,2})?")) {
            		return null;
            	}
                return BigDecimal.valueOf(Double.valueOf(text));
            }
        });
        return conversionService;
    }
 
    /* guarda el registro de la deuda en la base usando jpa */
    @Bean 
    public ItemWriter<DeudaCliente> writer() { 
        JpaItemWriter writer = new JpaItemWriter<DeudaCliente>(); 
        writer.setEntityManagerFactory(entityManagerDestino.getEntityManagerFactory());
        return writer; 
    } 

}

