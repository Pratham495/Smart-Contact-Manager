package com.example.demo.Controller;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Repositories.UserRepository;
import com.example.demo.Repositories.contactRepository;
import com.example.demo.entities.Contact;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;





@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder encode;
	
	@Autowired
	private UserRepository repo;
	
	
	@Autowired
	private contactRepository repo1;
	
	//method for adding common data to response
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		
		String username=principal.getName();
		System.out.println("USERNAME"+username);
		
		//get the username by email	
		User user = this.repo.getUserByusername(username);
		System.out.println("USER "+user);
		model.addAttribute("user",user);
	}
	
	//dashboard home
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal)
	//principal annotation comes from java.security
	{
		
		return "normal/user_dashboard.html";
	}
	
	//open add form handler
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","Add contact");
		model.addAttribute("contact",new Contact());
		
		return "normal/add_contact_form.html";
	}
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file, 
			Principal principal,HttpSession session) {
		
		
		try {
		String name=principal.getName();
		User user= this.repo.getUserByusername(name);
		
		//processing and uploading file
		
		if(file.isEmpty())
		{
			//if the file is empty try our message
			System.out.println("File is Empty");
			contact.setImage("contact.png");
		}
		else {
			//file the file to folder and update the name to contact
			contact.setImage(file.getOriginalFilename());
			
			File file1=new ClassPathResource("static/img").getFile();
		Path path=Paths.get(file1.getAbsolutePath()+File.separator+file.getOriginalFilename());	
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image is Uploaded");
		}
		
		contact.setUser(user);
		user.getContacts().add(contact);
		
		//Do Work to store value in Database
		this.repo.save(user);
		
		System.out.println("DATA"+contact);
		System.out.println("Added to database");
		
		//message to success....
		session.setAttribute("message",new Message("Your Contact is added Add More...","success"));
		
		}catch(Exception e) {
			System.out.println("Error"+e.getMessage());
			e.printStackTrace();
			//message to error
			session.setAttribute("message",new Message("Something Went Wrong...","danger"));
		}
		return "normal/add_contact_form.html";
	}
	//show contact handler
	
	@GetMapping("/show-contact")
	public String showContact( Model model, Principal principal)
	{
		model.addAttribute("title","Show user Contacts");
		//Contact list bhejni h
		
		String username = principal.getName();
		User user= this.repo.getUserByusername(username);
		
		
		List<Contact>contacts=this.repo1.findContactsByUser(user.getId());
		
		//send all contacts
		model.addAttribute("contacts",contacts);
		return "normal/showcontact.html";
	}
	//Showing Contact Details
	
	@GetMapping("/{cid}/contact")
	public String contactdetails(@PathVariable("cid")Integer cid, Model model,Principal principal) {
		System.out.println("CID "+cid);
		
		Optional<Contact>contactOptional=this.repo1.findById(cid);
		Contact contact=contactOptional.get();
		//
		String username=principal.getName();
		User user=this.repo.getUserByusername(username);
		
		if(user.getId()==contact.getUser().getId())
			model.addAttribute("contact",contact);
		return "normal/contact_details.html";
	}
	
	//delete contact handler
	@GetMapping("/delete/{cid}")
	public String delete(@PathVariable("cid")Integer cid,Model model,HttpSession session,Principal principal)
	{
		
		Contact contact=this.repo1.findById(cid).get();
		
		User user=this.repo.getUserByusername(principal.getName());
		user.getContacts().remove(contact);
		this.repo.save(user);
		
		session.setAttribute("message",new Message("Contact Deleted Successfully","success"));
		
		
		return "redirect:/user/show-contact/";
	}
	
	// update contact handler
	@PostMapping("/update-contact/{cid}")
	public String updateform(@PathVariable("cid")Integer cid,Model model)
	{
		model.addAttribute("title","update contact");
		Contact contact=this.repo1.findById(cid).get();
		model.addAttribute("contact",contact);
		return "normal/update-form.html";
	}
	//setting to your profile
	@GetMapping("/profile")
	public String yourprofile(Model model) {
		
		return "normal/profile.html";
	}
	
	//Open settings handler
	@GetMapping("/settings")
	public String openSettings()
	{
		return "normal/settings.html";
	}
	
	//change password handler
	@PostMapping("/change-password")
	public String changepassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,Principal principal,HttpSession session) {
		
		System.out.println("OLDPASSWORD "+oldPassword);
		System.out.println("NEWPASSWORD "+newPassword);
		
		String username=principal.getName();
		User currentUser=this.repo.getUserByusername(username);
		System.out.println(currentUser.getPassword());
		
		if(this.encode.matches(oldPassword,currentUser.getPassword())) {
			//change password
			currentUser.setPassword(this.encode.encode(newPassword));
			this.repo.save(currentUser);
			session.setAttribute("message", new Message("password succesfully changed", "success"));
		}
		else {
			session.setAttribute("message", new Message("Wrong Password Please Check your Password Again...", "danger"));
			return "redirect:/user/settings";
		}
		return "redirect:/user/index";
	}
	
	
}
 	