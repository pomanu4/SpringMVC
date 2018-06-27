package ua.com.company.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import ua.com.company.dao.IverificationTokenDAO;
import ua.com.company.entity.AccountVerificationToken;
import ua.com.company.entity.Authority;
import ua.com.company.entity.Person;
import ua.com.company.log.Log4JtestClass;
import ua.com.company.serviceInterf.IpersonService;

@Controller
public class AppController {
	
	private Logger loger = LogManager.getLogger(AppController.class);

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private IpersonService persServ;

	@Autowired
	private IverificationTokenDAO tokenDAO;

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String showIndex(Model model) {
		model.addAttribute("mes", "hello kitty");

		addValues(model);

		return "index";
	}

	public void addValues(Model model) {
		String[] arr = new String[] { "drugs", "RnR", "semki", "pivas", "dvijParij", };
		Person person = new Person();
		model.addAttribute("person", person);
		model.addAttribute("arr", arr);
		loger.debug("init array and form object");
	}
	
	@RequestMapping(path= {"ref1"}, method= RequestMethod.GET)
	public String logerTest() {
		loger.debug("ref1 reference was used");
		return "redirect:/";
	}

	@RequestMapping(path = { "save" }, method = RequestMethod.GET)
	public String savePerson(WebRequest request) {
		String contextPath = request.getContextPath();
		System.out.println(contextPath);
		return "redirect:/";
	}

	@RequestMapping(path = { "list" }, method = RequestMethod.POST)
	public String showResult(@ModelAttribute("person") Person person, WebRequest request) {
		person.setName("vasia");
		person.setPassword("vasia111");
		person.addAuthority(Authority.ROLE_USER);
		String appUrl = request.getContextPath();
		System.out.println(person);
//		publisher.publishEvent(new OnPersonRegistrationEvent(person, appUrl));
		persServ.save(person);
		return "redirect:/";
	}

	@RequestMapping(path = { "showText" }, method = RequestMethod.POST)
	public String showText(@RequestParam(name = "mesage") String mes) throws IOException {
		System.out.println(mes);
		String str2 = "../smvc/src/main/webapp/WEB-INF/files/simple.png";
		Log4JtestClass class1 = new Log4JtestClass();
		class1.logTestMethod();

		return "redirect:/";
	}

	@RequestMapping(method = RequestMethod.GET, path = { "qwerty" })
	public ResponseEntity<?> showInfo() {

		return ResponseEntity.ok().body("new responce entity ");
	}

	// load files
	@RequestMapping(path = { "loadFile1" }, method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadFile() throws IOException {
		String path = "../smvc/src/main/webapp/WEB-INF/files/simple.png";
		Path path2 = Paths.get(path);
		byte[] byt = Files.readAllBytes(path2);
		ByteArrayResource bar = new ByteArrayResource(byt);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path2.getFileName().toString())
				.contentType(MediaType.APPLICATION_JSON).contentLength(byt.length).body(bar);
	}

	@RequestMapping(path = { "asdf" }, method = RequestMethod.GET)
	public void downloadFile2(HttpServletResponse response) throws IOException {
		String pp = "../smvc/src/main/webapp/WEB-INF/files/simple.png";
		Path path = Paths.get(pp);
		byte[] byt = Files.readAllBytes(path);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment;filename=" + path.getFileName().toString());

		BufferedOutputStream stream = new BufferedOutputStream(response.getOutputStream());
		stream.write(byt);
		stream.flush();

	}

	@RequestMapping(path = { "upload" }, method = RequestMethod.POST)
	public String uploadFile(@RequestParam(name = "files") MultipartFile[] files) {

		String path = "D:\\aaImgs\\pak";

		for (MultipartFile f : files) {
			if (!f.isEmpty()) {
				File destFile = new File(path + File.separator + f.getOriginalFilename());
				try {

					f.transferTo(destFile);
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
					System.out.println("file upload exeption");
				}
			}
		}
		return "index";
	}

	@RequestMapping(path = { "userpage" }, method = RequestMethod.GET)
	public String showUserPage() {

		return "userPage";
	}

	@RequestMapping(path = { "adminpage" }, method = RequestMethod.GET)
	public String showAdminPage() {

		return "adminPage";
	}

	@RequestMapping(path = { "login" }, method = RequestMethod.GET)
	public String showLoginPage() {

		return "loginPage";
	}

	@RequestMapping(path = { "registrationConfirm" }, method = RequestMethod.GET)
	public String activateAcount(@RequestParam("token") String token) {
		AccountVerificationToken tok = tokenDAO.findByToken(token);
		Person person = tok.getPers();
		person.setEnabled(true);
		tok.setPers(person);
		tokenDAO.save(tok);

		return "redirect:/";
	}
	
	

}
