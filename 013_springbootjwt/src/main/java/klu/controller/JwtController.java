package klu.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import klu.model.JwtManager;

@RestController
@RequestMapping("jwt")
public class JwtController {
	
	@Autowired
	JwtManager JM;
	
	@PostMapping("generate")
	public String generate(@RequestBody Map<String, String> data)
	{
		try
		{
			return JM.generateJWT(data.get("username"));
		}catch(Exception e)
		{
			return e.getMessage();
		}
	}
	
	@PostMapping("/validate")
	public Map<String, String> validate(@RequestBody Map<String, String> data)
	{
		Map<String, String> response = new HashMap<String, String>();
		try
		{
			response = JM.validateJWT(data.get("token"));
		}catch(Exception e)
		{
			response.put("code", "401");
			response.put("msg", e.getMessage());
		}
		return response;
	}
}
