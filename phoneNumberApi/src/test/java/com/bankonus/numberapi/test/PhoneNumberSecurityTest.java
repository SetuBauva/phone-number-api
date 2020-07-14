package com.bankonus.numberapi.test;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.bankonus.numberapi.config.SpringSecurityConfig;
import com.bankonus.numberapi.dao.PhoneNumberDao;
import com.bankonus.numberapi.model.PhoneNumbers;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = SpringSecurityConfig.class)
@AutoConfigureMockMvc
public class PhoneNumberSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PhoneNumberDao phoneNumberDao;

	List<PhoneNumbers> phoneNumber;

	@Before
	public void init() {
		phoneNumber = new ArrayList<PhoneNumbers>();
		PhoneNumbers phoneNumber1 = new PhoneNumbers(101, 1, 1234567890, "InActive");
		phoneNumber.add(phoneNumber1);
	}

	@WithMockUser("USER")
	@Test
	public void getPhoneNumberForCustomerSuccess() throws Exception {

		when(phoneNumberDao.findByCustomerID(1)).thenReturn(phoneNumber);

		mockMvc.perform(get("/getPhoneNumbers/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].customerID", is(1))).andExpect(jsonPath("$[0].phoneNumber", is(1234567890)))
				.andExpect(jsonPath("$[0].status", is("InActive")));
	}

	@Test
	public void getPhoneNumberForCustomerUnauthorized() throws Exception {
		mockMvc.perform(get("/getPhoneNumbers/1")).andDo(print()).andExpect(status().isUnauthorized());
	}

	@WithMockUser("USER")
	@Test
	public void getAllPhoneNumbersSuccess() throws Exception {

		PhoneNumbers phoneNumber2 = new PhoneNumbers(101, 2, 1234567899, "Active");
		phoneNumber.add(phoneNumber2);

		when(phoneNumberDao.findAll()).thenReturn(phoneNumber);

		mockMvc.perform(get("/getAllPhoneNumbers")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[*].customerID").value(Lists.newArrayList(1, 2)))
				.andExpect(jsonPath("$[*].phoneNumber").value(Lists.newArrayList(1234567890, 1234567899)))
				.andExpect(jsonPath("$[*].status").value(Lists.newArrayList("InActive", "Active")));
	}

	@Test
	public void getAllPhoneNumbersUnauthorized() throws Exception {
		mockMvc.perform(get("/getAllPhoneNumbers")).andDo(print()).andExpect(status().isUnauthorized());
	}

	@WithMockUser("ADMIN")
	@Test
	public void activatePhoneNumbersSuccess() throws Exception {

		when(phoneNumberDao.updateStatus("Active", 1, 1234567890)).thenReturn(1);

		mockMvc.perform(patch("/activatePhoneNumbers"))
	      .andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].customerID", is(1))).andExpect(jsonPath("$[0].phoneNumber", is(1234567890)))
				.andExpect(jsonPath("$[0].status", is("Active")));
	}

	@WithMockUser("USER")
	@Test
	public void activatePhoneNumbers_Unauthorized() throws Exception {
		mockMvc.perform(patch("/activatePhoneNumbers")).andDo(print()).andExpect(status().isForbidden());
	}
}
