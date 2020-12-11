package com.example.demo.dtoPattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonDto {
    private String name;
    private String job;
    private long id;
    @JsonIgnore
    @JsonProperty("department_name")
    private String departmentName;
    @JsonProperty("department_id")
    private long departmentId;
    private long age;
}
