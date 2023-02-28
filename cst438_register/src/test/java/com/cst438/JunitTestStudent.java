package com.cst438;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {
	String testEmail = "rlampkin@csumb.edu";
	String testName = "Ryan Lampkin";
	
	@MockBean
	StudentRepository studentRepository;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void addStudent() throws Exception {
		MockHttpServletResponse response;
		
		Student testStudent = new Student();
		testStudent.setEmail(testEmail);
		testStudent.setName(testName);
		
		when(studentRepository.findByEmail(testEmail)).thenReturn(null);
		when(studentRepository.save(testStudent)).thenReturn(testStudent);
		
		response = mvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ryan Lampkin\", \"email\":\"rlampkin@csumb.edu\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		Student result = fromJsonString(response.getContentAsString(), Student.class);
		assertEquals(0, result.getStudent_id());
        verify(studentRepository).save(any(Student.class));
	}
	
	@Test
	public void addHoldTest() throws Exception {
		MockHttpServletResponse response;
		
		Student testStudent = new Student();
		testStudent.setEmail(testEmail);
		testStudent.setName(testName);
		testStudent.setStatusCode(0);
		testStudent.setStatus(null);
		
		when(studentRepository.findByEmail(testEmail)).thenReturn(testStudent);
	    when(studentRepository.save(testStudent)).thenReturn(testStudent);

        response = mvc.perform(post("/student/addHold")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ryan Lampkin\", \"email\":\"rlampkin@csumb.edu\", \"statusCode\":\"0\", \"status\":\"null\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        
        assertEquals(200, response.getStatus());
        
        Student result = fromJsonString(response.getContentAsString(), Student.class);
        assertEquals("Hold", result.getStatus());
        assertEquals(1, result.getStatusCode());
	}
	
	@Test
	public void removeHoldTest() throws Exception {
		MockHttpServletResponse response;
		
		Student testStudent = new Student();
		testStudent.setEmail(testEmail);
		testStudent.setName(testName);
		testStudent.setStatusCode(1);
		testStudent.setStatus("Hold");
		
		when(studentRepository.findByEmail(testEmail)).thenReturn(testStudent);
	    when(studentRepository.save(testStudent)).thenReturn(testStudent);
	    
	    response = mvc.perform(post("/student/removeHold")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ryan Lampkin\", \"email\":\"rlampkin@csumb.edu\", \"statusCode\":\"1\", \"status\":\"Hold\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
	    
	    assertEquals(200, response.getStatus());
        
        Student result = fromJsonString(response.getContentAsString(), Student.class);
        assertEquals(null, result.getStatus());
        assertEquals(0, result.getStatusCode());
	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
