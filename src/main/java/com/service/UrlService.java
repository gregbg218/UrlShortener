package com.service;

import com.model.Url;
import com.model.UrlDto;

public interface UrlService {
	public Url generateShortLink(UrlDto urlDto);
	public Url persistShortLink(Url url);
	public Url getEncodeUrl(String url);
	public void deleteShortLink(Url url);

}
