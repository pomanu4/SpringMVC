package ua.com.company.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JtestClass {
	
	private static Logger loger = LogManager.getLogger();

	public void logTestMethod() {
		
		loger.debug("This is a debug message");
		loger.info("This is an info message");
		loger.warn("This is a warn message");
		loger.error("This is an error message");
		loger.fatal("This is a fatal message");
	}
}
