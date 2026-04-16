package com.shabushabu.javashop.shop;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesUpdater {
	
	private static final String SHOP_PROPS_FILE = "/container/shop/data/shop.properties";
	private static final int NUM_SCORES = 10;
	
	public static final PropertiesUpdater s_instance = new PropertiesUpdater(); 
    
	protected Properties m_props;
	
	protected PropertiesUpdater() {
		Properties properties = new Properties();
	 
		try (FileInputStream inputStream = new FileInputStream(SHOP_PROPS_FILE)) {
            properties.load(inputStream);
            System.out.println("Properties read: " + properties);
        } catch (IOException e) {
            System.err.println("Failed to read properties file: " + e.getMessage());
            return;
        }
	 
		m_props = properties;
	}
	
	public static HashMap<String, String> getListOfScores() {
	
		HashMap<String, String> results = new HashMap<String, String>();
		
		if (null != s_instance.m_props) {
			
			Enumeration<?> exercises = s_instance.m_props.propertyNames();
				
			while (exercises.hasMoreElements()) {
				String key = (String) exercises.nextElement();
				System.out.println(key + " -- " + s_instance.m_props.getProperty(key) + " : " +  s_instance.m_props.getProperty(key) );
				results.put(key, s_instance.m_props.getProperty(key));
			}
		}
		
		return results;
	}
	
	public static String getScore(int exercise) {
		if (null == s_instance.m_props) {
			return "false";
		}
		
		return  s_instance.m_props.getProperty("exercise" + exercise);	
	}
	
	
	
	public static void setScore(int exercise, boolean passed) {
		  
		if (null == s_instance.m_props) {
			return ;
		}
		
		s_instance.m_props.setProperty("exercise" + exercise, passed ? "true" : "false" );
		   
		s_instance.storeTheScores();
	
	}
	
	protected void resetScores() {
		
		for (int i=1; i<=PropertiesUpdater.NUM_SCORES; i++ ) {
			PropertiesUpdater.setScore(i , false);
		}
	}
	
	protected void storeTheScores() {
		try (FileOutputStream outputStream = new FileOutputStream(SHOP_PROPS_FILE)) {
			s_instance.m_props.store(outputStream, null);
	        System.out.println("Properties written: " + s_instance.m_props);
		} catch (IOException e) {
		    System.err.println("Failed to write properties file: " + e.getMessage());
		}
	}

    public static void doPropsTest() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(SHOP_PROPS_FILE)) {
            properties.load(inputStream);
            System.out.println("Properties read: " + properties);
        } catch (IOException e) {
            System.err.println("Failed to read properties file: " + e.getMessage());
            return;
        }

        properties.setProperty("exercise1", "true");
        properties.setProperty("exercise2", "false");

        try (FileOutputStream outputStream = new FileOutputStream(SHOP_PROPS_FILE)) {
            properties.store(outputStream, null);
            System.out.println("Properties written: " + properties);
        } catch (IOException e) {
            System.err.println("Failed to write properties file: " + e.getMessage());
        }
    }
    
    
}