package com.mvc;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class FizzBuzzTest {

    @Test
    @DisplayName("Divisible by 3")
    @Order(1)
    void divideBy3(){
        assertEquals("Fizz",FizzBuzz.compute(3),"Must be fizz");
    }

    @Test
    @DisplayName("Divisible by 5")
    @Order(2)
    void divideBy5(){
        assertEquals("Buzz",FizzBuzz.compute(5),"Must be buzz");
    }

    @Test
    @DisplayName("Divisible by 3,5")
    @Order(2)
    void divideBy3And5(){
        assertEquals("FizzBuzz",FizzBuzz.compute(15),"Must be fizzbuzz");
    }

    @Test
    @DisplayName("Not Divisible by 3/5")
    @Order(4)
    void divideByNOT3Or5(){
        assertEquals("7",FizzBuzz.compute(7),"Must be num");
    }

    @DisplayName("Multi csv test")
    @ParameterizedTest(name="value={0},expected={1}")
    @CsvFileSource(resources = "/test-data.csv")
    void multiParametrierizedTest(int value,int expected){
        assertEquals(Integer.toString(expected),FizzBuzz.compute(value),"Must be fizz");
    }


}


