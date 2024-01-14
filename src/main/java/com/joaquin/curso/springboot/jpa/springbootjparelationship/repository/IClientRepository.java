package com.joaquin.curso.springboot.jpa.springbootjparelationship.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.Client;


public interface IClientRepository extends CrudRepository<Client, Long>{

    @Query("select c from Client c left join fetch c.addresses where c.id=?1")
    Optional<Client> findOnWthitAddresses(Long id);
    
    @Query("select c from Client c left join fetch c.invoices where c.id=?1") //Para mostrar los ejemplos es necesario comentar el otro valor de la bd en el toString ya que no se puede hacer un join doble, ya que no lo soporta (Esto pasa cuando los datos se guardan en list, para solucionarlo se tienen que guardar en sets)
    Optional<Client> findOneWithInvoices(Long id);

    @Query("select c from Client c left join fetch c.invoices left join fetch c.addresses where c.id=?1")
    Optional<Client> findOne(Long id);

    
}
