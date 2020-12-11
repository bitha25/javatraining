package com.example.demo.service;

import com.example.demo.exception.DepartmentNotFoundException;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.dtoPattern.PersonDto;
import com.example.demo.exception.PersonNotFoundException;
import com.example.demo.entities.PersonEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {
    private final PersonRepository personRepository;
    private final DepartmentRepository departmentRepository;

    public PersonEntity addPerson(@NonNull PersonDto personDto){
        PersonEntity personEntity = new PersonEntity();
        personEntity.setJob(personDto.getJob());
        personEntity.setName(personDto.getName());
        personEntity.setDepartment(departmentRepository.findById(personDto.getDepartmentId())
                .orElseThrow(DepartmentNotFoundException::new));
        personRepository.save(personEntity);
        return personEntity;
    }

    public List<PersonDto> getAllPersons(){
        return StreamSupport.stream(personRepository.findAll().spliterator(),false)
                .map(this::getPersonDto)
                .collect(Collectors.toList());
    }

    public PersonDto getPersonById(long id){
        PersonEntity personEntity = this.personRepository.findById(id)
                .orElseThrow(()-> new PersonNotFoundException(id));
        PersonDto personDto = new PersonDto();
        setPersonDto(personEntity, personDto);
        return personDto;
    }

    public void deletePersonById(long id){
        Optional<PersonEntity> personEntity = this.personRepository.findById(id);
        if(personEntity.isPresent()){
            this.personRepository.delete(personEntity.get());
        }else{
            throw new PersonNotFoundException(id);
        }
    }


    public void updatePerson(long id, PersonDto newPerson){
        Optional<PersonEntity> optionalPersonEntity = this.personRepository.findById(id);
        if(optionalPersonEntity.isPresent()){
            optionalPersonEntity.get().setName(newPerson.getName());
            optionalPersonEntity.get().setJob(newPerson.getJob());
            optionalPersonEntity.get().setDepartment(departmentRepository
                    .findById(newPerson.getDepartmentId())
                    .orElseThrow(DepartmentNotFoundException::new));
            personRepository.save(optionalPersonEntity.get());
        }else{
            throw new PersonNotFoundException(id);
        }
    }

    private PersonDto getPersonDto(PersonEntity personEntity) {
        PersonDto personDto = new PersonDto();
        setPersonDto(personEntity, personDto);
        return personDto;
    }

    private void setPersonDto(PersonEntity personEntity, PersonDto personDto) {
        personDto.setName(personEntity.getName());
        personDto.setJob(personEntity.getJob());
        personDto.setId(personEntity.getId());
        personDto.setDepartmentName(personEntity.getDepartment().getName());
        personDto.setDepartmentId(personEntity.getDepartment().getId());
    }
}
