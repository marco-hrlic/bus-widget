package com.example.zet_widget;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

public class XmlParser {
	public static String parseXml(Time input_time, String input_linija, String input_smjer, Context context) { 
		  String data = null; 
		 
		  // sax stuff 
		  try { 
		    SAXParserFactory spf = SAXParserFactory.newInstance(); 
		    SAXParser sp = spf.newSAXParser(); 
		 
		    XMLReader xr = sp.getXMLReader(); 
		 
		    
		    
		    DataHandler dataHandler = new DataHandler(); 
		    xr.setContentHandler(dataHandler); 
		    
		    Log.d("blabla", "sending data to handler");	    
			dataHandler.recieveInputData(input_time, input_linija, input_smjer);
		    
		    Log.d("blabla", "opening xml file");
		    xr.parse(new InputSource(context.getAssets().open("zagreb/" + input_linija + ".xml"))); 
		    
		    
		    
		    Log.d("blabla", "getting data from handler");
		    data = dataHandler.getData(); 
		 
		  } catch(ParserConfigurationException pce) { 
		    Log.e("SAX XML", "sax parse error", pce); 
		  } catch(SAXException se) { 
		    Log.e("SAX XML", "sax error", se); 
		  } catch(IOException ioe) { 
		    Log.e("SAX XML", "sax parse io error", ioe); 
		  } 
		 
		  return data; 
		} 
	
}
