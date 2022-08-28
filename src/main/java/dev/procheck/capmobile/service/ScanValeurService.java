package dev.procheck.capmobile.service;

import dev.procheck.capmobile.Model.CMC7ValeurAndMontantResponse;
import dev.procheck.capmobile.Model.Image;
import dev.procheck.capmobile.exception.FunctinalException;

public interface ScanValeurService {
	public CMC7ValeurAndMontantResponse scanValeur(Image image )throws FunctinalException;
}
