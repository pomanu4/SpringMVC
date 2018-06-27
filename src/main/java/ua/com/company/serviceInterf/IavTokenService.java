package ua.com.company.serviceInterf;

import ua.com.company.entity.Person;

public interface IavTokenService {
	
	public void saveToken(String token, Person person);
}
