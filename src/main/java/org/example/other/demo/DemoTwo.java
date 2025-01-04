package org.example.other.demo;

public class DemoTwo {
    public static void main(String[] args) {
        print(Person::new);
    }

    public static void print(PersonBuilder personBuilder) {
        Person build = personBuilder.build();
        System.out.println(build);
    }
}
