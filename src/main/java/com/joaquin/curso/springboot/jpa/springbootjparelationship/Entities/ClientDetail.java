package com.joaquin.curso.springboot.jpa.springbootjparelationship.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients_details")
public class ClientDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean premium;
    private Integer points;

    @OneToOne
    @JoinColumn(name="id_cliente") //El join colum en las relaciones bidireccionales va donde se se pone la forignkey
    private Client client;
     
   
    public ClientDetail() {
    }

    public ClientDetail(boolean premium, Integer points) {
        this.premium = premium;
        this.points = points;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean isPremium() {
        return premium;
    }
    public void setPremium(boolean premium) {
        this.premium = premium;
    }
    public Integer getPoints() {
        return points;
    }
    public void setPoints(Integer points) {
        this.points = points;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", premium=" + premium + ", points=" + points + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (premium ? 1231 : 1237);
        result = prime * result + ((points == null) ? 0 : points.hashCode());
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
        ClientDetail other = (ClientDetail) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (premium != other.premium)
            return false;
        if (points == null) {
            if (other.points != null)
                return false;
        } else if (!points.equals(other.points))
            return false;
        return true;
    }

}
