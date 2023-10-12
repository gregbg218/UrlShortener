package com.service;



import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import com.dao.UrlDao;
import com.google.common.hash.Hashing;
import com.model.Url;
import com.model.UrlDto;


public class UrlServiceImpl implements UrlService {
	
	private UrlDao urlDao = new UrlDao();

	public Url generateShortLink(UrlDto urlDto) {
		if(StringUtils.isNotEmpty(urlDto.getUrl()))
		{
			String encodeUrl= encodeUrl(urlDto.getUrl());
			Url urlToPersist = new Url();
			Long latestId = urlDao.getLatestId();
			Long newId = latestId + 1;
			urlToPersist.setId(newId);
			urlToPersist.setCreationDate(LocalDateTime.now());
			urlToPersist.setOriginalUrl(urlDto.getUrl());
			urlToPersist.setShortLink(encodeUrl);
			urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(),urlToPersist.getCreationDate()));
			Url urlToRet= persistShortLink(urlToPersist);
			
			
			if(urlToRet!=null)
				return  urlToRet;
			return null;
		}
		return null;
	}

	private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
		if(StringUtils.isBlank(expirationDate))
		{
			return creationDate.plusSeconds(60);
		}
		LocalDateTime expirationDateToRet = LocalDateTime.parse(expirationDate);
		return expirationDateToRet;
	}

	private String encodeUrl(String url) {
		String encodeUrl="";
		LocalDateTime time = LocalDateTime.now();
		encodeUrl= Hashing.murmur3_32().hashString(url.concat(time.toString()), StandardCharsets.UTF_8).toString();
		return encodeUrl;
	}

	public Url persistShortLink(Url url) {
		Url urlToRet= urlDao.save(url);
		return urlToRet;
	}

	public Url getEncodeUrl(String url) {
		Url urlToRet= urlDao.findByShortLink(url);
		return urlToRet;
	}

	public void deleteShortLink(Url url) {
		
		urlDao.delete(url);
	}

}
