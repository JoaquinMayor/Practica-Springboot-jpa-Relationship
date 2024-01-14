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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_alumnos_cursos", joinColumns = @JoinColumn(name="alumno_id"),//Esto es para determinar la tabla intermedia dentro de la base de datos, luego en el JounColum ponemos el nombre del atributo que en la tabla de la id de la propia tabla
    inverseJoinColumns = @JoinColumn(name="curso_id"), //Aquí ponemos con el que lo queremos enlazar
    uniqueConstraints = @UniqueConstraint(columnNames = {"alumno_id","curso_id"}))  //Se determina que no se puedan repetir esa combinación
    private Set<Course> courses;

    public Student() {
        this.courses = new HashSet<>();
    }

    public Student(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
        this.courses = new HashSet<>();
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

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course){
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(Course course){
        this.courses.remove(course);
        course.getStudents().remove(this);
    }

    @Override
    public String toString() {
        return "{id=" + id + ", name=" + name + ", lastname=" + lastname + ", courses=" + courses + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
        result = prime * result + ((courses == null) ? 0 : courses.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (lastname == null) {
            if (other.lastname != null)
                return false;
        } else if (!lastname.equals(other.lastname))
            return false;
        if (courses == null) {
            if (other.courses != null)
                return false;
        } else if (!courses.equals(other.courses))
            return false;
        return true;
    }

    

    
}
