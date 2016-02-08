package com.azhaurov.testwebapp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="cat")
public class Cat  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", length = 11, nullable = false)
    private Long id;

    @OneToMany(mappedBy = "cat", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Prod> products = new HashSet<Prod>();

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

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
}
