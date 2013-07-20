package com.example.zet_widget;

import java.util.Calendar;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.format.Time;

/*
 * SAX XML parser handler
 */



public class DataHandler extends DefaultHandler{
	
	public boolean inLinija = false, inSmjer = false, 
				   inDan = false, inSati = false, inMinute = false, 
				   resultFound = false, flag = false, flag_nema = false;
	
	public static String result, linija, smjer, sati = null, minute = null;
	public Time time = new Time();
	int dayOfTheWeek;
	
	
	
	public void recieveInputData(Time input_time, String input_linija, String input_smjer){
		time.setToNow();
		linija = input_linija;
		smjer = input_smjer;
	}
	
	/** 
	   * Returns the data object 
	   * 
	   * @return 
	   */ 
	public String getData(){
		if(result != null && resultFound) return result;
		return "error";
	}
	
	/** 
	   * This gets called when the xml document is first opened 
	   * 
	   * @throws SAXException 
	   */ 
	@Override
	public void startDocument() throws SAXException {
		
	}
	
	
	/** 
	   * Called when it's finished handling the document 
	   * 
	   * @throws SAXException 
	   */ 
	@Override 
	public void endDocument() throws SAXException { 
		
	}
	
	
	
	/** 
	   * This gets called at the start of an element. Here we're also setting the booleans to true if it's at that specific tag. (so we 
	   * know where we are) 
	   * 
	   * @param namespaceURI 
	   * @param localName 
	   * @param qName 
	   * @param atts 
	   * @throws SAXException 
	   */ 
	@Override 
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException { 
	 
		
		if(localName.equals("linija") && !resultFound) { 
	    	if(atts.getValue("id").equalsIgnoreCase(linija)) inLinija = true;
	    }
	
		else if(localName.equals("smjer") && inLinija && !resultFound) {
	    	if (atts.getValue("id").equalsIgnoreCase(smjer)) inSmjer = true;
	    }
	    
		else if(localName.equals("dan") && inSmjer && !resultFound) {
			Calendar calendar = Calendar.getInstance();
			dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
	    	if(Integer.valueOf(atts.getValue("id")) == dayOfTheWeek || (Integer.valueOf(atts.getValue("id")) > 1 
	    			&& Integer.valueOf(atts.getValue("id")) < 7) ) inDan = true;
	    }
		
		
		
		
		
		else if(localName.equals("nema_cjeli_dan") && inDan && !resultFound){
			flag_nema = true;
			resultFound = true;
		}
		
		else if(localName.equals("kraj") && inDan){
			//set the time to 0:00 if there are no buses left today
			time.set(0, 0, 0);
		}
		
		
		
		
		else if((localName.equals("sati")) && inDan && 
				(time.hour <= Integer.valueOf(atts.getValue(0)))  && (!resultFound)){
			
	    	inSati = true;
	    	sati = atts.getValue(0).trim();
	    	if ((localName.equals("sati")) && (time.hour < Integer.valueOf(atts.getValue(0)))) flag = true;
	    }
	    
		else if((localName.equals("minute")) && inSati && (!resultFound) ){
	    	inMinute = true;
	    } 
		
		
	    
	}
	
	/** 
	   * Called at the end of the element. Setting the booleans to false, so we know that we've just left that tag. 
	   * 
	   * @param namespaceURI 
	   * @param localName 
	   * @param qName 
	   * @throws SAXException 
	   */ 
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if(localName.equals("linija")){
			inLinija = false;
		}
		else if(localName.equals("smjer")) {
	    	inSmjer = false;
	    }
	    
		else if(localName.equals("dan")) {
	    	inDan = false;
	    }
	    
	    else if(localName.equals("sati")){
	    	inSati = false;
	    	
	    }
	    
	    else if(localName.equals("minute")){
	    	inMinute = false;
	    }
	    else if (localName.equals("nema_cjeli_dan")){
	    	flag_nema = false;
	    }
	}
	
	
	/** 
	   * Calling when we're within an element. Here we're checking to see if there is any content in the tags that we're interested in 
	   * and populating it in the Config object. 
	   * 
	   * @param ch 
	   * @param start 
	   * @param length 
	   */ 
	@Override 
	public void characters(char ch[], int start, int length) { 
		
		//Get the minutes
		String chars = new String(ch, start, length); 
		chars = chars.trim(); 
		
	    if(inMinute) { 
	    	int min = Integer.valueOf(chars);
	    	if (min < 60 && min >= time.minute && !flag){
	    		resultFound = true;
	    		minute = Integer.toString(min);
	    		
	    		//A bit of formating
	    		if(sati.length() == 1) sati = "0" + sati;
	    		if(minute.length() == 1) minute = "0" + minute;
	    		
	    		//Handle errors
	    		if(sati == null) sati = "error";
	    		else if(minute == null) minute = "error";
	    		else{
	    			//final result
	    			result = "|" + sati + ":" + minute;
	    		}
	    	}
	    	
	    	
	    	if(flag && min < 60){
	    		resultFound = true;
	    		minute = Integer.toString(min);
	    		
	    		if(sati.length() == 1) sati = "0" + sati;
	    		if(minute.length() == 1) minute = "0" + minute;
	    		
	    		if(sati == null) sati = "error";
	    		if(minute == null) minute = "error";
	    		result = "|" + sati + ":" + minute;
	    	  
	    	} 
	    	
	    	
	    }
	 
	    else if(flag_nema){
    		result = "|danas nema buseva";
    	} 
	    
	     
	}
	
	
}


