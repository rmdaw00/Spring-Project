package com.rmdaw.module15.web.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rmdaw.module15.business.facade.BookingFacadeImplementation;
import com.rmdaw.module15.data.model.classes.local.UserLocal;
import com.rmdaw.module15.data.model.interfaces.IUser;

@Controller
@RequestMapping("/users")
public class UserController implements ControllerExtras {
	
	private BookingFacadeImplementation facade;
	private static final String MESSAGE_SUCCESS = "msgSuccess";
	private static final String MESSAGE_ERROR = "msgError";
	private static final String MESSAGE_FORM_ERROR = "Error in the form";

	@Override
	@ModelAttribute("controller")
	public String getControllerName() {
		return ("/users");
	}
	
	@ModelAttribute("placeholder")
	public String getPlaceHolder() {
		return "By Name";
	}

	public UserController(BookingFacadeImplementation facade) {
		super();
		this.facade = facade;
	}
	
	@GetMapping
	public String getUsersByName(@RequestParam(required = false) String search, 
			@RequestParam(required = false, defaultValue = "1") int page, 
			@RequestParam(required = false, defaultValue = "10") Integer pageSize,
			Model model) {

		
		//This is for html paganation section 
		String pageSizeParam = pageSize!=10?"&pageSize="+pageSize:""; 
		model.addAttribute("searchURL" , "/events/search=?" + search + pageSizeParam);
		model.addAttribute("page", page);
		
		model.addAttribute("search",(search!=null)?search:"");
		
		List<IUser> users = facade.getUsersByName(search, pageSize, page);		
		model.addAttribute("users",users);
		
		return "users/home";
	}
	
	
	
	
	@GetMapping("/create")
	public String createUser(Model model) {
		model.addAttribute("action", "/users/create");
		return "users/form";
	}
	
	@PostMapping("/create")
	public String createUser(@Valid @ModelAttribute UserLocal user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute(MESSAGE_ERROR, MESSAGE_FORM_ERROR);
			return "users/form"; 
		}
		model.addAttribute(MESSAGE_SUCCESS, "User Created Successfully");
		facade.createUser(user);
		return getUsersByName(user.getName(),1, 10, model);
	}
	
	
	
	
	
	

	@GetMapping("/edit")
	public String editUser(@RequestParam Long id, Model model) {
		IUser user = null;
		if (id!=null) {
			user = facade.getUserById(id);
		}
		
		if(user==null) {
			model.addAttribute(MESSAGE_ERROR, "User was not Found");
			return createUser(model);
		}
		
		
		model.addAttribute("action", "/users/edit");
		model.addAttribute("user", user);
		
		return "users/form";
	}
	
	
	@PostMapping("/edit")
	public String editUser(@Valid @ModelAttribute UserLocal user, BindingResult 
							bindingResult, Model model) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute(MESSAGE_ERROR, MESSAGE_FORM_ERROR);
			return editUser(user.getId(), model);
		}
		
		facade.updateUser(user);
		model.addAttribute(MESSAGE_SUCCESS, "User was updated Successfully");
		
		return getUsersByName(user.getName(),1, 10, model);
	}
	
	
	@GetMapping("/delete")
	public String deleteUser(@RequestParam Long id, Model model) {
		if (id!=null && facade.deleteUser(id)) {
			model.addAttribute(MESSAGE_SUCCESS, "User was Deleted Successfully");
			return getUsersByName("",1, 10, model);
		} 
		
		model.addAttribute(MESSAGE_ERROR, "User was not Found");		
		return getUsersByName("",1, 10, model);
	}
	
}
