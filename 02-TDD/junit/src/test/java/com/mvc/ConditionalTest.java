package com.mvc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class ConditionalTest {


    @Test
    @Disabled(" Task yet to complete")
    void todoTask(){
        //TODO: execution not complete
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testForLinux(){
        System.out.println(" Linux test only ");
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindows(){
        System.out.println(" Linux test only ");
    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testForMac(){
        System.out.println(" Linux test only ");
    }

    @Test
    @EnabledOnOs({OS.WINDOWS,OS.LINUX})
    void testForLinuxWindow(){
        System.out.println(" Linux and Window test only ");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENV_TEST",matches = "true")
    void testForEnv(){
        System.out.println(" true Test env only ");
    }

    @Test
    @EnabledIfSystemProperty(named = "SYS_PROP_TEST",matches = "true")
    void testForSysProp(){
        System.out.println(" true sys prop only ");
    }


}
