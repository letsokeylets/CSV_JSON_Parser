package ru.netology;

public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee() {
        // Пустой конструктор
    }

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }

    //Метод необходим для установки значений объекта по ноде
    public void setValue(String name, String value) {
        switch (name) {
            case "id":
                this.id = Long.parseLong(value, 10);
                break;
            case "firstName":
                this.firstName = value;
                break;
            case "lastName":
                this.lastName = value;
                break;
            case "country":
                this.country = value;
                break;
            case "age":
                this.age = Integer.parseInt(value);
                break;
        }
    }
}
