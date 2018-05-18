import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBTest_Development {
	
	/*
	 * Alif Albiruni
	 * Dong Shin
	 * CSE 4701
	 * April 21, 2018
	 * 
	 * In this stage of Project 2, the Information Gain and Overlap between each of the mutations and the status
	 * is calculated by looping through three queries.
	 */

	static String[] attributes = { "APC", "TP53", "KRAS", "PIK3CA", "PTEN", "ATM", "MUC4", "SMAD4", "SYNE1", "FBXW7" };

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
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
			 * The PTC_RC class stores a data table of doubles containing
			 * the values for the conditional distribution tables A, B, C, D,
			 * expected info, info needed, and info gained in that order
			 * along the columns. In other words, the structure of the returned
			 * double array from PTC_RC is such that each gene corresponds to a row,
			 * and each column index corresponds to the following values:
			 * 
			 * 0 = A
			 * 1 = B
			 * 2 = C
			 * 3 = D
			 * 
			 * 4 = Info Expected
			 * 5 = Info Needed
			 * 6 = Info Gained
			 * 
			 */

			DBTest_Data_Manipulation DBTM = new DBTest_Data_Manipulation();
			
			/*
			 * After performing all of the computations, we retrieve the table.
			 * Using a boolean array, we print out the top five genes by Information Gained.
			 * We also retrieve the overlap, corresponding to A in the table.
			 * 
			 * The boolean array double checks which values have been printed already.
			 * At every outer loop iteration, we print the next largest value. Observe that
			 * the work of computation is delegated to the object DBTM of class
			 * DBTest_Data_Manipulation.
			 */
			
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM IG_READY");
			rs.next();
			//System.out.println(rs.getInt(1));
			
			PTC_RC output = DBTM.Return_Table(attributes, con);

			System.out.println("Part A\n");
			
			int rows = output.getRow();
			boolean[] hasBeenPicked = new boolean[rows];
			System.out.println("Gene\tInfo_Gain");
			for (int j = 5; j > 0; j--) {
				double max_val = -10000;
				int at_idx = 0;
				for (int i = 0; i < rows; i++) {
					if (output.getval(i, 6) > max_val && !hasBeenPicked[i]) {
						max_val = output.getval(i,  6);
						at_idx = i;
					}
				}
				hasBeenPicked[at_idx] = true;
				
				
				System.out.println(attributes[at_idx] + "\t" + output.getval(at_idx, 6));
			}
			
			System.out.println("\nPart B\n");
			System.out.println("Gene\tInfo_Gain\t\tOverlap");
			boolean[] hasBeenPicked2 = new boolean[rows];
			for (int j = 5; j > 0; j--) {
				double max_val = -10000;
				int at_idx = 0;
				for (int i = 0; i < rows; i++) {
					if (output.getval(i, 6) > max_val && !hasBeenPicked2[i]) {
						max_val = output.getval(i,  6);
						at_idx = i;
					}
				}
				hasBeenPicked2[at_idx] = true;
				
				
				System.out.println(attributes[at_idx] + "\t" + output.getval(at_idx, 6) + "\t" + (int)output.getval(at_idx, 0));
			}

		} catch (SQLException e) {
			Logger.getLogger(DBTest_Development.class.getName()).log(Level.SEVERE, null, e);
		}
	}

}
