package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="person")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "person_id")
    private long id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "job",nullable = false)
    private String job;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

}
