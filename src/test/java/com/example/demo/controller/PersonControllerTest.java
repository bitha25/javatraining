package com.example.demo.controller;
import com.example.demo.dtoPattern.PersonDto;
import com.example.demo.entities.DepartmentEntity;
import com.example.demo.entities.PersonEntity;
import com.example.demo.exception.PersonNotFoundException;
import com.example.demo.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonControllerTest {
    private static final String PATH = "/persons";
    private MockMvc mockMvc;
    private String requestJson;
    @InjectMocks
    private PersonController personController;
    @Mock
    private PersonService service;

    @Before
    public void setUp() throws Exception {
        File file = ResourceUtils.getFile("src/test/resources/persons.json");
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
        ObjectMapper mapper = new ObjectMapper();
        PersonDto orderRequestDto = mapper.readValue(file, PersonDto.class);
        requestJson = mapper.writeValueAsString(orderRequestDto);
    }

    @Test
    public void givenValidDto_savePersons_expectSuccess() throws Exception {
        this.mockMvc
                .perform(post("/person")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getAllPersons_expectSuccess() throws Exception {
        List<PersonDto> personDtoList = new ArrayList<>();
        PersonDto personDto = new PersonDto();
        personDto.setId(5);
        personDto.setJob("job");
        personDto.setName("Marius");
        personDto.setDepartmentName("depart");
        personDto.setDepartmentId(2);
        personDtoList.add(personDto);

        Mockito.when(service.getAllPersons()).thenReturn(personDtoList);
        mockMvc.perform(get(PATH)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(personDto.getId()),Long.class))
                .andExpect(jsonPath("$[0].name", Matchers.is("Marius")))
                .andExpect(jsonPath("$[0].job", Matchers.is("job")))
                .andExpect(jsonPath("$[0].department_id", Matchers.is(2)))
                .andExpect(jsonPath("$[0].department_name", Matchers.is("depart")))
                .andReturn();
        verify(service,times(1)).getAllPersons();
        verifyNoMoreInteractions(service);
    }
    @Test
    public void getAllPersons_expectNotFound() throws Exception {
        Mockito.when(service.getAllPersons()).thenThrow(new PersonNotFoundException(Integer.MAX_VALUE));
        mockMvc.perform(get(PATH)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
        verify(service).getAllPersons();
    }
    @Test
    public void getPersonById_expectSuccess() throws Exception {
        PersonDto personDto = new PersonDto();
        personDto.setId(5);
        personDto.setJob("job");
        personDto.setName("Marius");
        personDto.setDepartmentName("depart");
        personDto.setDepartmentId(2);

        Mockito.when(service.getPersonById(5)).thenReturn(personDto);
        mockMvc.perform(get("/person" + "/5")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(5)))
                .andExpect(jsonPath("$.name",is("Marius")))
                .andReturn();

        verify(service, times(1)).getPersonById(personDto.getId());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void getPersonById_expectNotFound() throws Exception {
        //cand intalnim linia service cu id 5...
        when(service.getPersonById(5)).thenThrow(new PersonNotFoundException(5));
        //Creem calea
        mockMvc.perform(get("/person" + "/5")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
        verify(service,times(1)).getPersonById(5);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void deleteTest_expectSuccess() throws Exception{
        mockMvc.perform(delete("/person/{id}","5")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        verify(service,times(1)).deletePersonById(5);
        verifyNoMoreInteractions(service);
    }
//    @Test
//    public void deleteTest_expectNotFound() throws Exception{
//        PersonDto personDto = new PersonDto();
//        personDto.setId(Integer.MAX_VALUE);
//        personDto.setName("user not found");
//        Mockito.when(service.deletePersonById(Integer.MAX_VALUE)).thenThrow(new PersonNotFoundException(Integer.MAX_VALUE));
//        mockMvc.perform(
//                delete("/person/{id}",personDto.getId()))
//                .andExpect(status().isNotFound());
//    }


    @Test
    public void testUpdatePerson_expectSuccess() throws Exception{
        mockMvc.perform(put("/person/{id}","5")
                .contentType(MediaType.asMediaType(APPLICATION_JSON))
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        verify(service,times(1)).updatePerson(anyLong(),any(PersonDto.class));
        verifyNoMoreInteractions(service);
    }



    @Test
    public void testUpdatePerson_expectNotFound() throws Exception {
        mockMvc.perform(get("/person/" + "11")
                .content(requestJson)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
    }
}