package com.example.minikube.demo.kubedemo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.minikube.demo.kubedemo.exception.ResourceNotFoundException;
import com.example.minikube.demo.kubedemo.model.Employee;
import com.example.minikube.demo.kubedemo.repository.EmployeeRepository;
@RestController @CrossOrigin(origins = "*")
public class KubeController {
	@Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
        throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId);
        if(Objects.isNull(employee))
          throw new ResourceNotFoundException("Employee not found for this id :: " + employeeId);
        return ResponseEntity.ok().body(employee);
    }
    
    @GetMapping("/specialEmp")
    public String getSpecEmp1() {
    	String value = System.getenv().getOrDefault("SPECIAL_EMPLOYEE","No Special Employee");
        return value;
    }
    
    @GetMapping("/secretEmp")
    public String getSpecEmp2() {
    	String value = System.getenv().getOrDefault("SECRET_EMPLOYEE","No Secret Employee");
    	return value;
    }
	
    @GetMapping("/getVersion")
    public String getVersionTwo() {
    	String value = "New Version is Running";
    	return value;
    }
    
}
