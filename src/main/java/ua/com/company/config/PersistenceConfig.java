package ua.com.company.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:dataBase/dataBase.properties")
@EnableJpaRepositories(basePackages= {"ua.com.company.dao"})
public class PersistenceConfig {
	
	private static final String DIALECT = "hibernate.dialect";
	private static final String HBM2_DDL = "hibernate.hbm2ddl.auto";
	private static final String SHOW_SQL = "hibernate.show_sql";

	@Autowired
	private Environment env;
	
	@Bean()
	public DataSource getDataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(env.getProperty("db.driver"));
		config.setJdbcUrl(env.getProperty("db.url"));
		config.setUsername(env.getProperty("db.username"));
		config.setPassword(env.getProperty("db.password")); 
		
		return new HikariDataSource(config);
	}
	
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean geEntityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(getDataSource());
		factoryBean.setJpaVendorAdapter(getVendorAdapter());
		factoryBean.setPackagesToScan(env.getProperty("entity.pack"));
		
		Properties jpaProperties = new Properties();
		jpaProperties.put(DIALECT, env.getProperty("db.dialect"));
		jpaProperties.put(HBM2_DDL, env.getProperty("db.hbm.value"));
		jpaProperties.put(SHOW_SQL, env.getProperty("db.showSQL.value") );
		
		factoryBean.setJpaProperties(jpaProperties);
		
		return factoryBean;
	}
	
	@Bean
	public HibernateJpaVendorAdapter getVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		
		return hibernateJpaVendorAdapter;
	}
	
	@Bean(name="transactionManager")
	public JpaTransactionManager getJpaTransactionManager(EntityManagerFactory factory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(factory);
		
		return transactionManager;
	}
}
