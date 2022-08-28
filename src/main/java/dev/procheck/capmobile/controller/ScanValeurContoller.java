package dev.procheck.capmobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import dev.procheck.capmobile.Model.AppUser;
import dev.procheck.capmobile.Model.CMC7ValeurAndMontantResponse;
import dev.procheck.capmobile.Model.Image;
import dev.procheck.capmobile.exception.FunctinalException;
import dev.procheck.capmobile.service.ScanValeurServiceImpl;

@RestController
public class ScanValeurContoller {
	
	@Autowired
	private ScanValeurServiceImpl scanValeurServiceImpl;
	
	@PostMapping("/scan/valeur")
	public CMC7ValeurAndMontantResponse scanValeur(@RequestBody Image image ) throws FunctinalException {
		return scanValeurServiceImpl.scanValeur(image);
	}
	
	
	@PostMapping("/test")
	public AppUser autenticate(@RequestBody AppUser user)
	{
		String uri = "http://172.24.100.6:8090/PKRecuirement/recuirement/autenticate";
		RestTemplate  restTemplate = new RestTemplate();
		AppUser appUser  = restTemplate.postForObject(uri, user, AppUser.class);
		return  appUser;
	}
	

}
