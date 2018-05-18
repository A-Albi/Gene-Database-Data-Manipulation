
public class PTC_RC {
	
	/*
	 * This class stores the appropriate data structures, adding a property to give the number of rows and columns.
	 */
	
	private int row;
	private int col;
	private double[][] probabilities;
	
	public PTC_RC(int r, int c) {
		this.row = r;
		this.col = c;
		this.probabilities = new double[r][c];
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public double[][] getProbabilities() {
		return probabilities;
	}
	public void setProbabilities(double[][] probabilities) {
		this.probabilities = probabilities;
	}
	
	public void setval(int r, int c, double v) {
		this.probabilities[r][c] = v;
	}
	
	public double getval(int r, int c) {
		return this.probabilities[r][c];
	}
}
