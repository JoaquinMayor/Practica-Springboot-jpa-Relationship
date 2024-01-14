package com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String intructor;  
    
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students; 
    
    public Course() {
        this.students = new HashSet<>();
    }   

    public Course(String name, String intructor) {
        this.students = new HashSet<>();
        this.name = name;
        this.intructor = intructor;
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


    public String getIntructor() {
        return intructor;
    }


    public void setIntructor(String intructor) {
        this.intructor = intructor;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
    
    @Override
    public String toString() {
        return "{id=" + id + ", name=" + name + ", intructor=" + intructor + "}";
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((intructor == null) ? 0 : intructor.hashCode());
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
        Course other = (Course) obj;
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
        if (intructor == null) {
            if (other.intructor != null)
                return false;
        } else if (!intructor.equals(other.intructor))
            return false;
        return true;
    }

}
