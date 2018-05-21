package com.asignore.springmvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User {

    private long id;

    private String name;

    private int age;

    private double salary;

    public User() {
        id = 0;
    }

}
