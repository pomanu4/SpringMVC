package ua.com.company.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RememberMeToken {
	
	@Id
	private String seriesId;

	@Basic
	private String userName;

	@Basic
	private String token;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	public RememberMeToken() {

	}

	public RememberMeToken(String seriesId, String userName, String token, Date date) {
		this.seriesId = seriesId;
		this.userName = userName;
		this.token = token;
		this.date = date;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "RememberMeToken [seriesId=" + seriesId + ", userName=" + userName + ", token=" + token + ", date="
				+ date + "]";
	}

}
