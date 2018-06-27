package ua.com.company.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import ua.com.company.config.SecurityConfig;

@Configuration
@EnableWebMvc
@ComponentScans({@ComponentScan("ua.com.company.controller"), @ComponentScan("ua.com.company.service"), @ComponentScan("ua.com.company.component")})
@Import({PersistenceConfig.class, SecurityConfig.class})
@PropertySource("classpath:appProperties/AppProp.properties")
public class AppConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
	
private ApplicationContext applicationContext;
	
	private static final String UTF8 = "UTF-8";
	private static final int PORT_NUMBER = 587;
	private static final String MAIL_PROTPCOL = "mail.transport.protocol";
	private static final String SMTP_AUTH = "mail.smtp.auth";
	private static final String SMTP_EMABLE = "mail.smtp.starttls.enable";
	private static final String DEBUG = "mail.debug";
	private static final boolean SMTP_AUTH_VALUE = true;
	private static final boolean SMTP_ENABLE_VALUE = true;
	private static final boolean DEBUGE_VALUE = true;
	
	@Autowired
	private Environment env;
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;		
	}
		
	@Bean
	public ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix(env.getProperty("view.prefix"));
		resolver.setSuffix(env.getProperty("view.suffix"));
		resolver.setTemplateMode(env.getProperty("template.version"));

		return resolver;
	}
	
	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setEnableSpringELCompiler(true);
		templateEngine.setTemplateResolver(templateResolver());

		return templateEngine;
	}
	
	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setCharacterEncoding(UTF8);
		thymeleafViewResolver.setContentType(env.getProperty("content.type"));
		thymeleafViewResolver.setTemplateEngine(templateEngine());

		return thymeleafViewResolver;
	}
		
	@Bean
	public MultipartResolver  resolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(10000000);
		multipartResolver.setMaxUploadSizePerFile(1000000);
		
		return multipartResolver;
	}
	
	

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(env.getProperty("css.file.marker")).addResourceLocations(env.getProperty("css.file.path"));
		registry.addResourceHandler(env.getProperty("file.marker")).addResourceLocations(env.getProperty("file.resources.path"));
	}
	
	@Bean("messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
//		source.setBasename("classpath:locale/messages");
		source.setBasenames(env.getProperty("message.source.default"), env.getProperty("message.source.ukr"),
				env.getProperty("message.source.eng"), env.getProperty("message.source.other"));
		source.setDefaultEncoding(UTF8);
		source.setUseCodeAsDefaultMessage(true);
		
		return source;
	}
	
	@Bean
	   public LocaleResolver localeResolver() {
	      CookieLocaleResolver localeResolver = new CookieLocaleResolver();
	      return localeResolver;
	   }
	
	// перехоплює значення урли з параметром lang  і присвоює локалі задане значення
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	      localeChangeInterceptor.setParamName(env.getProperty("locale.parameter.name"));
	      registry.addInterceptor(localeChangeInterceptor);
	}
	
	@Bean
	public JavaMailSenderImpl mailSenderImpl() {
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setHost(env.getProperty("mail.sender.host"));
		javaMailSenderImpl.setPort(PORT_NUMBER);
		javaMailSenderImpl.setUsername(env.getProperty("email.adress.send.from"));
		javaMailSenderImpl.setPassword(env.getProperty("email.password.send.from"));
		Properties properties = javaMailSenderImpl.getJavaMailProperties();
		properties.put(MAIL_PROTPCOL, env.getProperty("mail.transport.protocol.value"));
		properties.put(SMTP_AUTH, SMTP_AUTH_VALUE);
		properties.put(SMTP_EMABLE, SMTP_ENABLE_VALUE);
		properties.put(DEBUG, DEBUGE_VALUE);
		
		return javaMailSenderImpl;
	}

}
