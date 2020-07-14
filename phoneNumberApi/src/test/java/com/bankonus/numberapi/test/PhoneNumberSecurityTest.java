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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.bankonus.numberapi.dao.PhoneNumberDao;
import com.bankonus.numberapi.model.PhoneNumbers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PhoneNumberSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PhoneNumberDao phoneNumberDao;

	List<PhoneNumbers> phoneNumber;

	@Before
	public void init() {
		phoneNumber = new ArrayList<PhoneNumbers>();
		PhoneNumbers phoneNumber1 = new PhoneNumbers(101, 1, 1234567890, "Active");
		PhoneNumbers phoneNumber2 = new PhoneNumbers(101, 2, 1234567899, "Active");
		phoneNumber.add(phoneNumber1);
		phoneNumber.add(phoneNumber2);
	}

	@WithMockUser("USER")
	@Test
	public void find_login_ok() throws Exception {

		when(phoneNumberDao.findByCustomerID(1)).thenReturn(phoneNumber);

		mockMvc.perform(get("/getPhoneNumbers/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].customerID", is(1))).andExpect(jsonPath("$[0].phoneNumber", is(1234567890)))
				.andExpect(jsonPath("$[0].status", is("Active")));
	}

	@Test
	public void find_nologin_401() throws Exception {
		mockMvc.perform(get("/getPhoneNumbers/1")).andDo(print()).andExpect(status().isUnauthorized());
	}

	@Test
	public void find_nologin_401_all() throws Exception {
		mockMvc.perform(get("/getAllPhoneNumbers")).andDo(print()).andExpect(status().isUnauthorized());
	}

	@WithMockUser("USER")
	@Test
	public void find_nologin_401_update() throws Exception {
		mockMvc.perform(patch("/activatePhoneNumbers")).andDo(print()).andExpect(status().isForbidden());
	}
}
