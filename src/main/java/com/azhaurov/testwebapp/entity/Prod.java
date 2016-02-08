package com.azhaurov.testwebapp.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="prod")

public class Prod  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID", columnDefinition = "numeric", precision = 11, scale = 0, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "CAT_ID", referencedColumnName = "ID", nullable = false)
    private Cat cat;

    @Column(name = "NAME", columnDefinition = "character", length = 255, nullable = false, unique = true)
    private String name;

    @Column(name = "PRICE", columnDefinition = "decimal", precision = 16, scale = 2, nullable = false)
    private Float price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
