package archivator;

import java.sql.*;
import java.io.IOException;
import archivator.WriteBinary;


public class BlobFetch {

	/**
	 * @param args
	 */
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String host = args[0] ;
		String db = args[1];
		String user = args[2];
		//String pass = args[3];
		String tbl = args[3];
		String col = args[4];
		String idx = args[5];
		
		Connection con = null;

	    try {
	      Class.forName("com.mysql.jdbc.Driver").newInstance();
	      con = DriverManager.getConnection("jdbc:mysql://"+host+ '/' + db,
	        user, "");
	      //System.out.println(host + db + user + tbl + col);

	      if(!con.isClosed())
	        System.out.println("Successfully connected to " +
	          "MySQL server using TCP/IP...");

	    } catch(Exception e) {
	      System.err.println("Exception: " + e.getMessage());
	    } 
	    
	    try {
	    //Get a Statement object
	    Statement stmt = con.createStatement();
	    
	    ResultSet rs = stmt.executeQuery("select * from "+ db +"."+tbl);
	    rs.first();
	    //System.out.println("Første rad :"+Integer.toString(rs.getRow())); 
	    while (rs.getRow()>-1) 
	    {
	    //System.out.println("Leser rad "+ Integer.toString(rs.getRow()));
	    
	    
	    String FileName = "";
	    System.out.println("Variabler for rad " + Integer.toString(rs.getRow())+" initialisert");
	    
	    
	    //data = rs.getBlob("dok_dokument");
	    //FileName = Integer.toString(rs.getRow())+".doc"; 
	    FileName = rs.getString(idx)+".doc";
	    
	    
	    
	    //java.io.InputStream in = data.getBinaryStream();
	    byte bs[] = new byte[1];
	    WriteBinary w = new WriteBinary();
	    Blob data = null;
	    
	    
	    data = rs.getBlob(col);
	    
	    if (data == null) {
	    	rs.next();
	    }
	    
		/**if ((int) data.length()<1) {
	    	rs.next();	
	    	}**/
	    else {
	    	
	    	bs = data.getBytes(1, (int) data.length());
	    	WriteBinary.main(bs,FileName);
	    	rs.next();
	    	
	    	System.out.println("Skriver fil for rad :" + FileName );
	    	}
	    
	    }
	    /**byte b;
	    byte bs[] = new byte[102400];
	    int index = 0;
	    try {
			while ((in.read()) > -1) {
				
				WriteBinary w = new WriteBinary();
				b = (byte) in.read();
				bs[index] = b;
				index = index + 1;
				System.out.println(b);
				
				WriteBinary.main(bs);
				
				
				
				
			}**/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	    
	    
	    /**ResultSet rs = stmt.executeQuery("select * from city");
	    
	    
	    while(rs.next()){
	        int theInt= rs.getInt("id");
	        String str = rs.getString("District");
	        System.out.println(theInt+" "+str);
	      }//end while loop
	    
	    
	    /**int RadNr = rs.getRow();
	    System.out.println("Rad nr:"+RadNr);
	    rs.next();
	    RadNr = rs.getRow();
	    System.out.println("Rad nr:"+RadNr);
	    **/
	    
	     catch(SQLException e){}
	

	
	      try {
	        if(con != null)
	          con.close();
	      } catch(SQLException e) {}
	    }

	
}
