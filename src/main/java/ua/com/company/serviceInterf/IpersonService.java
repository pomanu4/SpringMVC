package ua.com.company.serviceInterf;

import java.util.List;

import ua.com.company.entity.Person;

public interface IpersonService {
	
	List<Person> getByNamePattern(String pattern);

	void save(Person person);

	Person findOneByEmail(String email);

}
