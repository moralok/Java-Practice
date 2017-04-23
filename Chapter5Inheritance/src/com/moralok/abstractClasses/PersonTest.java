package com.moralok.abstractClasses;

public class PersonTest {

	public static void main(String[] args) {
		Person[] persons = new Person[2];
		
		persons[0] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
		persons[1] = new Student("Moria Morris", "computer science");
		
		for (Person person : persons) {
			System.out.println(person.getName() + ", " + person.getDescription());
		}
	}

}
