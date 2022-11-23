package com.fhdw.loeppe.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@NotEmpty
    private String firstname;

    //@NotEmpty
    private String lastname;

    //@NotEmpty
    private String address;
}
