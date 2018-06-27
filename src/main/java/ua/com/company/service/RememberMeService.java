package ua.com.company.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.company.dao.RememberMeRepository;
import ua.com.company.entity.RememberMeToken;

@Service("persistentTokenRepository")
@Transactional
public class RememberMeService implements PersistentTokenRepository{
	
	@Autowired
	private RememberMeRepository repository;
	
	
	@Override
	public void createNewToken(PersistentRememberMeToken rmToken) {
		RememberMeToken token = new RememberMeToken();
		token.setSeriesId(rmToken.getSeries());
		token.setUserName(rmToken.getUsername());
		token.setToken(rmToken.getTokenValue());
		token.setDate(rmToken.getDate());
		repository.save(token);		
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		RememberMeToken token = repository.findOne(seriesId);
		if (token != null) {
			PersistentRememberMeToken rmToken = new PersistentRememberMeToken(token.getUserName(), token.getSeriesId(),
					token.getToken(), token.getDate());
			return rmToken;
		}
		return null;
	}

	@Override
	public void removeUserTokens(String userName) {
		repository.deleteByName(userName);
	}

	@Override
	public void updateToken(String seriesId, String token, Date data) {
		RememberMeToken newToken = repository.findOne(seriesId);
		newToken.setDate(data);
		newToken.setToken(token);
	}

}
