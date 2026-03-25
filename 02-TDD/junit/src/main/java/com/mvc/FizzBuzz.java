package com.mvc;

public class FizzBuzz {

    // If number divisible by 3 == fizz
    // if number divisible by 5 == buzz
    // if number divisible by 3,5 == fizzbuzz
    // if not , pringt number

    public static String compute(int i){
            StringBuilder str=new StringBuilder();
            if(i%3==0){
                str.append("Fizz");
            }
            if(i%5==0){
                str.append("Buzz");
            }
            if(str.isEmpty()){
                str.append(i);
            }
            return str.toString();
    }
}

