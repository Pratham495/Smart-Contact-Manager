package com.example.demo.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserRepository repo;
	
	/*Home Handler*/
	
	@GetMapping("/")
	public String home(Model model) {			
		model.addAttribute("title","Home - smart contact manager");
		return "home.html";
	}
	
	/*about Handler*/
	
	@GetMapping("/about")
	public String about(Model model) {			
		model.addAttribute("title","about - smart contact manager");
		return "about.html";
	}
	
	/*signup Handler*/
	
	@GetMapping("/signup")
	public String signup(Model model) {			
		model.addAttribute("title","Register - smart contact manager");
		model.addAttribute("user",new User());
		return "signup.html";
	}
	
	/*signin Handler*/
	
	@GetMapping("/signin")
	public String signin(Model model) {
		return "login.html";
	}
	
	
	/*Handler For Register User*/
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user")User user, BindingResult result1,@RequestParam(value="agreement",defaultValue="false")boolean agreement, Model model,HttpSession session)
	{ 
		try {
			if(!agreement) {
				System.out.println("you not have agree terms and condition");
				throw new Exception("you not have agree terms and condition");
			}
			
			if(result1.hasErrors()) {
				System.out.println("Error"+result1.toString());
				model.addAttribute("user",user);
				return "signup.html";
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.println("Agreement"+agreement);
			System.out.println("User"+user);
			
			User  result =this.repo.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message",new Message("succefully registerd !!","alert-success"));
			return "signup.html";
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message",new Message("something went wrong !!"+e.getMessage(),"alert-danger"));
			return "signup.html";
		}
		
	}
}
