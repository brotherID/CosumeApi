package dev.procheck.capmobile.controller;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Iterator;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.json.JSONObject;

//http://10.1.1.56:8014/getMontant
/**
 * objet pour recuperer le montant de la valeur du serveur AI Montant
 * 
 * @author N.BENZAIRA
 *
 */
public class PKICRAIMontant {

	/**
	 * Objet qui contient le resultat de l'ICR
	 */
	DataICR dataICR;

	/**
	 * erreur retourn�e en cas d'erreur ICR
	 */
	String errorCode;

	/**
	 * Constructeur de la classe
	 */
	public PKICRAIMontant() {
		this.dataICR = new DataICR();
		errorCode = "";
	}

	/**
	 * recupere le montant du serveur ICR AI
	 * 
	 * @param ICR_URL l'url du serveur AI montant
	 * @param img     l'image recto du cheque
	 * @return true si ok false si exception
	 */

	public boolean doWork(String ICR_URL, String imgBase64) {
		System.out.println("Start PKICRAIMontant.dowork");
		boolean isOk = true;
		HttpURLConnection con = null;
		StringBuffer result = new StringBuffer();
		try {
			URL u = new URL(ICR_URL);
			con = (HttpURLConnection) u.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");

			try (DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());) {
				JSONObject dataRequest = new JSONObject();
				//String imgBase64 = Base64.getEncoder().encodeToString(img);
				dataRequest.put("imageByte", imgBase64);
				dataRequest.put("typeGrain", "");
				outputStream.writeBytes(dataRequest.toString());
				outputStream.flush();
				outputStream.close();
			} catch (java.lang.RuntimeException rr) {
				System.out.println("Exception RuntimeException catched 2");
				rr.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				isOk = false;
			}
			BufferedReader br = null;
			dataICR.httpStatus = con.getResponseCode();
			if (con.getResponseCode() == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String strCurrentLine;
				while ((strCurrentLine = br.readLine()) != null) {
					result.append(strCurrentLine);
				}

			} else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				String strCurrentLine;
				while ((strCurrentLine = br.readLine()) != null) {
					result.append(strCurrentLine);
				}
			}
			dataICR.dataReturn = result.toString();
			JSONObject jsonResult = new JSONObject(result.toString());
			dataICR.mntC = jsonResult.getString("MTC");
			dataICR.mntL = jsonResult.getString("MTL");
			dataICR.codeReponse = jsonResult.getInt("code_reponse");
			dataICR.message = jsonResult.getString("message");
		} catch (java.lang.RuntimeException rr) {
			System.out.println("Exception RuntimeException catched");
			this.errorCode = "ICR.RuntimeException [" + rr.getMessage() + "]";
			rr.printStackTrace();
			isOk = false;
		} catch (Exception e) {
			this.errorCode = "ICR.Exception [" + e.getMessage() + "]";
			e.printStackTrace();
			isOk = false;
		} finally {
			try {
				con.disconnect();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return isOk;
	}
	
	
	public static byte[] convertTiffToJpg(byte[] imageByte) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {

               try (InputStream is = new ByteArrayInputStream(imageByte)) {

                     try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(is)) {

                            Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);

                            if (iterator == null || !iterator.hasNext()) {

                                  throw new RuntimeException("Image file format not supported by ImageIO: ");

                            }

                            // We are just looking for the first reader compatible:

                            ImageReader reader = iterator.next();

                            reader.setInput(imageInputStream);



                            int numPage = reader.getNumImages(true);



                            IntStream.range(0, numPage).forEach(v -> {

                                  try {

                                         final BufferedImage tiff = reader.read(v);

                                         ImageIO.write(tiff, "jpeg", output);

                                  } catch (IOException e) {

                                         e.printStackTrace();

                                  }

                            });

                     }

               }

        } catch (Exception e) {

               e.printStackTrace();

               return null;

        }

        return output.toByteArray();

  }

	/**
	 * Objet qui contient les donn�es retourn�es par l'ICR Montant
	 * 
	 * @author N.BENZAIRA
	 *
	 */
	public class DataICR {

		/**
		 * Montant Chiffre
		 */
		public String mntC;

		/**
		 * Montant lettre
		 */
		public String mntL;

		/**
		 * 1 : ok,-1 : Not Matching, -2 : Format montant not matched
		 */
		public int codeReponse;

		/**
		 * message d'erreur en cas d'erreur
		 */
		public String message;

		/**
		 * statut http retourn� par le serveur AI
		 */
		public int httpStatus;

		/**
		 * la chaine retourn�e par le serveur AI
		 */
		public String dataReturn;
	}

	/**
	 * @return the chequeDataICR
	 */
	public DataICR getDataICR() {
		return dataICR;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}
}
