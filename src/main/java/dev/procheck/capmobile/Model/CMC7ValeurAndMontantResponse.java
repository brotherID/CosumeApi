package dev.procheck.capmobile.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMC7ValeurAndMontantResponse {
	private CMC7ValeurResponse cMC7ValeurResponse;
	private MontantResponse montantResponse;
}
