package archivator;

import java.sql.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import archivator.WriteBinary;


public class BlobFetch {

		
	
	
	public static void main (String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//Initialiserer variabler
		int docNo = 0;
		int rowNo = 0;
		int highId = 0;
		
		String host = args[0];
		String db = args[1];
		String user = args[2];
		//String pass = args[3];
		String tbl = args[3];
		//String col = args[4];
		//String idx = args[4];
		
		Connection con = null;

	    //Initialiserer tilkobling til database
		try {
	      Class.forName("com.mysql.jdbc.Driver").newInstance();
	      con = DriverManager.getConnection("jdbc:mysql://"+host+ '/' + db,
	        user, "");

	      if(!con.isClosed())
	        System.out.println("Successfully connected to " +
	          "MySQL server using TCP/IP...");
	      
	    

	    } catch(Exception e) {
	      System.err.println("Exception: " + e.getMessage());
	    } 
	    
	    try {
	    	//Get a Statement object
		    Statement stmt = con.createStatement();
		    
		    //Henter ut antall unike ID'er i tabellen
		    ResultSet rs = stmt.executeQuery("SELECT count(distinct ID) as docs from "+ db + "." + tbl);
		    
		    rs.first();
		    docNo = rs.getInt("docs");
		    
		    //Teller antall rader i tabellen
		    ResultSet rt = stmt.executeQuery("SELECT count(*) as rows from "+db+"."+tbl);
		    rt.last();
		    rowNo = rt.getInt("rows");
		    
		    
		    ResultSet ru = stmt.executeQuery("SELECT max(ID) as highId from "+ db + "." +tbl);
		    ru.first();
		    highId = ru.getInt("highId");
		    
		    //Skriv en tilbakemelding om antall filer i tabellen og antall rader
		    System.out.println("Tabellen inneholder " + docNo +" dokumenter fordelt på " + rowNo + " rader");
		    
		    
		   
		    //con.close();
		    rs = null;
		    rt =null;
	    	}
	    
	    catch(SQLException e){}
	    
	    
	   
	    
	    
	    
	    try {
		    //Get a Statement object
		    Statement stmt = con.createStatement();
	    
	    
		    for(int i = 0; i < highId; i++)
		    	{
		    	
		    	//Hent alle rader for gjeldende fil
			    ResultSet rs = stmt.executeQuery("SELECT * FROM " + db + " ." + tbl +" WHERE ID = " + i + " order by seq");
			    //System.out.println(i);
			    //System.out.println(docNo);
			    rs.first();
			    System.out.println("Henter data..."); 
		    
			    
		    	

			    //Henter antall segmenter for fil
			    
			    rs.last();
			    int seqNo = rs.getRow();
			    
			    if (seqNo == 0)
			    	{
			    	System.out.println("Ingen rader for gitt ID");
			    	}
			    
			    else
			    {
			    		    
				    String fileName = "";
				    fileName = rs.getString("ID"); 
				    String fileType = "";
				    
				    rs.first();
				    
				    //Sjekk at filen er RTF-fil, eller lagre som txt
				    if (rs.getString("BLOCK1").startsWith("{" + '\\' + "rtf"))
				    {
				    	System.out.println("RTF-fil");
				    	fileType = "rtf";
				    }
			    	
				    else 
				    {
				    	System.out.println("Ukjent fil");
				    	fileType = "txt";
				    }
				    		    		    		
		    
				    Writer outFile = new FileWriter(fileName + "." + fileType);
		   
				    System.out.println("Innhold for fil nr." + fileName+" initialisert");
			    
			    
				    System.out.println("Filen har "+ seqNo +" biter");
				    
				    String fileRow="";
				    String[] fileCont = new String[seqNo];
			    
			    
				    //rs.first();
				    
				   
				    //Slå sammen BLOCK1 og BLOCK2 til èn tekststreng for hver rad
				    for(int j = 0; j<seqNo; j++) 
				    	{
				    	//System.out.println("Leser rad "+ Integer.toString(rs.getRow()));
				    	String block1 = rs.getString("BLOCK1");
				    	String block2 = rs.getString("BLOCK2");
				    	
				    	if (block1 == null) {
				    			block1 = "";
				    	}
				    	
				    	if (block2 == null) {
			    			block2 = "";
				    	}
				    	
				    	fileRow = block1 + block2;	    
				    	 
				    	//System.out.println("Innhold for fil nr." + fileName+" initialisert");
				    	//System.out.println(fileRow);
				    	fileCont[j]= fileRow;
				    					    	
				    	
					    outFile.write(fileRow);
					    rs.next();
				    
				      }
				
				System.out.println("Fil " + i + " skrevet.");
			    
				//Lukker fil
				outFile.close();
			    System.out.println("Fil lukket.");
			      
			    }
	    
	    	}
	    }
	    
	     catch(SQLException e){}
	

	
	      try {
	        if(con != null)
	          con.close();
	      } catch(SQLException e) {}
	    }

	
}
