package ua.com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.company.serviceInterf.IpersonService;

@Service
@Transactional
public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	IpersonService pService;

	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {	 
		return pService.findOneByEmail(arg0);
	}
}
