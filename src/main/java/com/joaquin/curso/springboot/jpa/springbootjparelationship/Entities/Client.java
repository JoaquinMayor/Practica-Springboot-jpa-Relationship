package com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //Un cliente tenga muchas Direcciones (CascadeType)Cuando se crea un cliente tambien la dirección o cuando se elimina, el orphan es para eliminar cualquier dato que no quedo asociado a un cliente
    //@JoinColumn(name = "client_id") //Esto hace que se maneje la fk con este dato de la tabla, y evita que se genera una tabla intermedia
    @JoinTable(name = "tbl_clientes_to_direcciones", //Sirve para manejar nosotros de tablas intermedias ya existentes, se pone el nombre de la tabla,
    joinColumns = @JoinColumn(name="id_cliente"), //luego el nombre de la id del entity individual
    inverseJoinColumns = @JoinColumn(name = "id_direccion"), //y luego de los entitys que estan asociados a este
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_direcciones"}))  //, el final determina que esa es la única en, que no puede tener otra relación
    private Set<Address> addresses; //Se crea una tabla intermedia, esta lista siempre se tiene que inicializar

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client") //Para hacer el mapeo desde los 2 lados de la facturas hay que hacer el mapeo con mappedBy, igualando al nombre del atributo dentro de factura de la clase cliente y solo se puede poner el JoinColum en la clase que tiene el ManyToOne
    private Set<Invoice> invoices;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
    ClientDetail clientDetail;
    
    public Client() {
        this.addresses = new HashSet<>();
        this.invoices = new HashSet<>();
    }

    public Client(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
        this.addresses = new HashSet<>();
        this.invoices = new HashSet<>();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public ClientDetail getClientDetail() {
        return clientDetail;
    }

    public void setClientDetail(ClientDetail clientDetail) {
        this.clientDetail = clientDetail;
    }

    @Override
    public String toString() { //En los toString no se puede tener en los 2 el toString del otro porque sino se genera un ciclo infinito, solo lo puede tener uno de los 2
        return "{id=" + id + ", name=" + name + ", lastname=" + lastname +", Adresses"+ addresses + ", invoices= " + invoices + ", ClientDetails=" + clientDetail + "}";
    }

    

    

   

    
}
