package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import com.model.Url;

public class UrlDao {
	
	private Connection con;
	
	
	public void setupConnectionToDb()
	{
		String url = "jdbc:mysql://localhost:3306/urlDB"; 
	    String uname = "root"; 
	    String password = "chu73prag"; 

		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,uname,password);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Url findByShortLink(String shortLink)
	{
		Url url = null;
		String query = "SELECT * FROM urlTable WHERE shortLink = ?";
		try 
		{
			
		setupConnectionToDb();
		
		PreparedStatement preparedStatement = con.prepareStatement(query);
	    preparedStatement.setString(1, shortLink);
	    
	    ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            url = new Url();
            url.setId(rs.getLong("id"));
            url.setOriginalUrl(rs.getString("originalUrl"));
            url.setShortLink(rs.getString("shortLink"));
            url.setCreationDate(rs.getObject("creationDate", LocalDateTime.class));
            url.setExpirationDate(rs.getObject("expirationDate", LocalDateTime.class));
        }

        rs.close();
        preparedStatement.close();
        con.close();
    } catch (Exception e) {
        e.printStackTrace(); // Handle exceptions appropriately in your application.
    }
		return url;
		
	}
	
	public void delete(Url url)
	{
		try {
            setupConnectionToDb();

            
            String deleteQuery = "DELETE FROM urlTable WHERE id = ?";

            
            PreparedStatement preparedStatement = con.prepareStatement(deleteQuery);

            
            preparedStatement.setLong(1, url.getId());

            
            int deletedRows = preparedStatement.executeUpdate();

            if (deletedRows > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("Record with ID " + url.getId() + " not found.");
            }

            preparedStatement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
	}

	public Url save(Url url) {
		try {
            setupConnectionToDb();

            
            String insertQuery = "INSERT INTO urlTable(id,originalUrl,shortLink,creationDate,expirationDate) VALUES(?,?,?,?,?)";

            
            PreparedStatement preparedStatement = con.prepareStatement(insertQuery);

            
            preparedStatement.setLong(1, url.getId());
            preparedStatement.setString(2, url.getOriginalUrl());
            preparedStatement.setString(3, url.getShortLink());
            preparedStatement.setObject(4, url.getCreationDate());
            preparedStatement.setObject(5, url.getExpirationDate());

            
            int insertedRows = preparedStatement.executeUpdate();

            if (insertedRows > 0) {
                System.out.println("Record inserted successfully.");
            } else {
            	System.out.println("Insertion failed. No rows inserted.");
            }

            preparedStatement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return url;
	}
	
	public long getLatestId()
	{
		long latestID=0;
		
		try
		{
			setupConnectionToDb();
			String idQuery= "SELECT MAX(id) FROM urlTable";
			
			PreparedStatement preparedStatement = con.prepareStatement(idQuery);
			ResultSet rs = preparedStatement.executeQuery();
			
			if (rs.next()) {
				latestID= rs.getLong("MAX(id)");
			}
			
			rs.close();
	        preparedStatement.close();
	        con.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return latestID;
	}
	
	

}
