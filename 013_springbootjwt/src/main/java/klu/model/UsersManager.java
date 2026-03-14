package klu.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import klu.repository.UsersRepository;

@Service
public class UsersManager {

	@Autowired
	UsersRepository UR;
	
	@Autowired
	JwtManager JWT;
	
	//SIGNUP OPERATION
	public Map<String, String> signup(Users U)
	{
		Map<String, String> response = new HashMap<String, String>(); 
		try
		{
			UR.save(U); // INSERT
			response.put("code", "200");
			response.put("msg", "Registered Successfully");
		}catch(Exception e)
		{
			response.put("code", "401");
			response.put("msg", e.getMessage());
		}
		return response;
	}
	
	//SIGNIN OPERATION
	public Map<String, String> signin(Users U)
	{
		Map<String, String> response = new HashMap<String, String>(); 
		try
		{
			Users tmp = UR.findByEmailandPassword(U.getEmail(), U.getPassword());
			if(tmp == null)
				throw new Exception("Invalid Credentials!");
			
			String token = JWT.generateJWT(U.getEmail());
			response.put("code", "200");
			response.put("token", token);
		}catch(Exception e)
		{
			response.put("code", "401");
			response.put("msg", e.getMessage());
		}
		return response;
	}
	
	//Fetch User Info
	public Map<String, String> uinfo(String token)
	{
		Map<String, String> response = new HashMap<String, String>();
		try
		{
			String username = JWT.validateJWT(token).get("username");
			Users U = UR.findById(username).get();
			response.put("code", "200");
			response.put("fullname", U.getFname() + " " + U.getLname());
		}
		catch(Exception e)
		{
			response.put("code", "401");
			response.put("msg", e.getMessage());
		}
		return response;
	}
	
	//Generate Random Text
	public String randomText()
	{
		String text = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer captchaText = new StringBuffer();
		int captchaLength = 6;
		Random random = new Random();
		while(captchaText.length() < captchaLength)
		{
			int index = (int)(random.nextFloat() * text.length());
			captchaText.append(text.substring(index, index + 1));
		}
		return captchaText.toString();
	}
	
	//Generate Captcha
	public Map<String, String> generateCaptcha() 
	{
		Map<String, String> response = new HashMap<String, String>();
		try
		{
			String captchaText = randomText();
			int width = 150;
			int height = 40;
			BufferedImage img = new BufferedImage(width, height, BufferedImage.OPAQUE);
			Graphics graphics = img.createGraphics();
			graphics.setFont(new Font("Arial", Font.BOLD, 20));
			graphics.setColor(Color.white);
			graphics.fillRect(0, 0, width, height);
			graphics.setColor(Color.red);
			graphics.drawString(captchaText, 20, 28);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ImageIO.write(img, "png", bout); 
			
			response.put("code", "200");
			response.put("text", captchaText);
			response.put("image", Base64.getEncoder().encodeToString(bout.toByteArray()));
		}catch(Exception e)
		{
			response.put("code", "404");
			response.put("msg", e.getMessage());
		}
		return response;
	}
}
