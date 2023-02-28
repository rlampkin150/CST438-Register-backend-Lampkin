package com.cst438.controller;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

@RestController
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;
	
	@PostMapping("/student")
	@Transactional
	public Student addStudent(@RequestBody StudentDTO studentDTO) {
		Student newStudent = studentRepository.findByEmail(studentDTO.getEmail());
		
		if (newStudent == null) {
			newStudent = new Student();
			newStudent.setEmail(studentDTO.getEmail());
			newStudent.setName(studentDTO.getName());
			studentRepository.save(newStudent);
			return newStudent;
		}
		else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student already exists, or invalid input " + studentDTO);
		}
	}
	
	@PostMapping("/student/addHold")
	@Transactional
	public Student addHold(@RequestBody StudentDTO studentDTO) {
		Student student = studentRepository.findByEmail(studentDTO.getEmail());
		
		if (student != null) {
			student.setStatusCode(1);
			student.setStatus("Hold");
			studentRepository.save(student);
			return student;
		}
		else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student does not exist, or already has hold " + studentDTO);
		}
	}
	
	@PostMapping("/student/removeHold")
	@Transactional
	public Student removeHold(@RequestBody StudentDTO studentDTO) {
		Student student = studentRepository.findByEmail(studentDTO.getEmail());
		
		if (student != null) {
			student.setStatusCode(0);
			student.setStatus(null);
			return student;
		}
		else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student does not exist, or does not have hold " + studentDTO);
		}
	}
}