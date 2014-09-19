package sm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostLog implements Runnable {

	private String url;
	private String query;
	
	public PostLog(String url, String query){
		this.url = url;
		this.query = query;
	}
	
	@Override
	public void run() {
		 HttpURLConnection con = null ;
	        InputStream is = null;
	        try {
	            con = (HttpURLConnection) ( new URL(url + query)).openConnection();
	            con.setRequestMethod("POST");
	            con.setDoInput(true);
	            con.setDoOutput(true);
	            con.connect();
	             
	            // Let's read the response
	            StringBuffer buffer = new StringBuffer();
	            is = con.getInputStream();
	            BufferedReader br = new BufferedReader(new InputStreamReader(is));
	            String line = null;
	            while (  (line = br.readLine()) != null )
	                buffer.append(line + "\r\n");
	             
	            is.close();
	            con.disconnect();
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        finally {
	            try { is.close(); } catch(Throwable t) {
	            }
	            try { con.disconnect(); } catch(Throwable t) {

	            }
	        }
	}

}
