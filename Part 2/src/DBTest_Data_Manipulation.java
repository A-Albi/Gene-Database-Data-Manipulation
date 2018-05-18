import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest_Data_Manipulation {

	/*
	 * Alif Albiruni
	 * Dong Shin
	 * CSE 4701
	 * April 21, 2018
	 * 
	 * In this stage of Project 2, the Information Gain and Overlap between each of the mutations and the status
	 * is calculated by looping through three queries. We obtain an object representing the data, the number of rows,
	 * and the number of columns of type PTC_RC.
	 * 
	 * In the following method, the connection and the list of genes, called "attributes" is given.
	 */
	
	public static PTC_RC Return_Table(String[] attributes, Connection con) throws SQLException {

		Statement st1 = con.createStatement();
		double NumPatients = 0;

		// We first count the total number of patients to get the sense of the size of
		// our data table.

		String q1 = "SELECT COUNT(*) FROM IG_READY";

		ResultSet rs1 = st1.executeQuery(q1);
		rs1.next();
		NumPatients = rs1.getInt(1);

		// We compute the attributes for every gene.

		double[][] probabilities = new double[attributes.length][7];
		PTC_RC output = new PTC_RC(attributes.length, 7);

		for (int i = 0; i < attributes.length; i++) {
			// True Positive
			// False Negative
			// False Positive
			
			String QA = "SELECT COUNT(*) FROM (SELECT * FROM IG_READY WHERE %s = 1 MINUS SELECT * FROM IG_READY WHERE STATUS = 0)";
			String QB = "SELECT COUNT(*) FROM (SELECT * FROM IG_READY WHERE %s = 0 MINUS SELECT * FROM IG_READY WHERE STATUS = 0)";
			String QC = "SELECT COUNT(*) FROM (SELECT * FROM IG_READY WHERE %s = 1 MINUS SELECT * FROM IG_READY WHERE STATUS = 1)";

			// True Positive A uses QA
			ResultSet rs_a = st1.executeQuery(String.format(QA, attributes[i]));
			rs_a.next();
				probabilities[i][0] = rs_a.getInt(1);
			// False Negative B uses QB
			ResultSet rs_b = st1.executeQuery(String.format(QB, attributes[i]));
			rs_b.next();
				probabilities[i][1] = rs_b.getInt(1);
			// False Positive C uses QC
			ResultSet rs_c = st1.executeQuery(String.format(QC, attributes[i]));
			rs_c.next();
				probabilities[i][2] = rs_c.getInt(1);
			// True Negative D uses A, B, and C
			probabilities[i][3] = NumPatients - (probabilities[i][0] + probabilities[i][1] + probabilities[i][2]);
			
			double A = probabilities[i][0]; // True Positive
			double B = probabilities[i][1]; // False Negative
			double C = probabilities[i][2]; // False Positive
			double D = probabilities[i][3]; // True Negative
			
			// The following computations find expected information, information needed, and information gained in
			// that order.
			double STATUS_YES = A + B;
			double STATUS_NO = C + D;
			double MUTATION_YES = A + C;
			double MUTATION_NO = B + D;
			// Information Expected
			double info_d = -return_I(STATUS_YES, STATUS_NO);
			// Information Needed
			double info_a = (((MUTATION_YES) / (MUTATION_NO + MUTATION_YES)) * return_I(A, C)) + (((MUTATION_NO) / (MUTATION_YES + MUTATION_NO)) * return_I(B, D));
			// Information Gained
			double info_gain = info_d + info_a;
			probabilities[i][4] = info_d;
			probabilities[i][5] = info_a;
			probabilities[i][6] = info_gain;
		}

		output.setProbabilities(probabilities);
		return output;
	}

	// This part does the summation for the Information Expected, Needed, and Gain computed.
	public static double return_I(double A, double B) {
		return ((A / (A + B)) * Math.log(A / (A + B)) + (B / (A + B)) * Math.log(B / (A + B)));
	}

}
