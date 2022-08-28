package dev.procheck.capmobile.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MontantResponse {
	 @JsonProperty("MTC")
	 private String MTC;
	 @JsonProperty("MTL")
	 private String MTL;
	 private int code_reponse;
	 private String message;
}
