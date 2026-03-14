package klu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import klu.model.Users;
import klu.model.UsersManager;

@RestController
@RequestMapping("users")

public class UsersController {
	
	@Autowired
	UsersManager UM;
	
	@PostMapping("signup")
	public Map<String, String> signup(@RequestBody Users U)
	{
		return UM.signup(U);
	}
	
	@PostMapping("signin")
	public Map<String, String> signin(@RequestBody Users U)
	{
		return UM.signin(U);
	}
	
	@GetMapping("uinfo")
	public Map<String, String> uinfo(@RequestHeader(name = "jwttoken") String token)
	{
		return UM.uinfo(token);
	}
	
	@GetMapping("captcha")
	public Map<String, String> captcha()
	{
		return UM.generateCaptcha();
	}
}
