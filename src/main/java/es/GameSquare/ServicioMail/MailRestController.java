package es.GameSquare.ServicioMail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class MailRestController {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	
	@RequestMapping("/sendEmailMod")
	public boolean modMail(@RequestBody String email) {
		executor.execute(new SendModMail(email, javaMailSender));
		return true;
	}
	
	@RequestMapping("/sendEmailGame")
	public boolean gameMail(@RequestBody String emailInfo) {
		executor.execute(new SendGameMail(emailInfo, javaMailSender));
		return true;
	}
	
}

class SendModMail implements Runnable {
	
	private String emailInfo;
	private JavaMailSender javaMailSender;
	
	public SendModMail(String emailInfo, JavaMailSender javaMailSender) {
		this.emailInfo = emailInfo;
		this.javaMailSender = javaMailSender;
	}
	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleMailMessage mail = new SimpleMailMessage();
		Map<String, String> map;
		try {
			map = mapper.readValue(emailInfo, Map.class);
			mail.setTo(map.get("email"));
			mail.setSubject("Mod published");
			mail.setText(map.get("devName") + " has created a mod for yor game " + map.get("softwareName"));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		javaMailSender.send(mail);
	}
}

class SendGameMail implements Runnable {
	
	private String emailInfo;
	private JavaMailSender javaMailSender;
	
	public SendGameMail(String emailInfo, JavaMailSender javaMailSender) {
		this.emailInfo = emailInfo;
		this.javaMailSender = javaMailSender;
	}
	
	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map;
		SimpleMailMessage mail = new SimpleMailMessage();
		try {
			map = mapper.readValue(emailInfo, Map.class);
			mail.setTo(map.get("email"));
			mail.setSubject("Game published");
			mail.setText(map.get("devName") + " has created a new game " + map.get("softwareName"));
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		javaMailSender.send(mail);
	}
		
}


	
	
