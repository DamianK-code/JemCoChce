package com.example.jemcochce.exeption;

public class InvalidRegisterData extends RuntimeException{
    public InvalidRegisterData(String s){
        super(s);
    }
}
