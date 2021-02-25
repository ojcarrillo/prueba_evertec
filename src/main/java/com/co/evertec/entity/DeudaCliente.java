package com.co.evertec.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="DEUDACLIENTE" ,schema = "EVERTEC")
public class DeudaCliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "El id del cliente no puede ser vacio")
	@Size(min=1, max=15, message = "Longitud invalida para el id del cliente")
	@Column
	private String idCliente;

	@NotNull(message = "El nombre del cliente no puede ser vacio")
	@Size(min=1, max=60, message = "Longitud invalida para el nombre del cliente")
	@Column
	private String nombreCliente;

	@NotNull(message = "El correo del cliente no puede ser vacio")
	@Size(min=1, max=60, message = "Longitud invalida para el correo del cliente")
	@Email(message = "Formato de correo invalido")
	@Column
	private String correoCliente;

	@NotNull(message = "El monto de la deuda no puede ser vacio")
	@Column
	private BigDecimal montoDeuda;

	@NotNull(message = "El id de la deuda no puede ser vacio")
	@Size(min=1, max=15, message = "Longitud invalida para el id de la deuda")
	@Column
	private String idDeuda;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "La fecha de vencimiento no puede ser vacia")
	@Column
	private Date fechaVencimiento;

	public DeudaCliente() {
		super();
	}

	public DeudaCliente(Long id, String idCliente, String nombreCliente, String correoCliente, BigDecimal montoDeuda,
			String idDeuda, Date fechaVencimiento) {
		super();
		this.id = id;
		this.idCliente = idCliente;
		this.nombreCliente = nombreCliente;
		this.correoCliente = correoCliente;
		this.montoDeuda = montoDeuda;
		this.idDeuda = idDeuda;
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getCorreoCliente() {
		return correoCliente;
	}

	public void setCorreoCliente(String correoCliente) {
		this.correoCliente = correoCliente;
	}

	public BigDecimal getMontoDeuda() {
		return montoDeuda;
	}

	public void setMontoDeuda(BigDecimal montoDeuda) {
		this.montoDeuda = montoDeuda;
	}

	public String getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(String idDeuda) {
		this.idDeuda = idDeuda;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	@Override
	public String toString() {
		return "DeudaCliente [idCliente=" + idCliente + ", nombreCliente=" + nombreCliente
				+ ", correoCliente=" + correoCliente + ", montoDeuda=" + montoDeuda + ", idDeuda=" + idDeuda
				+ ", fechaVencimiento=" + fechaVencimiento + "]";
	}

}
