package ua.com.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.company.dao.IpersonDAO;
import ua.com.company.entity.Person;
import ua.com.company.serviceInterf.IpersonService;

@Service
@Transactional
public class PersonService implements IpersonService{
	
	@Autowired
	private IpersonDAO pDAO;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public List<Person> getByNamePattern(String pattern) {

		List<Person> persList = pDAO.getByNamePattern(pattern);
		return persList;
	}

	@Override
	public void save(Person person) {
		String codedPassword = encoder.encode(person.getPassword().trim());
		person.setPassword(codedPassword);
		pDAO.save(person);
	}

	@Override
	public Person findOneByEmail(String email) {
		return pDAO.getOneUserByEmail(email);
	}

}
