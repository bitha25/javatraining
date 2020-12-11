package com.example.demo;

import com.example.demo.entities.DepartmentEntity;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationStarter implements CommandLineRunner{
    @Autowired
    DepartmentRepository departmentRepository;
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class,args);
    }


    @Override
    public void run(String... args) throws Exception {
        departmentRepository.save(new DepartmentEntity("Gaming"));
        departmentRepository.save(new DepartmentEntity("Systems"));
        departmentRepository.save(new DepartmentEntity("Design"));
        departmentRepository.save(new DepartmentEntity("Security"));
        departmentRepository.save(new DepartmentEntity("Testing"));
    }
}
