package com.example.demo.controller;

import com.example.demo.dtoPattern.PersonDto;
import com.example.demo.entities.PersonEntity;
import com.example.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/person")
    public void addPerson(@RequestBody PersonDto personDto){
        personService.addPerson(personDto);
    }

    @GetMapping("/persons")
    List<PersonDto> getAllPersons(){
        return personService.getAllPersons();
    }

    @GetMapping("/person/{id}")
    public PersonDto getPersonById(@PathVariable("id")int id){

        return personService.getPersonById(id);
    }

    @DeleteMapping("/person/{id}")
    public void deletePersonById(@PathVariable("id")int id){
        personService.deletePersonById(id);
    }

    @PutMapping(path="/person/{id}")
    public void updatePerson(@PathVariable("id")long id,@RequestBody PersonDto personToUpdate){
        personService.updatePerson(id,personToUpdate);
    }
}
