package ua.com.company.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.company.entity.RememberMeToken;

@Repository
public interface RememberMeRepository extends JpaRepository<RememberMeToken, String>{
	
	@Transactional
	@Modifying
	@Query("delete from RememberMeToken r where r.userName = (:name) ")
	void deleteByName(@Param("name") String name);

}
