package com.example.zet_widget;

public class Direction {
	
	public static String removeSuffix(String input){
		String result = null;
		char[] input_array = input.toCharArray();
		result =  new String(input_array, 0, 3);
		
		return (String)result;	
	}
	
	public static String removePrefix(String input){
		if(input == null) return "error";
		String result = null;
		char[] input_array = input.toCharArray();
		/*int i;
		for(i = 0;i < input.length();++i){
			if(Character.isDigit(input_array[i]) || input_array[i] == ' ') input_array[i] = '1'; 
		}
		result = new String(input_array);
		result = result.replaceAll("1", "");
		result = result.trim();
		return result;*/
		result = new String(input_array, 4, input.length()-4);
		return result;
	}
	
	public static String reverseDirection(String input){
		if(input == null) return "error";
		String result = null;
		input = removePrefix(input);
		
		String sub[] = input.split("-");
		
		result = sub[1]  + "-" + sub[0];
		
		return result;
		
	}
	
	public static String getDirection(String input){
		if(input == null) return "error";
		input = removePrefix(input);
		String sub[] = input.split("-");
		return sub[0];
	}
	
	public static String[] getDirectionsSingle(String input){
		
		input = removePrefix(input);
		String sub[] = input.split("-"); 
		return sub;
	}
	public static String[] getDirectionsDouble(String input){
	
		String sub[] = {
			removePrefix(input) , reverseDirection(input)	
		};
		return sub;
	}
}
