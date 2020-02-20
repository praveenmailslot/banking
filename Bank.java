

/*
 * Title 					    : Banking Management System
 * Author 				        : Praveen R
 * Start Date and Time 		    :18-Dec-2019  2 pm
 * Last Modified Date and Time 	: 2-Jan-2020 9 pm
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
/*These class contains all the bank details like account details,passwords privately.If any user attempts to login it will validate here weather the Account number is correct or not*/
public class Bank {
	
	public static BankingManagenmentSystem bankingmanagenmentsystem=new BankingManagenmentSystem();
	public static Bank bank=new Bank();
	public static long accountNumber;
	public String holderName;
	ResultSet resultSet;
	Statement statement; 
	Connection connection;
	PreparedStatement preparedStatement;
public  Bank() {

		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/BankingManagment", "root", "root");
			 statement =connection.createStatement();
			}catch(Exception exception)
			{
				System.out.println(exception);
			}
		
	}
	
	 /* Getter method for private variables. validating an account number and password from the bank*/
	
	
	public boolean validateAccount(String accountNumber, String password) 
	{ 
		try {
			String query="select * from AccountHolders WHERE accountNo ='"+accountNumber+"' and password='"+password+"'";
			resultSet=statement.executeQuery(query);
			if(resultSet.next())
			{
				this.accountNumber=Long.valueOf(resultSet.getString(2));
				holderName=resultSet.getString(5);
				System.out.println("Account Holder :"+resultSet.getString(5)+"\nAccount Type :"+resultSet.getString(6)+"\n");
				bankingmanagenmentsystem.setbalance(resultSet.getDouble(4));
				return true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
			return false;	
	}
	
	public void updateBalance(String query)
	{
		try {
			preparedStatement=connection.prepareStatement(query);
		    preparedStatement.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		
	}
	
	public void transactionlist(String query) {
		try {
			resultSet=statement.executeQuery(query);
			while(resultSet.next())
			{
				System.out.println("Account Number : "+resultSet.getLong(1)+"   Transaction type : "+resultSet.getString(3)+"   Transaction Amount : "+resultSet.getInt(4)+"   Transaction Date : "+resultSet.getDate(5)+"   Current Balance : "+resultSet.getDouble(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public  void passBookEntry(String mode,int amount,double balance)
	{
	
		long millis=System.currentTimeMillis();  
		java.sql.Date date=new java.sql.Date(millis);  
		System.out.println(date);  	
		try {
			String query="insert into booktransactions values(?,?,?,?,?,?)";
			preparedStatement=connection.prepareStatement(query);
			preparedStatement.setLong(1,accountNumber);
			preparedStatement.setString(2,holderName);
			preparedStatement.setString(3,mode);
			preparedStatement.setInt(4,amount);
			preparedStatement.setDate(5, date);
			preparedStatement.setDouble(6, balance);
			
			preparedStatement.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}	
}

