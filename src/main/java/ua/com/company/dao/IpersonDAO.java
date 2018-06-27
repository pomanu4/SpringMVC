package ua.com.company.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.company.entity.Person;

public interface IpersonDAO extends JpaRepository<Person, Integer>{
	
	@Query("select p from Person p where p.name like (:patt)")
	public List<Person> getByNamePattern(@Param("patt") String pattern);
	
	@Query("select p from Person p where p.email = (:email)")
	public Person getOneUserByEmail(@Param("email") String email);

}
