package ua.com.company.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.company.entity.AccountVerificationToken;

public interface IverificationTokenDAO extends JpaRepository<AccountVerificationToken, Integer>{
	
	@Query(" select a from AccountVerificationToken a where a.token=(:token)")
	public AccountVerificationToken findByToken(@Param("token") String token);
}
