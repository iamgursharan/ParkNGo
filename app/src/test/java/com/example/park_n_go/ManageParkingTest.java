package com.example.park_n_go;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ManageParkingTest {

    @Test
    public void checkGetParkingName() {
        List data=new ArrayList<Parking>();
        data.add(new Parking("12 church street","public","host@gmail.com","12121","43.1545","-79.6622","church street","","45","40","15","30"));
        String searchName="Church street";
        List expectedOutput=new ArrayList();
        expectedOutput.add("Church street");
        List actualOutput=ManageParkings.getNames(data);
        assertEquals(expectedOutput,actualOutput);
    }
}
