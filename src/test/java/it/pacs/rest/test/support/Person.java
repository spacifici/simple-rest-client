package it.pacs.rest.test.support;

/**
 * Used to test methods
 * @author: Stefano Pacifici
 */
public class Person {
    private String name;
    private String surname;

    public Person() {
        name = "";
        surname = "";
    }

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
