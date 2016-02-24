/*
 * Copyright (c) 2002-2005 Universidade Federal Alagoas - Campus Arapiraca
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
*/

package br.edu.ufal.cad.cbir.texture.db;



import java.io.File;
import java.sql.*;
import java.util.LinkedList;


public class DataBaseManager {

	private String driver = "com.mysql.jdbc.Driver";
	private String URL = "jdbc:mysql://localhost/cadcancer";//Vc tem que colocar o nome do seu banco. Vc coloca o localhost se o MySql estive na mesma maquina da aplicação, se não vc tem que colocar o ip do servidor de Dados.  
	private String USE = "cad";// Geralmente é root a não ser que vc mude  
	private String SENHA = "cad";//seu senha  

	Statement statement;

	private Connection connection;

	public void teste(){  

	}

	public DataBaseManager() {
		try{  
			Class.forName(driver);  
			connection = (Connection) DriverManager.getConnection(URL, USE, SENHA);  

			this.statement = (Statement) connection.createStatement();  

		}catch(Exception e){  
			e.printStackTrace();
		}
	}

	public Connection getConnection()
	{
		return this.connection;
	}

	public Statement getStatement()
	{
		return this.statement;
	}

	public void addSegmentedImagesToDatabase(String parentPath)
	{
		if((new File(parentPath).isDirectory()))
		{
			String sql = "insert into textura (path) values (\'"+parentPath+"\')";

			try {
				this.statement.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Returns the list of segmented images from 
	 * the database.
	 * 
	 * @return 
	 */
	public LinkedList<SegImages> getDBSegImages()
	{
		LinkedList<SegImages> ims = null;
		
		try {
			
			//ResultSet rs = this.statement.executeQuery("select * from textura");
			ResultSet rs = this.statement.executeQuery("select * from nodulo");
			
			ims = new LinkedList<SegImages>();
			
			while(rs.next())
			{
				//SegImages im = new SegImages(rs.getInt("id"), rs.getString("path"));
				SegImages im = new SegImages(rs.getInt("id_exam"), rs.getString("path_exam"));
				ims.add(im);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ims;
	}
	
	
	
	
	public void closeConnection()
	{
		try {
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		DataBaseManager mana = new DataBaseManager();
		
		mana.addSegmentedImagesToDatabase("/home/pedro/imgs/seg0");
		mana.addSegmentedImagesToDatabase("/home/pedro/imgs/seg1");
		LinkedList<SegImages> l = mana.getDBSegImages();
		
		while(!l.isEmpty())
		{
			SegImages i = l.pop();
			
			System.out.println(i.getId());
			System.out.println(i.getPath());
			
		}
	}
	

}
