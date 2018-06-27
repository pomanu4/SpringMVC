package ua.com.company.event;

import org.springframework.context.ApplicationEvent;

import ua.com.company.entity.Person;

public class OnPersonRegistrationEvent extends ApplicationEvent {
	
private static final long serialVersionUID = 1L;
	
	private String appUrl;
	
	private Person person;

	public OnPersonRegistrationEvent(Person person, String appUrl) {
		super(person);
		this.person = person;
		this.appUrl = appUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "OnPersonRegistrationEvent [appUrl=" + appUrl + ", person=" + person + "]";
	}

}
