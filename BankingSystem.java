package finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import java.time.*;

public class BankingSystem {
	static Connection conn;
	public static void connectDb() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String url = "jdbc:mysql://localhost:3306/BankingSystem";
			String username = "root";
			String password = "";

			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void disconnectDb() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static class MinimumBalanceException extends Exception{
		public MinimumBalanceException() {
			super("Amount should be 5000 or greater than 5000 ");
		}
	}
    public static void main(String[] args) {
    	Scanner s = new Scanner(System.in);
    	try {
			while(true) {
				System.out.println("Welcome to the Bank:");
				System.out.println("1. Open a Bank account");
				System.out.println("2. Perform transactions for an account");
				System.out.println("3. Exit the application");
				
				System.out.print("Enter Your Choice: ");
				int choice = s.nextInt();
				if(choice==3)
					break;
				else {
					String name;
					String surname;
					String mobileno;
					String DOB;
					long aadharcard;
					int pin;
					long amount = 0;
					connectDb();
					PreparedStatement ps = null;
					ResultSet rs = null;
					switch (choice) {
					case 1:	System.out.println("Enter your name: ");
					name = s.next();
					System.out.println("Enter your surname: ");
					surname = s.next();
					System.out.print("Enter your mobile no: ");
					mobileno = s.next();
					System.out.print("Enter your Date of Birth(yyyy-mm-dd): ");
					DOB=s.next();
					LocalDate dateOfBirth=LocalDate.parse(DOB);
					System.out.print("Enter your AadharCardno: ");
					aadharcard = s.nextLong();
					System.out.print("Enter your pin you want to generate: ");
					pin=s.nextInt();
					System.out.print("Enter the amount deposited:");
					amount = s.nextLong();
					try {
							if(amount<5000) {
								throw new MinimumBalanceException();
							}
							ps = conn.prepareStatement("insert into users(name,surname,mobile,DOB,aadharcardno,pin,amount) values(?,?,?,?,?,?,?)");
							ps.setString(1, name);
							ps.setString(2, surname);
							ps.setString(3, mobileno);
							ps.setString(4, DOB);
							ps.setLong(5, aadharcard);
							ps.setInt(6, pin);
							ps.setLong(7, amount);
					}
					catch(MinimumBalanceException e) {
							System.out.print(e.getMessage());
							System.out.print("\nEnter the amount deposited:");
							amount = s.nextLong();
							ps = conn.prepareStatement("insert into users(name,surname,mobile,DOB,aadharcardno,pin,amount) values(?,?,?,?,?,?,?)");
							ps.setString(1, name);
							ps.setString(2, surname);
							ps.setString(3, mobileno);
							ps.setString(4, DOB);
							ps.setLong(5, aadharcard);
							ps.setInt(6, pin);
							ps.setLong(7, amount);
					}
					int val = ps.executeUpdate();
					if (val>0)
						System.out.println("Account Created Sucessfully");
					else
						System.out.println("Not Inserted");
					
					break;
					case 2:	
						long deposit;
						long withdraw;
						long currbalance;
						while(true) {
						System.out.println("\nPerform transactions for an account:");
						System.out.println("1. Deposit");
						System.out.println("2. Withdraw");
						System.out.println("3. Change owner name");
						System.out.println("4. Display number of transactions and closing balance");
						System.out.println("5. Exit");
						System.out.print("Enter Your Choice: ");
						int choice1 = s.nextInt();
						if(choice1==5) {
							break;
						}
						else {
							
						  switch(choice1) {
						  case 1:
							  System.out.print("Enter the amount you want to deposit:");
							  deposit=s.nextLong();
							  System.out.print("Enter your AadharCardno: ");
							  aadharcard = s.nextLong();
							  System.out.print("Enter your pin: ");
							  pin=s.nextInt();
							  ps = conn.prepareStatement("select pin from users where aadharcardno=?");
							  ps.setLong(1, aadharcard);
							  rs = ps.executeQuery();
							  rs.next();
							  int pin1=rs.getInt(1);
							  
							  ps = conn.prepareStatement("select amount from users where aadharcardno=?");
							  ps.setLong(1, aadharcard);
							  rs = ps.executeQuery();
							  rs.next();
							  int amount1=rs.getInt(1);
							  
							  if(pin==pin1) {
								  withdraw=0;
								  currbalance=amount1+deposit;
								  
								  ps = conn.prepareStatement("update users set amount=? where aadharcardno=?");	
								  ps.setLong(1,currbalance);
								  ps.setLong(2,aadharcard);
								  ps.executeUpdate();
								  
								  ps = conn.prepareStatement("insert into balance(deposit,withdraw,currbalnce,pin) values(?,?,?,?)");
								  ps.setLong(1, deposit);
								  ps.setLong(2, withdraw);
								  ps.setLong(3, currbalance);
								  ps.setInt(4, pin);
								  int val1=ps.executeUpdate();
								  if (val1>0)
									  System.out.println("Deposited");
								  else
									  System.out.println("Not Deposited");
						      }
						      else{
						    	   System.out.print("You entered wrong pin");
						       }	
						  break;
						  case 2:
							  System.out.print("Enter the amount you want to withdraw:");
							  withdraw=s.nextLong();
							  System.out.print("Enter your AadharCardno: ");
							  aadharcard = s.nextLong();
							  System.out.print("Enter your pin: ");
							  pin=s.nextInt();
							  ps = conn.prepareStatement("select pin from users where aadharcardno=?");
							  ps.setLong(1, aadharcard);
							  rs = ps.executeQuery();
							  rs.next();
							  pin1=rs.getInt(1);
							  
							  ps = conn.prepareStatement("select amount from users where aadharcardno=?");
							  ps.setLong(1, aadharcard);
							  rs = ps.executeQuery();
							  rs.next();
							  amount1=rs.getInt(1);
							  
							  if(pin==pin1) {
								  
								  deposit=0;
								  currbalance=amount1-withdraw;
								  try {
									  if(currbalance<5000) {
										  throw new MinimumBalanceException();
									  }
								  
								  ps = conn.prepareStatement("update users set amount=? where aadharcardno=?");	
								  ps.setLong(1,currbalance);
								  ps.setLong(2,aadharcard);
								  ps.executeUpdate();
								  
								  ps = conn.prepareStatement("insert into balance(deposit,withdraw,currbalnce,pin) values(?,?,?,?)");
								  ps.setLong(1, deposit);
								  ps.setLong(2, withdraw);
								  ps.setLong(3, currbalance);
								  ps.setInt(4, pin);
								  
								  int val1=ps.executeUpdate();
								  if (val1>0)
									  System.out.println("Amount Deducted");
								  else
									  System.out.println("Failed to deduct amount");
								  }
								  catch(MinimumBalanceException e) {
									  System.out.print(e.getMessage());
							       }
								  
						      }
						      else{
						    	   System.out.print("You entered wrong pin");
						       }	
						  break;
						  case 3:
							  System.out.print("Enter your pin: ");
							  pin = s.nextInt();
							  System.out.print("Change your Name: ");
							  String name1 = s.next();
							  ps = conn.prepareStatement("update users set name=? where pin=?");
							  ps.setString(1, name1);
							  ps.setInt(2, pin);
							  val = ps.executeUpdate();
								if(val>0)
									System.out.println("Updated");
								else
									System.out.println("Not Updated");
		
							  break;
						  case 4:
							  	System.out.print("Enter your pin: ");
								pin = s.nextInt();
								ps = conn.prepareStatement("select * from balance where pin=?");
								ps.setInt(1, pin);
								rs = ps.executeQuery();
								int count=0;
								while(rs.next()) {
									System.out.println("=======================================================================");
									System.out.println("DepositBalance\t\t  WithdrawBalance\t\t CurrentBalance");
									System.out.println(rs.getLong(1)+ "\t\t\t\t"
											+ rs.getLong(2) +"\t\t\t\t" 
											+ rs.getLong(3)  
											);
									System.out.println("=======================================================================");
									count++;
								}
								System.out.println("Total number of Transactions: "+count);
							  break;
						  default:
							  System.out.print("Invalid choice");
							  break;
						  }
						}
						}
					default: System.out.println("Invalid Choice");
					break;
					}
					disconnectDb();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
    	
    }
}



function get(){

console.log("hello nidhi")
}
get()
