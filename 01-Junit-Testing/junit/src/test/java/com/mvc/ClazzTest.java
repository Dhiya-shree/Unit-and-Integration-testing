package com.mvc;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClazzTest {

    Clazz clazz;


    @Test
    @DisplayName("Multiply")
    void multiply(){
      assertEquals(12,clazz.multiple(3,4),"Must be 12");
    }

    @Test
    @DisplayName("Equal or not order 1")
    @Order(1)
    void testequal(){
        assertEquals(6,clazz.add(2,4),"Must be 6");
        assertNotEquals(5,clazz.add(2,4),"Must be 6");
    }

    @Test
    @Order(0)
    @DisplayName("Null check order 0")
    void testNull(){
        String str = null;
        assertNull(clazz.checkNull(str),"Obj null");
        str="a";
        assertNotNull(clazz.checkNull(str),"Obj null");
    }

    @Test
    @DisplayName("Same or not order - 1")
    @Order(-1)
    void testSame(){
        String str="a";
        assertSame(clazz.getAcademy(),clazz.getAcademyDepulicate(),"Same");
        assertNotSame(str,clazz.getAcademy()," No same");
    }

    @Test
    @DisplayName("True / False ")
    @Order(5)
    void trueorFalse(){
        assertTrue(clazz.isGreater(5,4)," Great");
        assertFalse(clazz.isGreater(4,5),"No great");
    }

    @Test
    @DisplayName(" Array equal")
    void testArray(){
        String[] s={"A","B","C"};
        String[] s2={new String("A"),"B","C"};
        assertArrayEquals(s,s2,"Deep same");
    }

    @Test
    @DisplayName(" Iterable equal")
    void testIterable(){
        HashSet<String> hs=new HashSet<>();
        hs.add("C");hs.add("A");hs.add("B");
        assertIterableEquals(clazz.getAcademyInList(),hs);
    }

    @Test
    @DisplayName("Lines match")
    void testLineMatch(){
        List<String> ls=List.of("A","B","C");
        assertLinesMatch(ls,clazz.getAcademyInList(),"Lines match");
    }

    @Test
    @DisplayName("Throw error")
    void throwTest(){
        assertThrows(Exception.class,()->clazz.throwException(-1),"Throw arith exc");
        assertDoesNotThrow(()->clazz.throwException(1),"No throw ");
    }

    @Test
    @DisplayName("Timeout")
    void testTimeout(){
        assertTimeoutPreemptively(Duration.ofSeconds(3),
                ()->clazz.checkTimeout()
        ,
                "Method should execute in 3 secs ");
    }

    @BeforeEach
    void set_up_before_each(){
        System.out.println(" Before each .. create clazz");
        clazz=new Clazz();
    }

    @AfterEach
    void set_up_after_each(){
        System.out.println(" After each class ");
        clazz=null;
    }

    @AfterAll
    static void set_up_after_all(){
        System.out.println("After all ");
    }

    @BeforeAll
    static void set_up_before_all(){
        System.out.println("Before all ");
    }



}
