package com.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.model.Url;
import com.model.UrlDto;
import com.model.UrlErrorResponseDto;
import com.model.UrlResponseDto;
import com.service.UrlService;
import com.service.UrlServiceImpl;


public class Controller extends HttpServlet {
	private UrlService urlService = new UrlServiceImpl();


    public Controller() {
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String shortLink = null;
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
		    shortLink = pathInfo.substring(1); // Extract the shortLink from the URL
		    // Implement logic to handle the shortLink
		} else {
		    // Handle the case where request.getPathInfo() is null (e.g., invalid URL)
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL");
		}
		if(StringUtils.isEmpty(shortLink))
		{
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setStatus("400");
			urlErrorResponseDto.setError("Invalid Url");
		
			Map<String, UrlErrorResponseDto> errorData = new HashMap<String, UrlErrorResponseDto>();
			errorData.put("urlErrorResponseDto", urlErrorResponseDto);
			ObjectMapper objectMapper = new ObjectMapper(); 
			objectMapper.registerModule(new JavaTimeModule());
			String jsonData = objectMapper.writeValueAsString(errorData);
        
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonData);
		}
		else
		{
			Url urlToRet = urlService.getEncodeUrl(shortLink);
			if(urlToRet==null)
			{
				UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
				urlErrorResponseDto.setStatus("400");
				urlErrorResponseDto.setError("Url does not exist or it might have expired!");
			
				Map<String, UrlErrorResponseDto> errorData = new HashMap<String, UrlErrorResponseDto>();
				errorData.put("urlErrorResponseDto", urlErrorResponseDto);
				ObjectMapper objectMapper = new ObjectMapper(); 
				objectMapper.registerModule(new JavaTimeModule());
				String jsonData = objectMapper.writeValueAsString(errorData);
	        
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonData);
			}
			else if(urlToRet.getExpirationDate().isBefore(LocalDateTime.now()))
			{
				urlService.deleteShortLink(urlToRet);
				UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
				urlErrorResponseDto.setStatus("200");
				urlErrorResponseDto.setError("Url expired, please try generating a new one");
			
				Map<String, UrlErrorResponseDto> errorData = new HashMap<String, UrlErrorResponseDto>();
				errorData.put("urlErrorResponseDto", urlErrorResponseDto);
				ObjectMapper objectMapper = new ObjectMapper(); 
				objectMapper.registerModule(new JavaTimeModule());
				String jsonData = objectMapper.writeValueAsString(errorData);
	        
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonData);
				
			}
			else
			{
				response.sendRedirect(urlToRet.getOriginalUrl());
			}
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UrlDto urlDto = new UrlDto();
		urlDto.setUrl(request.getParameter("url"));
//		urlDto.setExpirationDate(request.getParameter("expirationDate"));
		Url urlToRet = urlService.generateShortLink(urlDto);
		
		
		
        
		if(urlToRet!=null)
		{
			UrlResponseDto urlResponseDto = new UrlResponseDto();
			urlResponseDto.setOriginalUrl(urlToRet.getOriginalUrl());
			urlResponseDto.setShortLink(urlToRet.getShortLink());
			urlResponseDto.setExpirationDate(urlToRet.getExpirationDate());
			
			Map<String, UrlResponseDto> data = new HashMap<String, UrlResponseDto>();
	        data.put("urlResponseDto", urlResponseDto);
			ObjectMapper objectMapper = new ObjectMapper(); 
			objectMapper.registerModule(new JavaTimeModule());
	        String jsonData = objectMapper.writeValueAsString(data);
	        
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonData);
		}
		
		else
		{
		
			UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
			urlErrorResponseDto.setStatus("404");
			urlErrorResponseDto.setError("There was an error processing your request. Please try again");
		
			Map<String, UrlErrorResponseDto> errorData = new HashMap<String, UrlErrorResponseDto>();
			errorData.put("urlErrorResponseDto", urlErrorResponseDto);
			ObjectMapper objectMapper = new ObjectMapper(); 
			objectMapper.registerModule(new JavaTimeModule());
			String jsonData = objectMapper.writeValueAsString(errorData);
        
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonData);
		}
	}

}
