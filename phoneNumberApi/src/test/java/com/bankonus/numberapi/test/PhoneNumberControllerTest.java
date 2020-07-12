package com.bankonus.numberapi.test;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bankonus.numberapi.controller.PhoneNumberController;
import com.bankonus.numberapi.dao.PhoneNumberDao;
import com.bankonus.numberapi.model.PhoneNumbers;

import junit.framework.Assert;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnit4.class)
@ExtendWith(SpringExtension.class)
public class PhoneNumberControllerTest {

	// @Autowired
	// private MockMvc mockMvc;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@InjectMocks
	PhoneNumberController employeeController;

	@Mock
	private PhoneNumberDao repo;

	@SuppressWarnings("deprecation")
	@Test
	public void getAllPhoneNumbers() throws Exception {

		PhoneNumbers employee1 = new PhoneNumbers(1, 1111, 1234567890, "Active");
		PhoneNumbers employee2 = new PhoneNumbers(2, 2222, 1234567890, "Active");
		List<PhoneNumbers> employees = new ArrayList<PhoneNumbers>();
		employees.add(employee1);
		employees.add(employee2);

		when(repo.findAll()).thenReturn(employees);

		// when
		List<PhoneNumbers> result = employeeController.getAllPhoneNumbers();

		// then
		Assert.assertEquals(2, result.size());

		Assert.assertTrue(result.get(0).getPhoneId() == employee1.getPhoneId());

		Assert.assertTrue(result.get(1).getPhoneId() == employee2.getPhoneId());
	}

	@Test
	public void getParticularPhoneNumber() throws Exception {

		PhoneNumbers employee1 = new PhoneNumbers(1, 1111, 1234567890, "Active");
		PhoneNumbers employee2 = new PhoneNumbers(2, 1111, 1234567890, "Active");
		PhoneNumbers employee3 = new PhoneNumbers(3, 2222, 1234567890, "Active");
		List<PhoneNumbers> employees = new ArrayList<PhoneNumbers>();
		employees.add(employee1);
		employees.add(employee2);
		// employees.add(employee3);

		when(repo.findByCustomerID(1111)).thenReturn(employees);

		// when
		List<PhoneNumbers> result = employeeController.getAllPhoneNumbers(1111);

		// then
		Assert.assertEquals(2, result.size());

		Assert.assertTrue(result.get(0).getPhoneId() == employee1.getPhoneId());

		Assert.assertTrue(result.get(1).getPhoneId() == employee2.getPhoneId());
	}

	@Test
	public void activatePhoneNumber() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		//when(repo.save(any(PhoneNumbers.class))).thenReturn(true);
		PhoneNumbers pn = new PhoneNumbers(1, 1111, 1234567890, "Active");
		when(repo.updateStatus(pn.getStatus(), pn.getCustomerID(), pn.getPhoneNumber())).thenReturn(1);
		ResponseEntity<PhoneNumbers> responseEntity = employeeController.updatePhoneNumber(pn);

		Assert.assertEquals(responseEntity.getStatusCode(),HttpStatus.CREATED);
	}
}
