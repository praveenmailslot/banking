

/*
 * Title 					    : Banking Management System
 * Author 				        : Praveen R
 * Start Date and Time 		    :18-Dec-2019  2 pm
 * Last Modified Date and Time 	: 2-Jan-2020 9 pm
 */
 


 
import java.io.Console;
import java.sql.*;
import java.sql.DriverManager;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*Banking Management System will Get the Account Number and Password from an user and Validates if the account number is Exist in the bank or not  .After that it Validates the Account Number and Password from an Bank.If the Account Number and Password is then Account will Login After that we can perform our operations  Otherwise the Machine will logout   */

public class BankingManagenmentSystem {
	
	public static Scanner scanner=new Scanner(System.in);
	public static Bank bank=new Bank(); 
	public static double balance;
	public static int atempt=1; 
	static String accountNumber,password,query;
	
	/* It Will Execute Initially Before Execution of an Main method. It will Welcome the User Initially  */
	static
	{
		
		System.out.println("Welcome.....\n");
	}
	
	
	
	
	
	/*Getting an Account Number from user and Validate Account Number consist of  12 digits or not.It Allows the user up to 3 wrong Attempts to user if it exceeds Account will Automatically  logout */
	public static void getAccountNumber()
	{ 
		if(atempt==1)
		{
		System.out.println("Please Enter Your Account Number\n");
		}
		accountNumber=scanner.nextLine();
		atempt++;
		if(validateAccountNumber()==false)
		{	
			if(atempt<4)
			{
				System.out.println("Account Number Consist of  numbers Only \n Please Enter Correct Account Number\n");
				getAccountNumber();
			}
			else
			{
				System.out.println("You Atempted more number of times..  \n THANK YOU\n");
				System.exit(0);
			}
		}
		
	}
	
	
	/* Validating an Account Number Consist of 12 digits or not. If it Contains Exact format it will return true otherwise returns false*/
	public static boolean validateAccountNumber()
	{
		if((accountNumber).matches("\\d{12}"))
		{
			return true;
		}
		else
			return false;
	}
	
	/*Getting an password from user and checks exactly of 4 digits or not. It allows the user to 2 wrong attempts to user if it exceeds atm will logout*/
	public static void getPassword()
	{
		if(atempt==1)
		{
		System.out.println("Enter your Password");
		}
		password=scanner.nextLine();
		atempt++;
		if(validatePassword()==false)
		{
			if(atempt<3)
			{
				System.out.println("Invalid Password \n Please Enter Correct Password");
				getPassword();
			}
			else
			{
				System.out.println("You Attempted More Number of Times So Account has Locked ..\n Please Visit Your Branch Manager  \n THANK YOU");
				System.exit(0);
			}
						
		}
	}
	/* Validating an Password Consist of 4 digits or not. If it Contains Exact format it will return true otherwise returns false*/
	public static boolean validatePassword()
	{
		if((password).matches("\\d{4}"))
		{
				return true;
		}
		else
		{
			return false;
		}
	}
	
	/* If the Account Number and Password is Correct the Bank will Assign a Balance to this System*/
	public  void setbalance(double setBalance)
	{
		balance=setBalance;
	}
	
	/* It Will Display an list of options we can select any option for your Banking operation*/
	public static void displayOptions()
	{ 
		System.out.println(" Please Select Your Choice..\n 1.Check Balance \n 2.Amount Deposit \n 3.Amount Withdraw \n 4.All Transactions \n 5.Log out \n");
		int choice=scanner.nextInt();
		optionSelection(choice);
	}
	
	/* Based on user Option the Control will Transfer to that Function */
	public static void optionSelection(int option)
	{
		switch(option)
		{
		case 1: 
			displayBalance();
			break;
		case 2:
			atempt=1;
			deposit();
			break;
		case 3:
			atempt=1;
			withdraw();
			break;
		case 4:
			allTransactions();
			break;
		case 5:
			System.out.println("THANK YOU...");
			System.exit(0);
			break;
		default :
			System.out.println("You Entered a Wrong Option..\nSelect Valid Option");
			displayOptions();
			break;
			
		}
	}
	
	
	public static void allTransactions()
	{
		String query="select * from booktransactions where accountNo='"+accountNumber+"'";
		bank.transactionlist(query);
		control();
	}
	
	/* It will Display the Current Balance of user */
	public static void displayBalance()
	{

		System.out.println("Your Balance Is :"+balance+"\n");
		atempt=0;
		control();
	}
	
	/* We can Deposit the Amount Here It will Calculate the new Balance Based on the Deposit Amount If we Enter an invalid input it will handle it  Amount Deposit is limited at once the login*/
	public static void deposit()
	{
		atempt++;
		if(atempt==5)
		{
			System.out.println("You Attempted more number of times...\n THANK YOU ");
			System.exit(0);
		}
		int deposit=0;
		try {
		System.out.println("Enter the amount to be deposited");
		 deposit=scanner.nextInt();
		}
	    catch(InputMismatchException inputmismatchexception)
		{
	    	System.out.println("Enter Valid input");
	    	scanner.next();
	    	deposit();
		}
		if(deposit<100)
		{
			System.out.println("The Minimum Deposit Amount is 100.");
			deposit();
		}
		else if(deposit<=100000)
		{
			balance+=deposit;
			query="UPDATE AccountHolders SET Balance="+balance+"WHERE AccountNo='"+accountNumber+"'";
			bank.updateBalance(query);
			bank.passBookEntry("CREDIT", deposit,balance);
			System.out.println("Amount Deposited Successfully\n");
			atempt=0;
			control();
		}
		else
		{
			System.out.println("The Maximum Amount to Deposit is 100,000 at once");
			deposit();
		}
	}
	
	/* Amount Withdraw will Happens here.It Checks the Minimum Balance of the Account. if Balance is less than Minimum Balance then Withdraw will not Happen  */
	public static void withdraw()
	{
		atempt++;
		if(atempt==5)
		{
			System.out.println("You Attempted more number of times...\n THANK YOU ");
			System.exit(0);
		}

		int withdraw=0;
		if(balance==500)
		{
			System.out.println("You Have Minimum Balance So You Can't Withdraw Money");
			atempt=0;
			control();
		}
		else if(balance<500)
		{
			System.out.println("You Have an Less than Minimum Balance ");
			atempt=0;
			control();
		}
		else
		{
			try {
			System.out.println("Enter the Amount to Withdraw");
			 withdraw=scanner.nextInt();
			 if(withdraw<100)
			 {
				 System.out.println("Minimum Withdrawable Amount is 100 ");
				 withdraw();
			 }
			 
			}
			 catch(InputMismatchException inputmismatchexception)
			{
				 System.out.println("Enter Valid input");
				 scanner.next();
				 withdraw();
			}
			 if(withdraw<=20000)
			 {
				 if(balance-withdraw<500)
				 {
					 System.out.println("The Minimum Balance is 500 Try with Less than this Amount");
					 withdraw();
				 }
				 else
				 {
					 balance-=withdraw;
					 query="UPDATE AccountHolders SET Balance="+balance+"where AccountNo="+accountNumber;
					 bank.updateBalance(query);
					 bank.passBookEntry("DEBIT",withdraw,balance);
					 System.out.println("Please Collect Your Cash\n");
					 atempt=0;
						control();
				}
		}
			 else
			 {
				 System.out.println("The Maximum Amount to Withdraw is 20,000 at once");
				 withdraw();
			 }
		}
	}
	
	/* It will check the status of an account validation from an bank .if the Account number and is correct then it will display an list of options .if the Account number is incorrect then the control will terminate*/
	public static void statusCheck(boolean status)
	{
		if(status==true)
		{
			
			displayOptions();
		}
		else
		{
			System.out.println("Your Account Number and Password is Incorrect\n");
			System.out.println("THANK YOU...");
		}
	}
	
 
	public static void control()
	{
		System.out.println("Do You Want to Continue  \n Press 1 to Continue ");
		int control=scanner.nextInt();
		switch(control)
		{
		case 1:
			for(int space=0;space<20;space++)
			{
				System.out.println();
			}
			displayOptions();
			break;
		default :
			System.out.println("             THANK YOU");
			System.exit(0);
			break;
		}
	}
	public static void main(String[] args) 
	{
		getAccountNumber();										
		atempt=1;
		getPassword();
		statusCheck(bank.validateAccount(accountNumber,password));
		System.exit(0);
	
	}

}


