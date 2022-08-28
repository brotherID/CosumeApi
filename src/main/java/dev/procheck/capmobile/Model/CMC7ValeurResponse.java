package dev.procheck.capmobile.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMC7ValeurResponse {
	
	private String data;
	private int code_reponse;
	private String message;

}
