/*
 * Template JAVA User Interface
 * =============================
 * Skeleton Code Provided by Professor Bakalov\
 * Implementations done by Henry Ho SID: 862099626
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class MechanicShop{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public MechanicShop(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException { 
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 * 
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>(); 
		while (rs.next()){
			List<String> record = new ArrayList<String>(); 
			for (int i=1; i<=numCol; ++i) 
				record.add(rs.getString (i)); 
			result.add(record); 
		}//end while 
		stmt.close (); 
		return result; 
	}//end executeQueryAndReturnResult
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current 
	 * value of sequence used for autogenerated keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	
	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();
		
		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + MechanicShop.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		MechanicShop esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new MechanicShop (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. AddCustomer");
				System.out.println("2. AddMechanic");
				System.out.println("3. AddCar");
				System.out.println("4. InsertServiceRequest");
				System.out.println("5. CloseServiceRequest");
				System.out.println("6. ListCustomersWithBillLessThan100");
				System.out.println("7. ListCustomersWithMoreThan20Cars");
				System.out.println("8. ListCarsBefore1995With50000Milles");
				System.out.println("9. ListKCarsWithTheMostServices");
				System.out.println("10. ListCustomersInDescendingOrderOfTheirTotalBill");
				System.out.println("11. < EXIT");
				
				/*
				 * FOLLOW THE SPECIFICATION IN THE PROJECT DESCRIPTION
				 */
				switch (readChoice()){
					case 1: AddCustomer(esql); break;
					case 2: AddMechanic(esql); break;
					case 3: AddCar(esql); break;
					case 4: InsertServiceRequest(esql); break;
					case 5: CloseServiceRequest(esql); break;
					case 6: ListCustomersWithBillLessThan100(esql); break;
					case 7: ListCustomersWithMoreThan20Cars(esql); break;
					case 8: ListCarsBefore1995With50000Milles(esql); break;
					case 9: ListKCarsWithTheMostServices(esql); break;
					case 10: ListCustomersInDescendingOrderOfTheirTotalBill(esql); break;
					case 11: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice


	/*	
	--cid is big int so every customer can have a unique id
	--address is unlimited length (text) in case they have a very complex address
	--fname, lname is max length 50 as no one has such a long name
	--phone number will be recorded numbers only ex: 1234567890 big int as int is not large enough
	*/ 
	public static void AddCustomer(MechanicShop esql){//1
		try{
			
			
			String query = "INSERT INTO Customer  (id, fname, lname, phone, address) VALUES (";
			System.out.println("Enter the customer ID:");
			String id = in.readLine();
			while(id.isEmpty()) {
				System.out.println("ID cannot be blank.\n Enter customer ID:");
				id = in.readLine();
			}
			System.out.println("Enter Customers first name:");
			String fname = in.readLine();
			while(fname.isEmpty()) {
				System.out.println("First name cannot be blank.\n Enter Customers first name:");
				fname = in.readLine();
			}
			System.out.println("Enter Customers last name:");
			String lname = in.readLine();
			while(lname.isEmpty()) {
				System.out.println("Last name cannot be blank.\n Enter Customers last name:");
				lname = in.readLine();
			}	
			System.out.println("Enter Customers Phone:");
			String phone = in.readLine();
			while(phone.isEmpty()) {
				System.out.println("Phone number cannot be blank.\n Enter Customers phone number:");
				phone = in.readLine();
			}
			System.out.println("Enter Customers Address:");
			String address = in.readLine();
			while(address.isEmpty()) {
				System.out.println("Address cannot be blank.\n Enter customers address:");
				address = in.readLine();
			}
			query += id + ", '" +  fname + "', '" + lname + "', '" + phone + "', '" + address +  "');";
			System.out.println(query);
			esql.executeUpdate(query);			
		}
		catch(Exception e){
			System.err.println (e.getMessage());
		 }
	}
	
	public static void AddMechanic(MechanicShop esql){//2
		/*--fname, lname is max length 50 as no ne has such a long name
		--exp_year can only be double digits at most, we assume that all mechanics can do the same work, no specialization for now
		will display mechanic first and last name. */
		try{
			
			String query = "INSERT INTO Mechanic (id, fname, lname, experience) VALUES (";
			System.out.println("Enter the employee ID:");
			String empID = in.readLine();
			while(empID.isEmpty()) {
				System.out.println("Employee ID cannot be blank.\n Enter employee ID:");
				empID = in.readLine();
			}
			System.out.println("Enter the Mechanics first name:");
			String fname = in.readLine();
			while(fname.isEmpty()) {
				System.out.println("First name cannot be blank.\n Enter first name:");
				fname = in.readLine();
			}
			System.out.println("Enter the Mechanics last name:");
			String lname = in.readLine();
			while(lname.isEmpty()) {
				System.out.println("Last name cannot be blank.\n Enter last name:");
				lname = in.readLine();
			}
			System.out.println("Enter the experience of the mechanic in years");
			String exp = in.readLine();
			while(exp.isEmpty()) {
				System.out.println("Experience cannot be blank.\n Enter experience:");
				exp = in.readLine();
			}
			
			query += empID + ", '" + fname + "', '" + lname + "', " + exp + ");";
			System.out.println(query);
			esql.executeUpdate(query);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	/*--vin is only 17 characters
	--year is only 4 characters long
	--model and make should never be longer than 20 characters in any car ever in production
	--car must have an owner
	--the own relation is here too as it is M:1 
	*/
	public static void AddCar(MechanicShop esql){//3
		
		try {
			String query = "INSERT INTO Car VALUES (";
			System.out.println("Enter the car VIN:");
			String vin = in.readLine();
			while(vin.isEmpty()) {
				System.out.println("VIN cannot be empty.\n Enter VIN:");
				vin = in.readLine();
			}
			System.out.println("Enter the car Make:");
			String make = in.readLine();
			while(make.isEmpty()) {
				System.out.println("Make cannot be empty.\n Enter Make:");
				make = in.readLine();
			}
			System.out.println("Enter the Car Model:");
			String model = in.readLine();
			while(model.isEmpty()) {
				System.out.println("Model cannot be empty.\n Enter Model:");
				model = in.readLine();
			}
			System.out.println("Enter the car Year. Must be greater than 1970:");
			int year = 0;
			year = Integer.parseInt(in.readLine());
			while(year == 0) {
				System.out.println("Year cannot be empty.\n Enter Year:");
				year = Integer.parseInt(in.readLine());
			}
			while (year < 1880) {
				System.out.println("Year must be greater than 1880, as no cars were made before that");
				year = Integer.parseInt(in.readLine());
			}
			query += "'" + vin + "', '" + make + "', '" + model + "', " + year + ");";
			System.out.println(query);
			esql.executeUpdate(query);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}		
	}
	
	public static void AddCarService(MechanicShop esql, String vin) {//Bug with add car in Service_Request temporary fix 8/23/21
		try {
			String make, model;
			int year;
			System.out.println("Enter the car Make:");
			make = in.readLine();
			while(make.isEmpty()) {
				System.out.println("Make cannot be empty.\n Enter Make:");
				make = in.readLine();
			}
			System.out.println("Enter the car Model:");
			model = in.readLine();
			while(model.isEmpty()) {
				System.out.println("Model cannot be empty.\n Enter Model:");
				model = in.readLine();
			}
			System.out.println("Enter the car Year:");
			year = Integer.parseInt(in.readLine());
			while(year == 0) {
				System.out.println("Year cannot be empty.\n Enter Year:");
				year = Integer.parseInt(in.readLine());
			}
			while (year < 1880) {
				System.out.println("Year must be greater than 1880, as no cars were made before that");
				year = Integer.parseInt(in.readLine());
			}
			String query = "INSERT INTO Car VALUES('" + vin + "', '" + make + "', '" + model + "', '" + year + "')";
			System.out.println(query);
			esql.executeUpdate(query);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
			
	
public static void InsertServiceRequest(MechanicShop esql){//4 
                String vin, date, complaint;
                vin = date = complaint = null;
		int rid, cid, odo;
		rid = cid = odo = 0;
                String query;
                List<List<String>> result = new ArrayList<List<String>>();
                try {
                        System.out.println("Insert Service Request");
                        System.out.println("Enter customers last name: ");
                        String lname = in.readLine();
						while (lname.isEmpty()) {
							System.out.println("Last name cannot be empty");
							lname = in.readLine();	
						}
                        while (true) {
                                //pulls up all customers with this last name
                                query = "SELECT lname, id, fname FROM Customer WHERE lname = '" + lname + "' GROUP BY id";
                                result = esql.executeQueryAndReturnResult(query);
                                if(result.size() == 0) { //Customer is not found so create new customer
                                	System.out.println("Customer not found. Creating new customer");
                                        AddCustomer(esql);
                                        return;
                                }
                                else if (result.size() == 1) { //Only one customer with this last name exists, so we are done finding them 
					cid = Integer.parseInt(result.get(0).get(1));
					break;
				}
				else {				
					System.out.println("List of all customers with this last name:");
					for (int i = 0; i < result.size(); ++i) {
						System.out.println(i + ". " + result.get(i).get(2).trim() + " " + result.get(i).get(0).trim());
					}
					System.out.println("Select the correct customer by inputting the number next to their name: ");
					String input = in.readLine();
					while(input.isEmpty()) {
						System.out.println("Input cannot be empty.\n Enter Input:");
						input = in.readLine();
					}
					int number = Integer.parseInt(input);
					cid = Integer.parseInt(result.get(number).get(1));
					break;
				}
			}
			while (true) {
				query = "SELECT c.vin, c.make, c.model, c.year FROM Car c, Owns o WHERE c.vin = o.car_vin AND o.customer_id = " + cid;
				result = esql.executeQueryAndReturnResult(query);
				if (result.size() == 0) { //customer has no cars so add a car
					System.out.println("No cars found for this customer. Adding a new car");
					System.out.println("Enter Car Vin");
				    vin = in.readLine();
					while(vin.isEmpty()) {
						System.out.println("VIN cannot be empty.\n Enter VIN:");
						vin = in.readLine();
					}
					AddCarService(esql,vin);
					query = "SELECT COUNT(ownership_id) FROM owns";
					result = esql.executeQueryAndReturnResult(query);
					int oid = Integer.parseInt(result.get(0).get(0));
					System.out.println(oid);
					query = "INSERT INTO owns VALUES(" + oid + "," + cid + ",'" + vin + "')";
					esql.executeUpdate(query);
					break;
				}
				else {
					System.out.println("Listing cars");
					for (int i = 0; i < result.size(); i++) {
						System.out.println("Car No. " + i + " " + result.get(i).get(0) + " " + result.get(i).get(1) + " " + result.get(i).get(2));
					}
					System.out.println("Select the car for service: ");
					int car = Integer.parseInt(in.readLine());
					vin = result.get(car).get(0);
					break;
				}
			}
			String car_vin = vin;//vin gets assigned to car_vin
			query = "SELECT COUNT(rid) FROM Service_Request";
			result = esql.executeQueryAndReturnResult(query);
			rid = Integer.parseInt(result.get(0).get(0));
			System.out.println("Enter Service date: (yyyy-mm-dd)");
			date = in.readLine();
			while(date.isEmpty()) {
				System.out.println("Date cannot be empty.\n Enter Date:");
				date = in.readLine();
			}
			System.out.println("Enter Odometer: ");
			odo = Integer.parseInt(in.readLine());
			while(odo == 0) {
				System.out.println("odomoter cannot be empty.\n Enter Odomoter:");
				odo = Integer.parseInt(in.readLine());
			}
			System.out.println("Enter Complaint: ");
			complaint = in.readLine();
			while(complain.isEmpty()) {
				System.out.println("Complaint cannot be empty.\n Enter Complaint:");
				complaint = in.readLine();
			}
			query = "INSERT INTO Service_Request VALUES(" + rid + ", " + cid +", '" + vin + "', '" + date + "', " + odo + ", '" + complaint + "')";
			System.out.println(query);
			esql.executeUpdate(query);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	
	}

	
	public static void CloseServiceRequest(MechanicShop esql) throws Exception{//5
		String date, comment;
		int rid, mid, bill; //, wid;
		rid = mid = bill = 0;
		String query;
		List<List<String>> result =  new ArrayList<List<String>>();
		try {
			System.out.println("Close Service Request");
			while (true) {
				System.out.println("Enter Service request ID:");
				rid = Integer.parseInt(in.readLine());
				while (rid == 0) {
					System.out.println("Enter Service request ID:");
					rid = Integer.parseInt(in.readLine());
				}
				query = "SELECT s.rid FROM Service_Request s WHERE s.rid = " + rid; //comapres Service req rid to close req rid
				result = esql.executeQueryAndReturnResult(query);
				if (result.size() > 0) {
					break;
				}
				System.out.println("ID not found");
			}
			while (true) {
				System.out.println("Enter Mechanic ID:");
				mid = Integer.parseInt(in.readLine());
				while (mid == 0) {
					System.out.println("Enter Mechanic ID:");
					mid = Integer.parseInt(in.readLine());
				}
				query = "SELECT id FROM Mechanic WHERE id = " + mid;
				result = esql.executeQueryAndReturnResult(query);
				if(result.size() > 0) {
					break;
				}
				System.out.println("ID not found");			
			}
			query = "SELECT wid FROM Closed_Request WHERE rid = " + rid;
			result = esql.executeQueryAndReturnResult(query);
			//wid = Integer.parseInt(result.get(0).get(0));
			System.out.println("Enter Close Date (yyyy-mm-dd)");
			date = in.readLine();
			while(date.isEmpty()) {
				System.out.println("Date cannot be empty.\n Enter Date:");
				date = in.readLine();
			}
			System.out.println("Enter Comment");
			comment = in.readLine();
			while(comment.isEmpty()) {
				System.out.println("Comment cannot be empty.\n Enter Comment:");
				comment = in.readLine();
			}
			System.out.println("Enter Bill:");
			bill = Integer.parseInt(in.readLine());
			while (bill == 0) {
				System.out.println("Bill cannot be empty.\n Enter Bill:");
				bill = Integer.parseInt(in.readLine());
			}
			query = "INSERT INTO Closed_Request VALUES (" + rid + ", " + rid + ", " + mid + ", '" + date + "', '" + comment + "', " + bill + ")";
			System.out.println(query);
			esql.executeUpdate(query);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		} 	
	}
	
	public static void ListCustomersWithBillLessThan100(MechanicShop esql){//6
		try {
			String query = "SELECT c.fname, c.lname, r.date, r.comment, r.bill FROM Customer c, Closed_Request r, Service_Request s  WHERE s.customer_id = c.id AND r.rid = s.rid AND r.bill < 100";
			List<List<String>> result = esql.executeQueryAndReturnResult(query);
			System.out.printf("%-8s%-8s%-8s%-8s%-15s\n", "First", "Last", "Date", "Comment", "Bill\n");
			for (int i = 0; i < result.size(); ++i) {
				System.out.printf("%-8s%-8s%-8s%-8s%-15s\n", result.get(i).get(0).trim(), result.get(i).get(1).trim(), result.get(i).get(2).trim(), result.get(i).get(3).trim(), result.get(i).get(4).trim());
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}
	
	public static void ListCustomersWithMoreThan20Cars(MechanicShop esql){//7
		try {
			String query = "SELECT c.fname, c.lname FROM Customer c WHERE (SELECT COUNT(c1.vin) FROM Owns o, Car c1 WHERE c.id = o.customer_id AND o.car_vin = c1.vin) > 20";
			List<List<String>> result = esql.executeQueryAndReturnResult(query);
			System.out.printf("%-8s%-8s\n", "First", "Last\n");
			for (int i = 0; i < result.size(); ++i) {
				System.out.printf("%-8s%-8s\n", result.get(i).get(0).trim(), result.get(i).get(1).trim());
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void ListCarsBefore1995With50000Milles(MechanicShop esql){//8
		try {
			String query = "SELECT DISTINCT c.make, c.model, c.year FROM Car c, Service_Request s WHERE c.vin = s.car_vin AND c.year < 1995 AND s.odometer < 50000";
			List<List<String>> result = esql.executeQueryAndReturnResult(query);
			System.out.printf("%-8s%-8s%-8s\n", "Make", "Model", "Year\n"); 	
			for (int i = 0; i < result.size(); ++i) {
				System.out.printf("%-8s%-8s%-8s\n", result.get(i).get(0), result.get(i).get(1), result.get(i).get(2));
			}
		}
		catch (Exception e) {
			System.err.println (e.getMessage());
		}
	}
	
	public static void ListKCarsWithTheMostServices(MechanicShop esql){//9
		//
		try {
			System.out.print("Enter the max Service Request");
			int inp = Integer.parseInt(in.readLine());
			String query = "SELECT c.make, c.model, COUNT(s.rid) count FROM Car c, Service_Request s WHERE c.vin = s.car_vin GROUP BY c.make, c.model ORDER BY count DESC LIMIT " + inp + " ";
			List<List<String>> result = esql.executeQueryAndReturnResult(query);
			System.out.printf("%-8s%-8s%-8s\n", "Make", "Model", "Request\n");
			for (int i = 0; i < result.size(); ++i) {
				System.out.printf("%-8s%-8s%-8s\n", result.get(i).get(0), result.get(i).get(1), result.get(i).get(2));
			}
			
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}			
	}
	
	public static void ListCustomersInDescendingOrderOfTheirTotalBill(MechanicShop esql){//9
		//
		try {
			String query = "SELECT c.fname, c.lname, SUM(r.bill) bill FROM Customer c, Owns o, Car C1, Closed_Request r, Service_Request s WHERE c.id = o.customer_id AND o.car_vin = c1.vin AND c1.vin = s.car_vin AND r.wid = s.rid GROUP BY c.fname, c.lname ORDER BY bill DESC ";
			List<List<String>> result = esql.executeQueryAndReturnResult(query);
			System.out.printf("%-8s%-8s%-8s\n", "First", "Last", "Bill\n");
			for (int i = 0; i < result.size(); ++i) {
				System.out.printf("%-8s%-8s%-8s\n", result.get(i).get(0).trim(), result.get(i).get(1).trim(), result.get(i).get(2).trim());
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		} 
	}
	
}
