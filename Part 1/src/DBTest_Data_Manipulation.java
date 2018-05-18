import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class DBTest_Data_Manipulation {

	public static String[][] Return_Table(String G, Connection con) throws SQLException {

		/*
		 * Column zero contains the query set ID.
		 */
		
		String[][] Data_Table = new String[250][6];
		for (int i = 1; i <= 50; i++) {
			Data_Table[i * 5 - 1][0] =
			Data_Table[i * 5 - 2][0] =
			Data_Table[i * 5 - 3][0] =
			Data_Table[i * 5 - 4][0] =
			Data_Table[i * 5 - 5][0] = Integer.toString(i);
		}

		/*
		 * Column 1 contains the size of query set i.
		 */

		for (int i = 1; i <= 50; i++) {
			String qr1 = "SELECT COUNT(QUERY.SETID) FROM QUERY WHERE QUERY.SETID = %d";
			Statement st1 = con.createStatement();
			ResultSet rs1 = st1.executeQuery(String.format(qr1, i));
			rs1.next();
			int val = new Integer(((java.math.BigDecimal) rs1.getObject(1)).intValue());
			Data_Table[i * 5 - 1][1] =
			Data_Table[i * 5 - 2][1] =
			Data_Table[i * 5 - 3][1] =
			Data_Table[i * 5 - 4][1] =
			Data_Table[i * 5 - 5][1] = Integer.toString(val);
		}

		/*
		 * Column 2 contains the gene set ID. Column 4 contains the size of the
		 * query set / gene set pairing. Column 5 contains the time taken to
		 * calculate the inner join of the query and gene sets for a given query
		 * set i.
		 */

		for (int i = 1; i <= 50; i++) {
			String qr2 = "SELECT QUERY.SETID, %s.SETID, COUNT(QUERY.SETID) FROM QUERY, %s WHERE QUERY.GENEID = %s.GENEID AND QUERY.SETID = %d GROUP BY QUERY.SETID, %s.SETID ORDER BY COUNT(QUERY.SETID) DESC";
			Statement st2 = con.createStatement();
			double b1 = System.currentTimeMillis();
			ResultSet rs2 = st2.executeQuery(String.format(qr2, G, G, G, i, G));
			double b2 = System.currentTimeMillis();
			int counter1 = -5;
			while (rs2.next() && counter1 < 0) {
				int val1 = new Integer(((java.math.BigDecimal) rs2.getObject(2)).intValue());
				int val2 = new Integer(((java.math.BigDecimal) rs2.getObject(3)).intValue());
				Data_Table[i * 5 + counter1][2] = Integer.toString(val1);
				Data_Table[i * 5 + counter1][4] = Integer.toString(val2);
				counter1++;
			}
			Data_Table[i * 5 - 1][5] =
			Data_Table[i * 5 - 2][5] =
			Data_Table[i * 5 - 3][5] =
			Data_Table[i * 5 - 4][5] =
			Data_Table[i * 5 - 5][5] = Double.toString(b2 - b1);
		}

		/*
		 * Column 3 contains the size of the specified gene set.
		 */

		String qr1 = "SELECT %s.SETID, COUNT(%s.SETID) FROM %s GROUP BY %s.SETID";
		Statement st3 = con.createStatement();
		ResultSet rs3 = st3.executeQuery(String.format(qr1, G, G, G, G));
		Hashtable<Integer, Integer> HT = new Hashtable<Integer, Integer>();
		while (rs3.next()) {
			int val1 = new Integer(((java.math.BigDecimal) rs3.getObject(1)).intValue());
			int val2 = new Integer(((java.math.BigDecimal) rs3.getObject(2)).intValue());
			HT.put(val1, val2);
		}

		System.out.println("Table for " + G.toUpperCase());
		System.out.print("Query Set ID\tQ Size\t\tGene Set ID\tG Size\t\tO CNT\t\tTime\n");

		for (int i = 0; i < 250; i++) {
			Data_Table[i][3] = Integer.toString(HT.get(Integer.parseInt(Data_Table[i][2])));
			System.out.println(
					Data_Table[i][0] + "\t\t" +
					Data_Table[i][1] + "\t\t" +
					Data_Table[i][2] + "\t\t" +
					Data_Table[i][3] + "\t\t" +
					Data_Table[i][4] + "\t\t" +
					Data_Table[i][5]);
		}

		return Data_Table;
	}

}
