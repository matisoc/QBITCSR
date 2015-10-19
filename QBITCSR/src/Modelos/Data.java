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
	                   " PEM        	TEXT	NOT NULL, " + 
	                   " KPV            BLOB	NOT NULL, " +
	                   " P12			TEXT"+ ")"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      c.close();	
		
	}
	

	public void update(String compania, String PEM, String KPV) throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + filedb);
		stmt = c.createStatement();
	    String query = "insert into BSP (COMPANIA, PEM, KPV) "
	    				+ "values ('" + compania + "','" + PEM + "','"+ KPV + "')";
	
		stmt.executeQuery(query);    	    
	}
	

	public boolean getKey (int id) throws ClassNotFoundException, SQLException
	{
		
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + filedb);
		String query="select KPV from BSP where ID='"+id+"'";  
		stmt = c.createStatement();
		try
		{   
			ResultSet rslt=stmt.executeQuery(query);  
	        if(rslt.next())
	        {  
	        	byte[] fileArray=rslt.getBytes("KPV");  
	        	FileOutputStream fos = new FileOutputStream(new File("c:\\aTunes\\clave.key"));
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
		
	
	public Object[] refreshTable () throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + filedb);
		stmt = c.createStatement();  
		rs = stmt.executeQuery(
				"select id, compania, vencimiento,"
				+ "	case when kpv is null then '0' else '1' end as KPV_Bit "
				+ "from bsp ORDER BY ID DESC");
				
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		int colNo = rsmd.getColumnCount();
		Object[] objects = null;
		while(rs.next())
		{
			objects = new Object[colNo];
			for(int i=0;i<colNo;i++)
			{
				switch(i)
				{
					case 3:  objects[i] = rs.getObject(i+1).toString().equals("0") ? Boolean.FALSE : Boolean.TRUE;
							 break;
					default: objects[i]= rs.getObject(i+1);
							 break;
	
				}
				
				
			}
		}
		return objects;
	
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
	
	public void grabar ( String compania, String DEM, String KPV) throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:" + filedb);

	    String query= "insert into BSP (COMPANIA, PEM, KPV) "
    				+ "values (?, ?, ?)";
	        		
	    PreparedStatement prepStmt=null;  
	    try
	    {  
	    	c.setAutoCommit(false);  
	        prepStmt=c.prepareStatement(query);  
	        prepStmt.setString(1, compania);  
	        prepStmt.setString(2, DEM);  
	              
	        byte[] prikey=KPV.getBytes(StandardCharsets.UTF_8); 
	        prepStmt.setBytes(3, prikey);  
	              
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
	
		
		

	     
	    

