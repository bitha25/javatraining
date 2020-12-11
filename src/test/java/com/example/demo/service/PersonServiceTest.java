package com.example.demo.service;
import com.example.demo.dtoPattern.PersonDto;
import com.example.demo.entities.DepartmentEntity;
import com.example.demo.entities.PersonEntity;
import com.example.demo.exception.DepartmentNotFoundException;
import com.example.demo.exception.PersonNotFoundException;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {
    @InjectMocks
    private PersonService service;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private DepartmentRepository departmentRepository;

    @Test
    public void getAllPersons_expectSuccess(){
        List<PersonEntity> personEntityList = new ArrayList<>();
        PersonEntity personEntity = new PersonEntity();
        DepartmentEntity departmentEntity =new DepartmentEntity();
        departmentEntity.setId(5);
        departmentEntity.setName("Departament");
        personEntity.setId(8);
        personEntity.setName("Gabi");
        personEntity.setDepartment(departmentEntity);
        personEntity.setJob("job");
        personEntityList.add(personEntity);

        when(personRepository.findAll()).thenReturn(personEntityList);

        List<PersonDto> result = service.getAllPersons();
        assertEquals(personEntityList.size(),result.size());
        assertEquals(personEntity.getId(),result.get(0).getId());
    }
    @Test
    public void testGetPersonById(){
        PersonEntity personEntity = new PersonEntity();
        DepartmentEntity departmentEntity = new DepartmentEntity();

        personEntity.setId(8);
        personEntity.setName("Gabi");

        departmentEntity.setName("Security");
        departmentEntity.setId(5);
        personEntity.setDepartment(departmentEntity);
        when(personRepository.findById(8L)).thenReturn(Optional.of(personEntity));

        PersonDto result =service.getPersonById(8);
        assertEquals(personEntity.getId(),result.getId());
        assertEquals(personEntity.getName(),result.getName());
    }

    @Test(expected = PersonNotFoundException.class)
    public void getPersonById_NotFound(){
        PersonEntity personEntity = new PersonEntity();
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setId(2);
        departmentEntity.setName("dep");
        personEntity.setName("Gabi");
        personEntity.setId(8);
        personEntity.setDepartment(departmentEntity);
        when(personRepository.findById(8L)).thenReturn(Optional.empty());

        service.getPersonById(8);

    }

    @Test
    public void addPerson(){
        when(departmentRepository.findById(5L)).thenReturn(Optional.of(new DepartmentEntity("")));
        PersonDto personDto = new PersonDto();
        personDto.setId(8);
        personDto.setName("Gabi");
        personDto.setDepartmentId(5);
        personDto.setJob("job");

        PersonEntity personEntity = new PersonEntity();
        personEntity.setName("Gabi");
        when(personRepository.save(Mockito.any(PersonEntity.class))).thenReturn(personEntity);
        PersonEntity result = service.addPerson(personDto);

        assertEquals(personDto.getName(),result.getName());

    }

    @Test(expected = DepartmentNotFoundException.class)
    public void addPerson_expectException(){
        when(departmentRepository.findById(2L)).thenReturn(Optional.empty());

        service.addPerson(new PersonDto());
    }

    @Test
    public void deletePerson(){
        PersonEntity personEntity = new PersonEntity();
        when(personRepository.findById(5L)).thenReturn(Optional.of(personEntity));

        service.deletePersonById(5L);
        verify(personRepository,times(1)).delete(personEntity);
    }

    @Test(expected = PersonNotFoundException.class)
    public void deletePerson_expectException(){
        when(personRepository.findById(3L)).thenReturn(Optional.empty());
        service.deletePersonById(3L);
    }

    @Test
    public void updatePerson(){
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName("Gabi");
        personEntity.setId(5L);
        personEntity.setJob("jobulet");
        PersonDto personDto = new PersonDto();
        personDto.setName(personEntity.getName());
        personDto.setJob(personEntity.getJob());
        personDto.setId(personEntity.getId());
        personDto.setDepartmentId(2);
        when(personRepository.findById(5L)).thenReturn(Optional.of(personEntity));
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(new DepartmentEntity()));

        service.updatePerson(5L,personDto);

        verify(personRepository).save(any(PersonEntity.class));
    }

    @Test(expected = PersonNotFoundException.class)
    public void updatePerson_personException(){
        when(personRepository.findById(2L)).thenReturn(Optional.empty());

        service.updatePerson(2L,new PersonDto());

    }

    @Test(expected = DepartmentNotFoundException.class)
    public void updatePerson_departmentException(){
        when(personRepository.findById(5L)).thenReturn(Optional.of(new PersonEntity()));
        service.updatePerson(5L,new PersonDto());
        verify(departmentRepository,times(1)).save(new DepartmentEntity());
        verify(personRepository,times(1)).save(new PersonEntity());
    }
}