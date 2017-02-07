package net.saucecode.oneroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HighScore {

	public static final String HOST = "iron.insecure.gq:43934";
	
	public static List<String> getRecentScores(){
		try {
			System.out.println("Downloading recent scores...");
			URL url = new URL("http://" + HOST + "/recentscores.ld");
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			List<String> lines = new ArrayList<String>();
			
			for (String line; (line = reader.readLine()) != null;) {
		        lines.add(line);
		        System.out.println(line);
		    }
			
			return lines;
			
		} catch (Exception e) {
			System.out.println("Failed to download recent scores from scoreboard server.");
		}
		
		return null;
	}
	
	public static List<String> getHighestScores(){
		try {
			System.out.println("Downloading highest scores...");
			URL url = new URL("http://" + HOST + "/highest.ld");
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			List<String> lines = new ArrayList<String>();
			
			for (String line; (line = reader.readLine()) != null;) {
		        lines.add(line);
		        System.out.println(line);
		    }
			
			return lines;
			
		} catch (Exception e) {
			System.out.println("Failed to download highest scores from scoreboard server.");
		}
		
		return null;
	}
	
	public static boolean submitNewScore(String name, int score){
		try {
			System.out.println("Uploading new score...");
			URL url = new URL("http://" + HOST + "/submitscore.ld?name=" + URLEncoder.encode(name, "UTF-8") + "&score=" + score);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			List<String> lines = new ArrayList<String>();
			
			for (String line; (line = reader.readLine()) != null;) {
		        lines.add(line);
		        System.out.println(line);
		    }
			
			return lines.get(0).equalsIgnoreCase("true");
			
		} catch (Exception e) {
			System.out.println("Failed to upload score to scoreboard serevr.");
		}
		
		return false;
	}
}
