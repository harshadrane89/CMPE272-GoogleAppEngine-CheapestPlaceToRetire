package com.scratchpad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;




public class Scratch {

	
	public static void main(String[] args) {
		ArrayList<String> ref=new ArrayList<String>();
		List<String> newe=new ArrayList<String>();
		ref.add("Hello");
		ref.add("Hiii");
		ref.add("Hiii");
		ref.add("Hiii");
		ref.add("Hiii");
		ref.add("Hiii");
		
		newe=ref.subList(0, 5);
		ref=new ArrayList<String>(newe);
	}
}
