package com.mvc;

import java.util.List;

public class Clazz {
    private String academy="ABC";
    private String academyDepulicate=academy;
    private List<String> academyInList= List.of("A","B","C");

    public String getAcademy() {
        return academy;
    }

    public String getAcademyDepulicate() {
        return academyDepulicate;
    }

    public List<String> getAcademyInList() {
        return academyInList;
    }


    public int add(int a,int b){
        return a+b;
    }

    public int multiple(int a,int b){
        return a*b;
    }

    public Object checkNull(Object obj){
        if(obj!=null){
            return obj;
        }
        return null;
    }

    public Boolean isGreater(int a,int b){
        return a>b;
    }

    public  String throwException(int a) throws Exception{
        if(a<0){
            throw new ArithmeticException();
        }
        return "Good int";
    }

    public void checkTimeout() throws InterruptedException{
        System.out.println("Thread sleep for 2 secs");
        Thread.sleep(2000);
        System.out.println(" Thread sleep over ....");
    }
}
