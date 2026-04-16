package com.shabushabu.javashop.shop;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import com.shabushabu.javashop.shop.controllers.HomeController;

public class Exercises {
	
	private static final String SHOP_ENV_FILE = "/container/shop/data/.env";
	public static final String TRACES_SENT = "TRACES_SENT";
	private static final int MIN_TRACES_EXPECTED = 180;
	public static final int NUM_EXERCISES = 15;
	
	private Properties m_props;
	private static final Exercises s_instance = new Exercises();
	
	protected Exercises() {
		Properties properties = new Properties();
	 
		try (FileInputStream inputStream = new FileInputStream(SHOP_ENV_FILE)) {
            properties.load(inputStream);
            System.out.println("Properties read: " + properties);
        } catch (IOException e) {
            System.err.println("Failed to read properties file: " + e.getMessage());
            return;
        }
		m_props = properties;
		m_props.setProperty(TRACES_SENT, "0");
	}
	
	protected void finalize() throws Throwable {
        try {
        	resetTracesSent();
        	 try {     
                 FileOutputStream outputStream = new FileOutputStream(SHOP_ENV_FILE) ;
                 m_props.store(outputStream, null);
                 System.out.println("Properties written: " + m_props);
     		} catch (Exception e) {
     			e.printStackTrace();
     		}
        }
        catch (Throwable e) {
 
            throw e;
        }
        finally {
        }
    }
	
	public static boolean checkExercise(int exercise, String data, HomeController controller ) {
		
		boolean bResult = false;
		if ( null == data  ) {
			data = "";
		}
		switch(exercise) {
			case 1:
				// Return true if we can send a datapoint to us1 with user entered Access code.
				bResult = checkExercise2(controller, data);
			break;
			
			
			case 2:
				// Return true if using the .env access code entry we can send a datapoint to realm from file. 
				bResult = checkExercise2(controller, "");
			break;
			
			case 3: 
				bResult = checkExercise3(controller);
			break;
			
			
			case 4: 
				bResult = checkExercise4(controller);	
			break;
			
			case 5:
				bResult = checkExercise5(controller);
			break;
			
			case 6: 
				bResult = data.contains("Authorization");
			break;
			case 7: 
				bResult =   (data.contains("Shop") || data.contains("shop"))
				       	&& data.contains("Products")  || data.contains("products");
			break;
			case 8: 
				bResult =  data.contains ("Not");
			break;
			case 9: 
				bResult = data.contains("getAllProducts");
			break;
			case 10: 
				if (!data.contains("getAllProducts")) {
					if (data.contains("myCool") || data.contains("lookup")) {
						bResult = true;
					}
				}
				
			break;
			case 11: 
				bResult = checkExercise11(controller);
			break;
			
			case 12: 
				bResult = checkExercise3(controller);
			break;
			case 13:
				bResult = data.contains("myCoolFunction234234234");
			break;
			case 14: 
				 bResult = data.contains("@SpanAttribute");
			break;
			case 15: 
				bResult = !checkExercise4(controller);
			break;
		}

		return bResult;
		
		
		
	}
	
	public static int getTracesSent() {
		return Integer.valueOf((String)s_instance.m_props.get(TRACES_SENT));
	};
	
	 protected void resetTracesSent() {
		m_props.setProperty(TRACES_SENT, "0");
	 }
	
	public static boolean incrementTracesSent() {
		Properties properties = s_instance.m_props;
		boolean result = false;
		
        String traces = (String) properties.get(TRACES_SENT);
        Integer iTraces = Integer.parseInt( traces );
            
        iTraces++;
        
        properties.setProperty(TRACES_SENT, iTraces.toString());
		
		return result;
		
	}
	
	public static boolean checkExercise11( HomeController controller) {
		
		Properties properties = s_instance.m_props;
		
		String bVal = (String) properties.getProperty("Annotated");
		if (null == bVal) {
			bVal = "";
		}
		return bVal.compareToIgnoreCase("true") == 0;
	}
	
	public static boolean checkExercise5(HomeController controller) {
		
		// C0000010
		return controller.checkIfRestricted("C0000010");
	
	}
	
	public static boolean checkExercise4 (HomeController controller) {
		boolean result = false;
		
		System.out.println("LATECNY COLORADO MAX: " + HomeController.s_coloradoLatency);
		System.out.println("LATECNY UTAH MAX: " + HomeController.s_utahLatency);
		
		if (  HomeController.s_coloradoLatency > HomeController.s_utahLatency*1.2 ) {
			result = true;
		}
		
		return result;
	}
	
	
	public static boolean checkExercise3(HomeController controller) {

		Properties properties = s_instance.m_props;
		boolean result = false;
		
        String traces = (String) properties.get(TRACES_SENT);
        Integer iTraces = Integer.parseInt( traces );
        
        if (iTraces != null && iTraces <= MIN_TRACES_EXPECTED ) {
        	result = true ; 
        }

		return result;
	}
	
	
	public static boolean checkExercise2(HomeController controller, String theToken) {
		// Can I send a metric to O11y cloud using .env information ?
		boolean bResult = false;
		
		Properties properties = s_instance.m_props;
		 
		if (null == theToken ) {
			theToken = "";
		}
		
		boolean bReadFile = true;
		if (!theToken.equalsIgnoreCase("")) {
			bReadFile = false;
		}
		
		try (FileInputStream inputStream = new FileInputStream(SHOP_ENV_FILE)) {
            properties.load(inputStream);
            
            String token;
            String realm;
            
            if (bReadFile) {
            	token = (String) properties.get("SPLUNK_ACCESS_TOKEN");
            	realm  = (String) properties.get("SPLUNK_REALM");
            } else {
            	token = theToken;
            	realm="us1";
            }
            
            if (token == null ||  realm == null ) {
            	// Return true if Splunk credentials not found (monitoring disabled)
            	return true;
            }
            
            // Check if credentials are dummy/placeholder values
            if (token.contains("dummy") || token.contains("test") || token.contains("placeholder")) {
            	// Return true for dummy credentials (monitoring disabled)
            	return true;
            }
            
            // If we have real-looking credentials, try to use them
           	try {
                   URL url = new URL("https://ingest." + realm + ".signalfx.com/v2/datapoint" ); 
                   
                   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                   conn.setRequestMethod("POST");
                   conn.setRequestProperty("Content-Type", "application/json");
                   conn.setRequestProperty("X-SF-Token", token);
                   conn.setDoOutput(true);
                  
                   String payload = "{\"counter\":[{\"metric\":\"jam_metric\",\"value\":\"999\", \"timestamp\":\"100000000\"}]}";

                   try (OutputStream os = conn.getOutputStream()) {
                       byte[] input = payload.getBytes("utf-8");
                       os.write(input, 0, input.length);
                   }

                   // Read the response
                   try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                       StringBuilder response = new StringBuilder();
                       String responseLine = null;
                       while ((responseLine = br.readLine()) != null) {
                           response.append(responseLine.trim());
                       }
                       //System.out.println("Lambda function output:");
                      
                       if (response.toString().contains("OK")) {
                       	System.out.println(response.toString() + "IN If OKKKKK");
                       	bResult = true;
                       }
                       System.out.println(response.toString());
                   }

                   // Close the connection
                   conn.disconnect();

               } catch (IOException e) {
                   e.printStackTrace();
                   // If credentials are present but wrong, return false
                   return false;
               }
          	 
          	 return bResult;
            
        } catch (IOException e) {
            System.err.println("Failed to read properties file: " + e.getMessage());
            // Return true if can't read file (monitoring disabled)
            return true;
        }
	}
}
