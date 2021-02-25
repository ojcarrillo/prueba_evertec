package com.co.evertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.co.evertec.entity.DeudaCliente;

@Repository
public interface DeudaClienteRepository extends JpaRepository<DeudaCliente, Long> {

}
