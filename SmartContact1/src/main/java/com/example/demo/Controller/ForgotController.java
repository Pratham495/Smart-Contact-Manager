package com.example.demo.Controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.entities.User;
import com.example.demo.service.EmailService;

@Controller
public class ForgotController {
	
	@Autowired
	private EmailService service;
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encode;

	//Forgot Password Handler
	
		@GetMapping("/forgot")
		public String openEmailform() {
			
			return "forgot_email_form.html";
		}
		
		@PostMapping("/send-otp")
		public String sendOTP(@RequestParam("email") String email,HttpSession session) {
			
			System.out.println("EMAIL "+email);
			
			//generate otp 4 digit
			
			Random random = new Random(1000);
			 int otp=random.nextInt(9999999);
			 System.out.println("OTP "+otp);
			 
			 //write code for verify your email
			 String subject="OTP FOR SCM";
			 String message=""
					 +"<div style='border:1px solid #e2e2e2; padding:20px'>"
					 +"<h1>"
					 +"OTP is "
					 +"<b> "+otp
					 +"</n>"
					 +"</h1>"
					 +"</div>";
			 String to=email;
			 
			boolean flag= this.service.sendEmail(subject, message, to);
			if(flag)
			{
				session.setAttribute("myotp",otp);
				session.setAttribute("email",email);
				return "verify_otp.html";
			}else
			{
				session.setAttribute("message","Check your email id");
				return "forgot_email_form.html";
			}
			
		}
		
		//verify otp
		@PostMapping("/verify-otp")
		public String verifyotp(@RequestParam("otp") int otp, HttpSession session)
		{
			int myotp=(int)session.getAttribute("myotp");
			String email=(String)session.getAttribute("email");
			if(myotp==otp)
			{
				//change password form
			User user=this.repo.getUserByusername(email);
			
			if(user==null)
			{
				//send error message
				session.setAttribute("message","user does not exists with this email!!");
				return "forgot_email_form.html";
			}else {
				
			}
				
				return "change_password_form.html";
			}
			else {
				session.setAttribute("message","you have entered wrong OTP");
				return "verify_otp.html";
			}
			
		}
		
		//change password
		@PostMapping("/change-password")
		public String changePassword(@RequestParam("newpassword")String newpassword,HttpSession session) {
			String email=(String)session.getAttribute("email");
		User user=	this.repo.getUserByusername(email);
			user.setPassword(this.encode.encode(newpassword));
			this.repo.save(user);
			return "redirect:/signin?change=password changed successfully..";
			
			
		}
}
