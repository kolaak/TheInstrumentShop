package com.shabushabu.javashop.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shabushabu.javashop.shop.Exercises;
import com.shabushabu.javashop.shop.PropertiesUpdater;
import com.shabushabu.javashop.shop.services.InstrumentService;
import com.shabushabu.javashop.shop.services.ProductService;

import java.util.HashMap;

import javax.naming.NoPermissionException;

@Controller
public class HomeController {
	
	public static long s_coloradoLatency;
	public static long s_utahLatency;

    @Autowired
    private ProductService productService;

    @Autowired
    private InstrumentService instrumentService;
    
    
    @RequestMapping(value="/")
    public String getProductsAllLocations(Model model,  @RequestParam(value="name",required=false) String theName, 
    													@RequestParam(value="location", required=false) String theLocation,													
    													@RequestParam(value="userid", required=false) String userid) throws Exception {
    		
    	Exercises.incrementTracesSent();

    	
    	if (null == theName ) {
		
			theName = "Guest";
		}	
		
		if (null == theLocation ) {
			theLocation="California";
		} 
		
		
		if (null == userid) {
			userid="X0000";
		}
		
		allParameters(theName, theLocation, userid);
		
		User user = new User();
		user.setLocation(theLocation);
		user.setName(theName);
		model.addAttribute("user", user);
		
		long startTime = System.nanoTime();
		//System.out.println("THE START TIME IS ... " + startTime);
		model.addAttribute("products", productService.getProducts(theLocation));
		
		long endTime = System.nanoTime();
		//System.out.println("THE END TIME IS ... " + endTime);
		
		long duration = endTime - startTime;
		
		//System.out.println("THE DURATION IS " + duration);
		if (theLocation.compareToIgnoreCase("Utah") == 0 ) {
			
		//	System.out.println("UTAH Latency Check is ... " + duration);
			if (duration > s_utahLatency) {
		//		System.out.println("Utah latency increased to : " + duration);

				s_utahLatency = duration;
			}
			// Reset Colorado Latency
			s_coloradoLatency = 0;
		} else if (theLocation.compareToIgnoreCase("Colorado") == 0 ) {
			if (s_coloradoLatency < duration ) {
		//		System.out.println("Colorado latency increased to : " + duration);
				s_coloradoLatency = duration;
			}
		}
			
		model.addAttribute("instruments", instrumentService.getInstruments(theLocation));
		
		return "index";
    
    } 
   
    @RequestMapping(value = "/score")
    public @ResponseBody HashMap<String, String> getScores( @RequestParam(value="exercise",required=false)  String exercise,
    														@RequestParam(value="data",required=false)  String data) {
    	if ( null == exercise ) {
    		exercise = "0";
    	}
    	
    	if (null == data) {
    		data = "";
    	}
    	
    	Integer iExercise = Integer.valueOf(exercise);
    	
    	if (iExercise == 0) {
    		return PropertiesUpdater.getListOfScores();
    	} else {
    		HashMap<String, String> result = new HashMap<String, String>();
    		System.out.println("DATA IS FOUND: " + data + " . . .  Exercise is: " + iExercise);
	
    		boolean bResult = Exercises.checkExercise(iExercise, data, this);
    		result.put("exercise" + exercise, bResult ? "true" : "false");
    		return result;
    	}    	
    }
    
  
  
    public void allParameters( String name, String location, 
    		 String userid ) throws NoPermissionException {
    	
    	if (checkIfRestricted(userid) ) {
    		throw new NoPermissionException("User does not have permissions for action requested.");
    	}
    }
    
   
    public boolean checkIfRestricted(String userId) {
    	
    	 boolean bResult = false;
    	/* 
   	 try {
            URL url = new URL("https://mofi2flod5cpeismodr7eonuiu0gkoli.lambda-url.us-west-1.on.aws/?userId=" + userId); 
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
                       
            String payload = "{\"userId\":" ;
            payload = payload + "\"" + userId + "\"}";

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
                
                if (response.toString().contains("Not")) {
                	bResult = true;
                }
            }

            // Close the connection
            conn.disconnect();

        } catch (IOException e) {
            if (e.getMessage().contains("403")) {
            	bResult = true;
            } else {
            	e.printStackTrace();
            }
        }
   	 */
   	 return bResult;
    }
    
    @RequestMapping("/healthcheck")
    @ResponseStatus(code = HttpStatus.OK, reason = "OK")
    public String healthCheck() {
        return "HTTP Status OK (CODE 200)\n";
    }  
    
}
