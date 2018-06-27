package ua.com.company.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "toc")
public class AccountVerificationToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int avt_id;

	@Column(name = "tok")
	private String token;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "p_id", nullable = false)
	private Person pers;

	public AccountVerificationToken() {
	}

	public int getAvt_id() {
		return avt_id;
	}

	public void setAvt_id(int avt_id) {
		this.avt_id = avt_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Person getPers() {
		return pers;
	}

	public void setPers(Person pers) {
		this.pers = pers;
	}

	@Override
	public String toString() {
		return "AcountVerificationToken [avt_id=" + avt_id + ", token=" + token + ", pers=" + pers + "]";
	}

}
