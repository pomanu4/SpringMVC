package ua.com.company.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="person_T")
public class Person implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int p_id;
	
	@Column(name="P_name")
	private String name;
	
	@Column(name="P_email")
	private String email;
	
	@Column(name="p_password")
	private String password;
	
	@ElementCollection
	@CollectionTable(name="hobbies", joinColumns=@JoinColumn(name="p_id"))
	@Column(name="p_hob")
	private List<String> hobbies = new ArrayList<String>();
		
	@ElementCollection(targetClass=Authority.class, fetch=FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name="authority", joinColumns=@JoinColumn(name="p_id"))
	@Column(name="p_auth")
	private Set<Authority> authorities = new HashSet<>();
	
	private boolean accountNonExpired = true;
	
	private boolean credentialsNonExpired = true;
	
	private boolean accountNonLocked = true;
	
	private boolean enabled = true;
	

	public Person() {
		
	}

	public Person(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public List<String> getHobbies() {
		return hobbies;
	}

	public void setHobbies(List<String> hobbies) {
		this.hobbies = hobbies;
	}
			
	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public void addAuthority(Authority auth) {
		this.authorities.add(auth);
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getUsername() {
		
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		
		return enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<SimpleGrantedAuthority> authority = new HashSet<>();
		this.authorities.forEach((Authority auth) ->{
			authority.add(new SimpleGrantedAuthority(auth.name()));
		} );
	
		return authority;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", email=" + email + ", password=" + password + ", hobbies=" + hobbies
				+ ", authorities=" + authorities + "]";
	}

}
