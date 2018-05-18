import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Alif Albiruni
 * CSE 4701
 * April 4, 2018
 * 
 * Project 2 Part 1
 * 
 * The following program establishes a connection with a server for cse4701.
 * It gathers the necessary information by retrieving the necessary queries and
 * then performing data manipulation from the provided tables.
 */

public class DBTest_Development {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection con;
		
		String user = "cse4701";
		String password = "intersect";
		String host = "query.engr.uconn.edu";
		String port = "1521";
		String sid = "BIBCI";
		String url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
		
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			con = DriverManager.getConnection(url, user, password);
			
			/*
			 * The following class computes the necessary statistics.
			 */
			
			DBTest_Data_Manipulation DBTM = new DBTest_Data_Manipulation();
			DBTM.Return_Table("G1", con);
			DBTM.Return_Table("G2", con);
			
		} catch (SQLException e) {
			Logger.getLogger(DBTest_Development.class.getName()).log(Level.SEVERE, null, e);
		}
	}

}
