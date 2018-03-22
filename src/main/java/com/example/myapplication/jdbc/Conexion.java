package com.example.myapplication.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
	
	
	public Connection conexion = null;
	public String hostName = "meditacionserver.database.windows.net";
    public String dbName = "MeditacionDB";
    public String user = "meditacion";
    public String password = "Server1974.";
    public String url = "jdbc:sqlserver://"+hostName+":1433;databaseName="+dbName+";user="+user+";password="+password+";Trusted_Connection=False;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    
    public Conexion () {
    	
    	try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conexion = DriverManager.getConnection(url);
	        String schema = conexion.getSchema();
	        
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("conexión fallida");
			e.printStackTrace();
		}

    }
    
    public Connection getConnection() {
    	return conexion; // Devuelve el objeto conection
    	
    }

}
