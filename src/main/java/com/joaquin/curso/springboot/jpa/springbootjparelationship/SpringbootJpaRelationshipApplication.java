package com.joaquin.curso.springboot.jpa.springbootjparelationship;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.Address;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.Client;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.ClientDetail;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.Course;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.Invoice;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities.Student;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.repository.IClientDetailsRepository;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.repository.IClientRepository;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.repository.ICourseRepository;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.repository.IInvoiceRepository;
import com.joaquin.curso.springboot.jpa.springbootjparelationship.repository.IStrudentRepository;

@SpringBootApplication
public class SpringbootJpaRelationshipApplication implements CommandLineRunner{
	@Autowired
	private IClientRepository clientRepository;
	@Autowired
	private IInvoiceRepository invoiceRepository;
	@Autowired
	private IClientDetailsRepository clientDetailRepository;
	@Autowired
	private IStrudentRepository studentRepository;
	@Autowired
	private ICourseRepository courseRepository;
	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaRelationshipApplication.class, args);
	}
	 
	@Override
	public void run(String... args) throws Exception {
		manyToManyRemoveFindByIdBidireccional();
	}
	//------------------------ManyToOne y OneToMany---------------------------------
	@Transactional
	public void manyToOneCreateClient(){ //Le estamos asignando a una factura un cliente
		Client client  = new Client("John", "Lopez");
		clientRepository.save(client);

		Invoice invoice = new Invoice("Compras de _Oficina", 2000L);
		invoice.setClient(client);
		Invoice invoiceBD = invoiceRepository.save(invoice);
		System.out.println(invoiceBD);
	}

	@Transactional
	public void manyToOneFindByIdClient(){ //Le estamos asignando a una factura un cliente
		Optional<Client> optionalClient  = clientRepository.findById(1L);
		if(optionalClient.isPresent()){
			Client client = optionalClient.orElseThrow();
			Invoice invoice = new Invoice("Compras de _Oficina", 2000L);
			invoice.setClient(client);
			Invoice invoiceBD = invoiceRepository.save(invoice);
			System.out.println(invoiceBD);
		}

	}

	@Transactional
	public void oneToManyCreateClient(){ //Creamos un cliente con direcciones
		Client client = new Client("Fran", "Moras");
		Address address1 = new Address("Av. Colon", 1234);
		Address address2 = new Address("Santa Fe", 2337);
		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		Client clientBD = clientRepository.save(client);
		System.out.println(clientBD);
	}

	@Transactional
	public void oneToManyFindByIdClient(){ //Actualizamos direcciones de un cliente existente
		Optional<Client> optionalClient = clientRepository.findById(2L);

		if(optionalClient.isPresent()){
			Address address1 = new Address("Av. Colon", 1234);
			Address address2 = new Address("Santa Fe", 2337);
			Client client = optionalClient.orElseThrow();
			Set<Address> adresses = new HashSet<>();
			adresses.add(address1);
			adresses.add(address2);
			client.setAddresses(adresses); //Se le pasa así las direcciones a la BD, ya que la busqueda del dato jpa terminó en el findById
			
			clientRepository.save(client);
			System.out.println(client);
		}
		
	}

	@Transactional
	public void removeAddres(){  //Eliminamos una dirección de un de un usuario existente
		Client client = new Client("Fran", "Moras");
		Address address1 = new Address("Av. Colon", 1234);
		Address address2 = new Address("Santa Fe", 2337);
		client.getAddresses().add(address1);
		client.getAddresses().add(address2);

		Client clientBD = clientRepository.save(client);
		System.out.println(clientBD);

		Optional<Client> optionalClient = clientRepository.findById(3L);
		//El último comando del properties ayuda a que funcione el remove en consola, porque da error, pero cuando trabajamos en web esto no sucede
		//Para eliminar también es necesario tener el la clase address un método equals y hashCode para que lo pueda identificar bien
		optionalClient.ifPresent(cli -> {
			cli.getAddresses().remove(address1);
			clientRepository.save(cli);
			System.out.println(cli);
		});
	}

	@Transactional
	public void removeAddressFindById(){ //Actualizamos direcciones de un cliente existente
		Optional<Client> optionalClient = clientRepository.findById(2L);

		if(optionalClient.isPresent()){
			Address address1 = new Address("Av. Colon", 1234);
			Address address2 = new Address("Santa Fe", 2337);
			Client client = optionalClient.orElseThrow();

			Set<Address> adressses = new HashSet<>();
			adressses.add(address2);
			adressses.add(address1);
			client.setAddresses(adressses); //Se le pasa así las direcciones a la BD, ya que la busqueda del dato jpa terminó en el findById
			
			clientRepository.save(client);
			System.out.println(client);

			Optional<Client> optionalClient2 = clientRepository.findOnWthitAddresses(2L); //Al hacer el fetch en el llamado query ya no hace la carga perecesa
			optionalClient2.ifPresent(cli -> {
				cli.getAddresses().remove(address2);
				clientRepository.save(cli);
				System.out.println(cli);
			});
		}
		
	}

	@Transactional
	public void oneToManyInvoiceBidireccional(){ //Creamos un cliente e ingresamos facturas de manera bidireccional
		Client client = new Client("Fran", "Moras");

		Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
		Invoice invoice2 = new Invoice("Compras de oficina", 8000L);

		Set<Invoice> invoices = new HashSet<>(); //Es importante que cuando hacemos la relación bidireccional la factura guarde al cliente y el cliente guarde a la factura
		invoices.add(invoice2);
		invoices.add(invoice1);
		client.setInvoices(invoices);

		invoice1.setClient(client);
		invoice2.setClient(client);

		clientRepository.save(client); //Se pasa para guardar solo al cliente, ya que el cliente contiene las facturas y las facturas e guardan en cascada

		System.out.println(client);
	
	}

	@Transactional
	public void oneToManyInvoiceBidireccionalFindById(){ //Buscamos un cliente e ingresamos facturas de manera bidireccional
		Optional<Client> optionalClient = clientRepository.findOneWithInvoices(1L);

		optionalClient.ifPresent(client ->{
			Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
			Invoice invoice2 = new Invoice("Compras de oficina", 8000L);

			Set<Invoice> invoices = new HashSet<>(); //Es importante que cuando hacemos la relación bidireccional la factura guarde al cliente y el cliente guarde a la factura
			invoices.add(invoice2);
			invoices.add(invoice1);
			client.setInvoices(invoices);

			invoice1.setClient(client);
			invoice2.setClient(client);

			clientRepository.save(client); //Se pasa para guardar solo al cliente, ya que el cliente contiene las facturas y las facturas e guardan en cascada
			System.out.println(client);
		});

	}

	@Transactional
	public void removeInvoiceBidireccionalFindById(){ //Buscamos un cliente y eliminamos la factura de manera bidireccional
		Optional<Client> optionalClient = clientRepository.findOne(1L);

		optionalClient.ifPresent(client ->{
			Invoice invoice1 = new Invoice("Compras de la casa", 5000L);
			Invoice invoice2 = new Invoice("Compras de oficina", 8000L);

			Set<Invoice> invoices = new HashSet<>(); //Es importante que cuando hacemos la relación bidireccional la factura guarde al cliente y el cliente guarde a la factura
			invoices.add(invoice2);
			invoices.add(invoice1);
			client.setInvoices(invoices);

			invoice1.setClient(client);
			invoice2.setClient(client);

			clientRepository.save(client); //Se pasa para guardar solo al cliente, ya que el cliente contiene las facturas y las facturas e guardan en cascada
			System.out.println(client);
		});


		Optional<Client> optionalClientDB = clientRepository.findOne(1L);

		optionalClientDB.ifPresent(client ->{
			Optional<Invoice> invoiceOptional = invoiceRepository.findById(2L);
			invoiceOptional.ifPresent(invoice->{
				client.getInvoices().remove(invoice);
				invoice.setClient(null); //Eliminar el cliente de la factura y la factura del cliente

				clientRepository.save(client); //Siempre guardar solo el cliente porque hace eliminación en cascada
				System.out.println(client);
			});
		});
	}

	//--------------------------------OneToOne-------------------------------------
	@Transactional
	public void oneToOne(){ //Creación de cliente y cliente  oneToOne
		Client client = new Client("Erba","Pura");
		clientRepository.save(client); //Como es una relación uniderteccional en este ejemplo, primero guardamos al cliente por separado

		ClientDetail clientDetail = new ClientDetail(true, 5000);
		clientDetail.setClient(client);
		clientDetailRepository.save(clientDetail); //Aca se entabla la relación
	}

	@Transactional
	public void oneToOneFindById(){//Buscar al cliente y cliente  oneToOne
		Optional<Client> clientOptional = clientRepository.findOne(1L);
		clientOptional.ifPresent(client->{
		 ClientDetail clientDetail = new ClientDetail(true, 5000);
		 clientDetail.setClient(client);
		 clientDetailRepository.save(clientDetail); //Aca se entabla la relación
		System.out.println(clientDetail); //Al ser oneToOne, osea al pasar solo un dato no hace carga perezosa, se obtiene directamente al recuperar el dato ya ya sea del cliente como del clientDetail
		});
	}

	@Transactional
	public void oneToOneBidireccional(){ //Creación de cliente y cliente bidireccional oneToOne
		Client client = new Client("Erba","Pura");
		ClientDetail clientDetail = new ClientDetail(true, 5000);

		client.setClientDetail(clientDetail);
		clientDetail.setClient(client);

		clientRepository.save(client); //Se guarda solo el cliente porque se guarda en cascada

		System.out.println(client);
		
	}

	//---------------------------ManyToMany----------------------------------------------------

	@Transactional
	public void manyToMany(){ //Creación de estudiantes y cursos y se los enlaza
		Student student1 = new Student("Jano","Pura");
		Student student2 = new Student("Elva","Doe");

		Course course1 = new Course("Curso de Java maste","Andrés");
		Course course2 = new Course("Curso de SpringBoot master","Andrés");

		student1.setCourses(Set.of(course1,course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));//Es saveAll porque se guardan varios estudiantes

		System.out.println(student1);
		System.out.println(student2);
	}

	@Transactional
	public void manyToManyFindById(){//Busqueda de estudiantes y cursos y se los enlaza
		Optional<Student> studentOptional1 = studentRepository.findById(1L);
		Optional<Student> studentOptional2 = studentRepository.findById(2L);

		Student student1 = studentOptional1.get();
		Student student2 = studentOptional2.get();

		Course course1 = courseRepository.findById(1L).get();
		Course course2 = courseRepository.findById(2L).get();

		student1.setCourses(Set.of(course1,course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));//Es saveAll porque se guardan varios estudiantes

		System.out.println(student1);
		System.out.println(student2);
	}

	@Transactional
	public void manyToManyRemoveFindById(){ //Busqueda de estudiantes y cursos y se le quita un curso
		Optional<Student> studentOptional1 = studentRepository.findById(1L);
		Optional<Student> studentOptional2 = studentRepository.findById(2L);

		Student student1 = studentOptional1.get();
		Student student2 = studentOptional2.get();

		Course course1 = courseRepository.findById(1L).get();
		Course course2 = courseRepository.findById(2L).get();

		student1.setCourses(Set.of(course1,course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));//Es saveAll porque se guardan varios estudiantes

		System.out.println(student1);
		System.out.println(student2);

		Optional<Student> studentOptionalDB = studentRepository.findOneWithCourses(1L);
		if(studentOptionalDB.isPresent()){
			Student studentDB = studentOptionalDB.get();
			Optional<Course> courseOptionalDB = courseRepository.findById(2L);

			if(courseOptionalDB.isPresent()){
				Course courseDB = courseOptionalDB.get();
				studentDB.getCourses().remove(courseDB);
				studentRepository.save(studentDB);
				
				System.out.println(studentDB);
			}
		}
	}


	@Transactional
	public void manyToManyBidireccional(){ //Creación de estudiantes y cursos y se los enlaza de manera bidireccional
		Student student1 = new Student("Jano","Pura");
		Student student2 = new Student("Elva","Doe");

		Course course1 = new Course("Curso de Java maste","Andrés");
		Course course2 = new Course("Curso de SpringBoot master","Andrés");

		student1.addCourse(course1);
		student1.addCourse(course2);
		student2.addCourse(course2);

		studentRepository.saveAll(Set.of(student1, student2));//Es saveAll porque se guardan varios estudiantes

		System.out.println(student1);
		System.out.println(student2);
	}

	@Transactional
	public void manyToManyRemoveFindByIdBidireccional(){ //Busqueda de estudiantes y cursos y se le quita un curso de manera bidireccional
		Optional<Student> studentOptional1 = studentRepository.findById(1L);
		Optional<Student> studentOptional2 = studentRepository.findById(2L);

		Student student1 = studentOptional1.get();
		Student student2 = studentOptional2.get();

		Course course1 = courseRepository.findById(1L).get();
		Course course2 = courseRepository.findById(2L).get();

		student1.setCourses(Set.of(course1,course2));
		student2.setCourses(Set.of(course2));

		studentRepository.saveAll(Set.of(student1, student2));//Es saveAll porque se guardan varios estudiantes

		System.out.println(student1);
		System.out.println(student2);

		Optional<Student> studentOptionalDB = studentRepository.findOneWithCourses(1L);
		if(studentOptionalDB.isPresent()){
			Student studentDB = studentOptionalDB.get();
			Optional<Course> courseOptionalDB = courseRepository.findById(2L);

			if(courseOptionalDB.isPresent()){
				Course courseDB = courseOptionalDB.get();
				studentDB.removeCourse(courseDB);
				studentRepository.save(studentDB);
				
				System.out.println(studentDB);
			}
		}
	}

}
