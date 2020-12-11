package com.example.demo.dtoPattern;


import com.example.demo.TestDtoUtility;
import org.junit.Test;

import java.io.IOException;

public class PersonDtoTest {
    @Test
    public void serialization() throws IOException{
        TestDtoUtility.assertSerialization("persons.json",PersonDto.class);
    }

}