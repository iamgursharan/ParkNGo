package com.example.park_n_go;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ManageUsersTest {
    @Test
    public void checkGetObjectPass() {
        List data=new ArrayList<User>();
        data.add(new User("User 1","Driver","User1@gmail.com","45287","1224488"));
        data.add(new User("User 2","Host","User2@gmail.com","45256","1554488"));
        String searchEmail="User1@gmail.com";
        String[] expectedOutput={"User 1","Driver","User1@gmail.com","45287","1224488"};
        String[] actualOutput=ManageUsers.getObject(data,searchEmail);
        assertEquals(expectedOutput,actualOutput);
    }

    public void checkGetObjectFail() {
        List data=new ArrayList<User>();
        data.add(new User("User 1","Driver","User1@gmail.com","45287","1224488"));
        data.add(new User("User 2","Host","User2@gmail.com","45256","1554488"));
        String searchEmail="User@gmail.com";
        String[] expectedOutput={"User 1","Driver","User1@gmail.com","45287","1224488"};
        String[] actualOutput=ManageUsers.getObject(data,searchEmail);
        assertEquals(expectedOutput,actualOutput);
    }
}
