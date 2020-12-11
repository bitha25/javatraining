package com.example.demo.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="department_id")
    private long id;
    @Column(name="name", nullable = false)
    private String name;
    @Column(table="person")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "department")
    private List<PersonEntity> personEntityList;

    public DepartmentEntity(String name) {
        this.name=name;
    }
}
