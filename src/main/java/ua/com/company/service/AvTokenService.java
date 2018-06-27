package ua.com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.company.dao.IverificationTokenDAO;
import ua.com.company.entity.AccountVerificationToken;
import ua.com.company.entity.Person;
import ua.com.company.serviceInterf.IavTokenService;

@Service
@Transactional
public class AvTokenService implements IavTokenService{
	
	@Autowired
	private IverificationTokenDAO vtDAO;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void saveToken(String token, Person person) {
		AccountVerificationToken tok = new AccountVerificationToken();
		tok.setToken(token);
		String encodedPassword = encoder.encode(person.getPassword().trim());
		person.setPassword(encodedPassword);
		tok.setPers(person);
		vtDAO.save(tok);

	}
}
