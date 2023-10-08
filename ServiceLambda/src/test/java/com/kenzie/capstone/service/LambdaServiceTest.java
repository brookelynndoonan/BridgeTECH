package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.dao.UserAccountsDao;
import com.kenzie.capstone.service.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    private ExampleDao exampleDao;

    private UserAccountsDao userAccountsDao;

    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.exampleDao = mock(ExampleDao.class);
        this.userAccountsDao = mock(UserAccountsDao.class);
        this.lambdaService = new LambdaService(userAccountsDao);
    }
 /*
    @Test
    void setDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String data = "somedata";

        // WHEN
        ExampleData response = this.lambdaService.setExampleData(data);

        // THEN
        verify(exampleDao, times(1)).setExampleData(idCaptor.capture(), dataCaptor.capture());

        assertNotNull(idCaptor.getValue(), "An ID is generated");
        assertEquals(data, dataCaptor.getValue(), "The data is saved");

        assertNotNull(response, "A response is returned");
        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    @Test
    void getDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String data = "somedata";
        ExampleRecord record = new ExampleRecord();
        record.setId(id);
        record.setData(data);


        when(exampleDao.getExampleData(id)).thenReturn(Arrays.asList(record));

        // WHEN
        ExampleData response = this.lambdaService.getExampleData(id);

        // THEN
        verify(exampleDao, times(1)).getExampleData(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

  */

    // Write additional tests here

    @Test
    void setUserAccountsTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> accountTypeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String name = "name";
        String accountType = "accountType";
        String password = "password";
        String email = "dragonhatchet@wcingguild.com";

        // WHEN
        UserAccounts response = this.lambdaService.setUserAccounts(name,accountType,password, email);

        // THEN
        verify(userAccountsDao, times(1)).setUserAccounts(idCaptor.capture(),
                nameCaptor.capture(), accountTypeCaptor.capture(),
                passwordCaptor.capture(), emailCaptor.capture());

        assertNotNull(idCaptor.getValue(), "An ID is generated");
        assertEquals(name, nameCaptor.getValue(), "The name is saved");
        assertEquals(accountType, accountTypeCaptor.getValue(), "The accountType is saved");
        assertEquals(password, passwordCaptor.getValue(), "The password is saved");

        assertNotNull(response, "A response is returned");
        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
        assertEquals(name, response.getName(), "The response name should match");
        assertEquals(accountType, response.getAccountType(), "The response accounttype should match");
        assertEquals(password, response.getPassword(), "The response password should match");

    }

    @Test
    void getUserAccountTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String name = "name";
        String accountType = "accountType";
        String password = "password";

        UserAccountRecord record = new UserAccountRecord();
        record.setId(id);
        record.setName(name);
        record.setAccountType(accountType);
        record.setPassword(password);


        when(userAccountsDao.getUserAccounts(id)).thenReturn(Arrays.asList(record));

        // WHEN
        UserAccounts response = this.lambdaService.getUserAccounts(id);

        // THEN
        verify(userAccountsDao, times(1)).getUserAccounts(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(name, response.getName(), "The response name should match");
        assertEquals(accountType, response.getAccountType(), "The response accountType should match");
        assertEquals(password, response.getPassword(), "The response password should match");
    }

}