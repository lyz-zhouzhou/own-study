package org.example.other.basics;

public class Dog extends Animal{
    @Override
    public void eat() {
        System.out.println("狗舔着吃");
    }

    @Override
    public void sleep() {
        System.out.println("狗卧着睡");
    }
}
