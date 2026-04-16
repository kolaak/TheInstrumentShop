

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class GenerateTraffic {
	
	private static final String TESTER_PROPS_FILE = "/container/test/data/tester.properties";
	private static final String TESTER_FIRST_RUN = "FirstRun";

	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
	
		 Properties properties = new Properties();
		 try (FileInputStream inputStream = new FileInputStream(TESTER_PROPS_FILE)) {
			 properties.load(inputStream);
			 System.out.println("Properties read: " + properties);
		 } catch (IOException e) {
			 System.err.println("Failed to read properties file: " + e.getMessage());
			 return;
		 }

		 Boolean firstTimeAutoBoot = Boolean.parseBoolean((String)properties.getProperty(TESTER_FIRST_RUN));

		 if (firstTimeAutoBoot) {

			 properties.setProperty(TESTER_FIRST_RUN, "False");
			 try (FileOutputStream outputStream = new FileOutputStream(TESTER_PROPS_FILE)) {
				 properties.store(outputStream, null);
				 System.out.println("Properties written: " + properties);
				 return;
			 } catch (IOException e) {
				 System.err.println("Failed to write properties file: " + e.getMessage());
			 }
		 }
		 

		 try {
			 Thread.sleep(20000);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }

		 String url ="http://shop:8010";
		boolean chicago = false;
		
		if (null != args && args.length >0 ) {
			chicago = args[0].equalsIgnoreCase("-chicago");
		}
		
		System.out.println("Utah Location");
		for (int j=0; j<40; j++) {
			try {
		
				HttpClient client = HttpClient.newHttpClient();
				String theURL = url + "?name=Guest&location=Utah&userid=U" + "00000" + j;
			
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(theURL)).build();
				
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
				@SuppressWarnings("unused")
				String sResult = response.body().toString();
			 
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println("California Location");
		long startTime = System.nanoTime();
			
		for (int k=0; k<40; k++) {
				
			try {
			
				HttpClient client = HttpClient.newHttpClient();
				String theURL =  url + "?name=Guest&location=California&userid=C" + "00000" + k;
			
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(theURL)).build();
				
				@SuppressWarnings("unused")
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
				//String sResult = response.body().toString();
			 
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println("Oregon Location");
		for (int j=0;  j<30; j++) {
			try {
		
				HttpClient client = HttpClient.newHttpClient();
				String theURL = url + "?name=Guest&location=Oregon&userid=O" + "00000" + j;
			
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(theURL)).build();
				
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
				@SuppressWarnings("unused")
				String sResult = response.body().toString();
			 
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		
		System.out.println("CALIFORNIA DURATION IS: " + duration /  1000000000);

		if (!chicago) {	
			 startTime = System.nanoTime();
		
			System.out.println("Colorado Location");
		
			for (int l=0; l<40; l++) {
				try {
					HttpClient client = HttpClient.newHttpClient();
					String theURL =  url + "?name=Guest&location=Colorado&userid=CC" + "00000" + l;
			
					HttpRequest request = HttpRequest.newBuilder().uri(URI.create(theURL)).build();
				
					HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
					String sResult = response.body().toString();
			 
				} catch(Exception e) {
					e.printStackTrace();
				}
			
			}
		
		
			endTime = System.nanoTime();

			duration = (endTime - startTime);
		
			System.out.println("COLORADO DURATION IS: " + duration/ 1000000000);
	        
		} else {
			startTime = System.nanoTime();
			
			System.out.println("Chicago Location");
			
			for (int l=0; l<2; l++) {
				try {
			
					HttpClient client = HttpClient.newHttpClient();
					String theURL =  url + "?name=Guest&location=Chicago";
				
					HttpRequest request = HttpRequest.newBuilder().uri(URI.create(theURL)).build();
					
					HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				
					String sResult = response.body().toString();
				 
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			 endTime = System.nanoTime();
	
			 duration = (endTime - startTime);
			
			System.out.println("CHICAGO DURATION IS: " + duration/ 1000000000);
			
			
	     }
	}
}
