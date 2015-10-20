package Modelos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class Data 
{
	File db;
	String filedb;
	Connection c;
	Statement stmt;
	ResultSet rs;

	public Data()
	{
		filedb = "qbit.bsp";
		db = new File(filedb);
		c = null;
		stmt= null;	
	}
	
	private void crearTabla() throws IOException, ClassNotFoundException, SQLException
	{
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:" + filedb);
	    stmt = c.createStatement();
	    String sql = "CREATE TABLE BSP " +
	                   "(ID INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL," +
	                   " COMPANIA       VARCHAR(100)    NOT NULL, " + 
	                   " VENCIMIENTO    DATE     , " + 
	                   " KPV            BLOB	NOT NULL, " +
	                   " KPB            BLOB	NOT NULL, " +
	                   " PEM            BLOB	NOT NULL, " +
	                   " CRT            BLOB	, " + 
	                   " P12			BLOB"+ ")"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.close();	
	}
	

	public void update(String compania, String KPV, String KPB, String PEM) throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + filedb);
		stmt = c.createStatement();
	    String query = "insert into BSP (COMPANIA, KPV, KPB, PEM) "
	    		+ "values ('" + compania + "','" + KPV + "','"+ KPB + "','" + PEM + "')";
		stmt.executeQuery(query);    	    
	}

	
	
	
	
	public boolean blobToFile (int id, int type, String path) throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + filedb);
		String query = null;
		byte[] fileArray = null;
		switch(type)
		{
			case 0:	query="select KPV from BSP where ID='"+id+"'";
			case 1: query="select KPB from BSP where ID='"+id+"'";
			case 2: query="select PEM from BSP where ID='"+id+"'";
			case 3: query="select CRT from BSP where ID='"+id+"'";
			case 4: query="select P12 from BSP where ID='"+id+"'";
		}
		
		stmt = c.createStatement();
		try
		{   
			ResultSet rslt=stmt.executeQuery(query);  
	        if(rslt.next())
	        {  
	        	switch(type)
	        	{
					case 0:	fileArray=rslt.getBytes("KPV");
							break;
					case 1: fileArray=rslt.getBytes("KPB");
							break;
					case 2: fileArray=rslt.getBytes("PEM");
							break;
					case 3: fileArray=rslt.getBytes("CRT");
							break;
					case 4: fileArray=rslt.getBytes("P12");
							break;
	        	}
	        	FileOutputStream fos = new FileOutputStream(new File(path));
	        	fos.write(fileArray);
	        	fos.close();
	        	return true;
	        
	        }  
	        rslt.close();  

	        }catch(Exception e){  
	            e.printStackTrace();
	            return false;
	        }finally{  
	            try {  
	                c.close();  
	                stmt.close();
	                
	            } catch (Exception e) {  
	            }  
	        }  
	          
	        return true;  
	    }  
		

	public String blobToString (int id, int type) throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + filedb);
		String query = null, ret = null;
		byte[] fileArray = null;
		stmt = c.createStatement();
		query="select KPV,KPB,PEM,CRT,P12 from BSP where ID='"+id+"'";
		try
		{   
			ResultSet rslt=stmt.executeQuery(query);  
	        if(rslt.next())
	        {  
	        	switch(type)
	        	{
					case 0:	fileArray=rslt.getBytes("KPV");
							break;
					case 1: fileArray=rslt.getBytes("KPB");
							break;
					case 2: fileArray=rslt.getBytes("PEM");
							break;
					case 3: fileArray=rslt.getBytes("CRT");
							break;
					case 4: fileArray=rslt.getBytes("P12");
							break;
	        	}
	        	ret= new String(fileArray, StandardCharsets.UTF_8);
	        }  
	        rslt.close();  

	        }catch(Exception e){  
	            e.printStackTrace();
	            return null;
	        }finally{  
	            try {  
	                c.close();  
	                stmt.close();
	                
	            } catch (Exception e) {  
	            }  
	        }  
	          
	        return ret;  
	    }  
		
	
	public LinkedList <Object[]> refreshTable () throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + filedb);
		stmt = c.createStatement();  
		rs = stmt.executeQuery(
				"select id, compania, vencimiento,"
				+"	case when PEM is null then '0' else '1' end as PEM_Bit, "
				+"	case when CRT is null then '0' else '1' end as CRT_Bit, "
				+"	case when P12 is null then '0' else '1' end as P12_Bit "
				+ "from bsp ORDER BY ID DESC");
				
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		int colNo = rsmd.getColumnCount();
		LinkedList <Object[]> linkedlist = new LinkedList<Object[]>();
		
		while(rs.next())
		{
			Object[] objects = new Object[colNo];
			for(int i=0;i<colNo;i++)
			{
				switch(i)
				{
					case 3: objects[i]= rs.getObject(i+1).toString().equals("0") ? Boolean.FALSE : Boolean.TRUE;  
							break;
					case 4: objects[i]= rs.getObject(i+1).toString().equals("0") ? Boolean.FALSE : Boolean.TRUE;  
							break;
					case 5: objects[i]= rs.getObject(i+1).toString().equals("0") ? Boolean.FALSE : Boolean.TRUE;  
							break;
					
					default: objects[i]= rs.getObject(i+1);
							break;
				}

			}
			linkedlist.add(objects);	
		}
		return linkedlist;
	}
	
	
	public void connect() throws IOException, Exception 
	{
		Class.forName("org.sqlite.JDBC");
		if(db.exists())
		{	
			System.out.println("Existe el archivo.."+"\n");
			c = DriverManager.getConnection("jdbc:sqlite:" + filedb);
			stmt = c.createStatement();
			String sql = "select * from BSP";
			try
			{
				stmt.executeQuery(sql);
				stmt.close();
				c.close();
				
				System.out.println("Existe la tabla.."+"\n");
			}
			catch(SQLException s)
			{
				System.out.println("Se va a crear la tabla....."+"\n");
				crearTabla();
			}
		}
		else
		{
			System.out.println("No existe el archivo....."+"\n");
			db.createNewFile();
			crearTabla();
			System.out.println("Se creo el archivo y la tabla........"+"\n");		
		}	
	}
	
	public void grabar ( String compania, String KPV, String KPB, String PEM) throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + filedb);

	    String query= "insert into BSP (COMPANIA, KPV, KPB, PEM) "
    				+ "values (?, ?, ?, ?)";
       		
	    PreparedStatement prepStmt=null;  
	    try
	    {  
	    	c.setAutoCommit(false);  
	        prepStmt=c.prepareStatement(query);  
	        prepStmt.setString(1, compania);  

	        byte[] prikey=KPV.getBytes(StandardCharsets.UTF_8); 
	        prepStmt.setBytes(2, prikey);
	        
	        byte[] pubkey=KPB.getBytes(StandardCharsets.UTF_8); 
	        prepStmt.setBytes(3, pubkey);
	        
	        byte[] pem=PEM.getBytes(StandardCharsets.UTF_8); 
	        prepStmt.setBytes(4, pem);  
	              
	        prepStmt.executeUpdate();  
	        c.commit();  
	        System.out.println("Grabo ok");  
	    }
	    catch(Exception e)
	    {  
	    	e.printStackTrace();  
	    }
	    finally{  
	            try {  
	                c.close();  
	                prepStmt.close();  
	            } catch (Exception e) {  
	            }  
	        }  
	    }  
	      	
		
	}
	
		
		

	     
	    

