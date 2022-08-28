package dev.procheck.capmobile.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.procheck.capmobile.Model.CMC7ValeurAndMontantResponse;
import dev.procheck.capmobile.Model.CMC7ValeurResponse;
import dev.procheck.capmobile.Model.Image;
import dev.procheck.capmobile.Model.MontantResponse;
import dev.procheck.capmobile.controller.PKICRAIMontant;
import dev.procheck.capmobile.exception.FunctinalException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScanValeurServiceImpl implements ScanValeurService {
	
	@Value("#{'${type.grains.accepted:CHQ,LCN}'.split(',')}")
	private List<String> typeGrainsAccepted;
	@Value("${uri.CMC7.valeur:http://10.1.11.57:8000/CMC7Valeur}")
	private String uriCMC7Valeur;
	@Value("${uri.montant:http://10.1.11.57:8014/getMontant}")
	private String uriMontant;
	

	@Override
	public CMC7ValeurAndMontantResponse scanValeur(Image image) throws FunctinalException {
		log.info("*******Begin scanValeur {}********",image);
		CMC7ValeurAndMontantResponse response = new CMC7ValeurAndMontantResponse();
		if(typeGrainsAccepted.contains(image.getTypeGrain()))
		{
			RestTemplate  restTemplateCMC7Valeur = new RestTemplate();
			CMC7ValeurResponse cMC7ValeurResponse =  restTemplateCMC7Valeur.postForObject(uriCMC7Valeur, 
					image, CMC7ValeurResponse.class);
			response.setCMC7ValeurResponse(cMC7ValeurResponse);
			
			if(cMC7ValeurResponse.getCode_reponse()==1)
			{
				
				RestTemplate  restTemplateMontant = new RestTemplate();
				MontantResponse montantResponse = restTemplateMontant.postForObject(uriMontant,
						image,MontantResponse.class);
				
				if(montantResponse.getCode_reponse()==1)
				{
					response.setMontantResponse(montantResponse);
				}
				else
				{
					throw new FunctinalException(String.format("API [ %s ] not valide",uriMontant));
				}
				
			}
			else
			{
				throw new FunctinalException(String.format("API [ %s ] not valide",uriMontant));
			}
		}
		else
		{
			throw new FunctinalException(String.format("API [ %s ] not valide",uriCMC7Valeur));
		}
		
//		PKICRAIMontant pkICR = new PKICRAIMontant();
//		pkICR.doWork("http://10.1.11.57:8014/getMontant", "");
//		String mtc =pkICR.getDataICR().mntC;
		log.info("*******End scanValeur {}********",response);
		return response;
	}

	
}
