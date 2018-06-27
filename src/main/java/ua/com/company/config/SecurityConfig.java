package ua.com.company.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@PropertySource({"classpath:appProperties/AppProp.properties", "classpath:security/security.properties"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String USER_ROLE = "hasRole('ROLE_USER')";
	private static final String ADMIN_ROLE = "hasRole('ROLE_ADMIN')";
	private static final String COOKI = "JSESSIONID";
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService uds;
	
	@Autowired
	private PersistentTokenRepository tokenRep;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(uds);
	}

	@Autowired
	public void globalConfigure(AuthenticationManagerBuilder managerBuilder, AuthenticationProvider provider) {
		try {
			configurer().withUser(env.getProperty("inmemory.admin.name"))
			.password(env.getProperty("inmemory.admin.password"))
			.roles(env.getProperty("inmemory.admin.role"))
			.and()
			.configure(managerBuilder);
			managerBuilder.authenticationProvider(provider);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(uds);
		provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.formLogin()
		.loginPage(env.getProperty("login.page.url"))
		.failureUrl(env.getProperty("failure.url"))
		.passwordParameter(env.getProperty("user.auth.password.parameter"))
		.usernameParameter(env.getProperty("user.auth.name.parameter"))
		.defaultSuccessUrl(env.getProperty("default.success.url"))
		.and()
		.logout()
		.logoutUrl(env.getProperty("logout.url"))
		.logoutSuccessUrl(env.getProperty("login.page.url"))
		.deleteCookies(COOKI)
		.and()
		.authorizeRequests()
		.antMatchers("/", "ref1", "/login")
		.permitAll()
		.antMatchers("/user**").access(ADMIN_ROLE + " or " + USER_ROLE)
		.antMatchers("/admin**").access(ADMIN_ROLE);
		http.csrf().disable();
		http.rememberMe().rememberMeParameter(env.getProperty("remember.me.perameter.name"))
		.tokenRepository(tokenRep)
		.userDetailsService(uds);
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(env.getProperty("css.file.marker"));
		web.ignoring().antMatchers(env.getProperty("resources.marker"));
	}

	@Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        PersistentTokenBasedRememberMeServices tokenBasedservice = new PersistentTokenBasedRememberMeServices(
        		env.getProperty("remember.me.perameter.name"), uds, tokenRep);
        return tokenBasedservice;
    }


	private InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer() {

		return new InMemoryUserDetailsManagerConfigurer<>();
	}


}
