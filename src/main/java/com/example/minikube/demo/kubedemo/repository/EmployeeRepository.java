package com.example.minikube.demo.kubedemo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.example.minikube.demo.kubedemo.model.Employee;



@Component
public class EmployeeRepository {

	private static Map<Long, Employee> empMap;
	
	@PostConstruct
	public void init() {
		empMap = new ConcurrentHashMap<>();
		empMap.put(1L, new Employee(1L, "Pradeep", "Singh", "Pradeep.Singh2@gmail.com"));
		empMap.put(2L, new Employee(2L, "Anand", "Zaveri", "Anand.Zaveri@gmail.com"));
		empMap.put(3L, new Employee(3L, "Akhilesh", "Juyal", "Akhilesh.Juyal@gmail.com"));
		empMap.put(4L, new Employee(4L, "Vaibhav", "Jha", "Vaibhav.Jha@outlook.com"));
		empMap.put(5L, new Employee(5L, "Himanshu", "Sharma", "Himanshu.Sharma@yahoo.com"));
		empMap.put(6L, new Employee(6L, "Jignesh", "Mehta", "Jignesh.Mehta@outlook.com"));
	}
	
	public List<Employee> findAll(){
		List<Employee> empList = new ArrayList<>();
		for(Entry<Long, Employee> entry : empMap.entrySet()) {
			empList.add(entry.getValue());
		}
		return empList;
	}
	
	public Employee findById(Long employeeId){
		for(Entry<Long, Employee> entry : empMap.entrySet()) {
			if(entry.getValue().getId() == employeeId) {
				return entry.getValue();				
			}
		}
		return null;
	}
	
	public void delete(Employee employee){
		for(Entry<Long, Employee> entry : empMap.entrySet()) {
			if(entry.getValue().getId() == employee.getId()) {
				empMap.remove(employee.getId());			
			}
		}		
	}
	
	public Employee save(Employee employee){
		empMap.put(employee.getId(), employee);
		return employee;
	}
	
}
