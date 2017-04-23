package com.moralok.arrayList;

import java.util.ArrayList;

import com.moralok.inheritance.Employee;

public class ListTest {

	public static void main(String[] args) {
		ArrayList<Employee> staff = new ArrayList<>();
		
		staff.add(new Employee("Carl Cracher", 80000, 1987, 12, 15));
		staff.add(new Employee("Harry Hacker", 50000, 1989, 10, 1));
		staff.add(new Employee("Tommy Tester", 40000, 1990, 3, 15));
		
		for (Employee employee : staff) {
			employee.raiseSalary(5);
		}
		
		for (Employee employee : staff) {
			System.out.println("name=" + employee.getName() + ",salary=" + employee.getSalary());
		}
	}

}
