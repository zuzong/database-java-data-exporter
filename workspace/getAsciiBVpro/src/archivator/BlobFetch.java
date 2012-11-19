package archivator;

import java.sql.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javax.swing.JOptionPane;

import archivator.WriteBinary;


public class BlobFetch {

		
	
	
	public static void main (String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//Initialiserer variabler
		int docNo = 0;
		int rowNo = 0;
		int highId = 0;
		
		//Vis info
		JOptionPane.showMessageDialog(null, "Dette programmet henter ut dokumenter fra gamle Visma/Unique-systemer\nsom lagrer dokumentene i blokker i tabellen UQWIDETAB.\n\nProgrammet fungerer ikke for systemer der dokumentene er lagret\nsom BLOB-elementer." );
		
		//Motta informasjon om databasen
		String host = JOptionPane.showInputDialog("Tast inn IP-adresse eller hostname\nfor MySQL-databasen", "localhost");
		String db = JOptionPane.showInputDialog("Tast inn navn på database/schema\nder dokumentene ligger", "");
		String user = JOptionPane.showInputDialog("Tast inn brukernavn for databasen", "root");
		String pass = JOptionPane.showInputDialog("Tast inn eventuelt passord\nfor databasen", "");
		String tbl = JOptionPane.showInputDialog("Tast inn navn på tabellen der\ndokumentene ligger", "TEKSTER");
		//String col = args[4];
		//String idx = args[4];
		
		Connection con = null;

	    //Initialiserer tilkobling til database
		try {
	      Class.forName("com.mysql.jdbc.Driver").newInstance();
	      con = DriverManager.getConnection("jdbc:mysql://"+host+ '/' + db,
	        user, pass);

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
		    ResultSet rs = stmt.executeQuery("SELECT count(distinct VEDTAK) as docs from "+ db + "." + tbl);
		    
		    rs.first();
		    docNo = rs.getInt("docs");
		    
		    //Teller antall rader i tabellen
		    ResultSet rt = stmt.executeQuery("SELECT count(*) as rows from "+db+"."+tbl);
		    rt.last();
		    rowNo = rt.getInt("rows");
		    
		    
		    ResultSet ru = stmt.executeQuery("SELECT count(*) as HighId from "+ db + "." +tbl);
		    ru.first();
		    highId = ru.getInt("highId");
		    
		    //Skriv en tilbakemelding om antall filer i tabellen og antall rader
		    //System.out.println("Tabellen inneholder " + docNo +" dokumenter fordelt på " + rowNo + " rader");
		    JOptionPane.showMessageDialog(null, "Tabellen inneholder " + docNo +" vedtak fordelt på "+ rowNo +" rader.");
		    
		    
		   
		    //con.close();
		    rs = null;
		    rt =null;
	    	}
	    
	    catch(SQLException e){}
	    
	    
	   
	    
	    
	    
	    try {
		    //Get a Statement object
		    Statement stmt = con.createStatement();
	    
	    
		    
		    	
		    	//Hent alle rader for gjeldende fil
			    ResultSet rs = stmt.executeQuery("SELECT * FROM " + db + " ." + tbl + " where TEKST_ORA is not null order by VEDTAK, NR");
			    //System.out.println(i);
			    //System.out.println(docNo);
			    rs.first();
			    System.out.println("Henter data..."); 
			    while (!rs.isLast())
			    
			    {
				    String fileName = "";
				    fileName = (rs.getString("VEDTAK") +"_" + rs.getString("NR")); 
				    String fileType = "";
				    
				    				    
				    //Sjekk at filen er RTF-fil, eller lagre som txt
				    if (rs.getString("TEKST_ORA").startsWith("{" + '\\' + "rtf"))
				    {
				    	System.out.println("RTF-fil");
				    	fileType = "rtf";
				    }
			    	
				    else 
				    {
				    	System.out.println("Ren tekst");
				    	fileType = "txt";
				    }
				    		    		    		
		    
				    Writer outFile = new FileWriter(fileName + "." + fileType);
		   
				    System.out.println("Innhold for fil nr." + fileName+" initialisert");
			    
			    
				    //System.out.println("Filen har "+ seqNo +" biter");
				    
				    String fileRow = rs.getString("TEKST_ORA");
				    System.out.println(fileRow);
				    
				    outFile.write(fileRow);
				    
				    //String[] fileCont = new String[seqNo];
			    
			    
				    //rs.first();
				    
				   
				    
				    System.out.println("Fil skrevet.");
			    
				    //Lukker fil
				    outFile.close();
				    System.out.println("Fil lukket.");
				    rs.next();
			      
			    
	    
			    }
		    
		    JOptionPane.showMessageDialog(null, "Dokumentuttrekk ferdig!");
	    }
	    
	     catch(SQLException e){}
	

	
	      try {
	        if(con != null)
	          con.close();
	      } catch(SQLException e) {}
	    }

	
}
