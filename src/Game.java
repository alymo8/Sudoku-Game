import java.util.*;
import java.lang.*;
import java.io.*;

public class Game {

	public Board sudoku;

	public class Cell{
		private int row = 0;
		private int column = 0;

		public Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}
		public int getRow() {
			return row;
		}
		public int getColumn() {
			return column;
		}

	}

	public class Region{
		private Cell[] matrix;
		private int num_cells;
		public Region(int num_cells) {
			this.matrix = new Cell[num_cells];
			this.num_cells = num_cells;
		}
		public Cell[] getCells() {
			return matrix;
		}
		public void setCell(int pos, Cell element){
			matrix[pos] = element;
		}

	}

	public class Board{
		private int[][] board_values;
		private Region[] board_regions;
		private int num_rows;
		private int num_columns;
		private int num_regions;

		public Board(int num_rows,int num_columns, int num_regions){
			this.board_values = new int[num_rows][num_columns];
			this.board_regions = new Region[num_regions];
			this.num_rows = num_rows;
			this.num_columns = num_columns;
			this.num_regions = num_regions;
		}


		public int[][] getValues(){
			return board_values;
		}
		public int getValue(int row, int column) {
			return board_values[row][column];
		}
		public Region getRegion(int index) {
			return board_regions[index];
		}
		public Region[] getRegions(){
			return board_regions;
		}
		public void setValue(int row, int column, int value){
			board_values[row][column] = value;
		}
		public void setRegion(int index, Region initial_region) {
			board_regions[index] = initial_region;
		}	
		public void setValues(int[][] values) {
			board_values = values;
		}

	}

	private boolean chkAround(int i, int j, int number) {

		if(sudoku.num_rows==1 && sudoku.num_columns==1) {//only 1 number
			return true;
		}
		else if(sudoku.num_rows==1 && sudoku.num_columns!=1) {//one row and many columns
			if(j==0) {//we are on the left, only look right
				if(number == sudoku.getValue(i, j+1)) return false;		//check right
			}
			else if(j==sudoku.num_columns-1) {//we are on the right, only look left
				if(number == sudoku.getValue(i, j-1)) return false;		//check left
			}
			else {//we are in the middle
				if(number == sudoku.getValue(i, j-1)) return false;		//check left
				if(number == sudoku.getValue(i, j+1)) return false;		//check right
			}	
		}
		else if(sudoku.num_rows!=1 && sudoku.num_columns==1) {//rows != 1 cols=1 we have a vector
			if(i==0) {//we are up, look down
				if(number == sudoku.getValue(i+1, j)) return false;		//check down
			}
			else if(i==sudoku.num_rows-1) {//we are down, look up
				if(number == sudoku.getValue(i-1, j)) return false;		//check up
			}
			else {//we are in the middle of the vector
				if(number == sudoku.getValue(i-1, j)) return false;		//check up
				if(number == sudoku.getValue(i+1, j)) return false;		//check down
			}
		}
		else {

			if(i!=0 && j!=0 && j!= sudoku.num_columns-1 && i!= sudoku.num_rows-1) {	//general case

				if(number == sudoku.getValue(i+1, j+1)) return false;	//check down right
				if(number == sudoku.getValue(i+1, j-1)) return false;	//check down left
				if(number == sudoku.getValue(i-1, j-1)) return false;	//check up left
				if(number == sudoku.getValue(i-1, j+1)) return false;	//check up right	
				if(number == sudoku.getValue(i-1, j)) return false;		//check up
				if(number == sudoku.getValue(i+1, j)) return false;		//check down
				if(number == sudoku.getValue(i, j-1)) return false;		//check left
				if(number == sudoku.getValue(i, j+1)) return false;		//check right
			}


			if(i==0 && j !=0 && j!=sudoku.num_columns-1) {	//we are in the middle of first row, don't check up, up left, up right
				if(number == sudoku.getValue(i+1, j+1)) return false;	//check down right
				if(number == sudoku.getValue(i, j+1)) return false;		//check right
				if(number == sudoku.getValue(i, j-1)) return false;		//check left
				if(number == sudoku.getValue(i+1, j)) return false;		//check down
				if(number == sudoku.getValue(i+1, j-1)) return false;	//check down left
			}

			if(i==sudoku.num_rows-1 && j!=0 && j!=sudoku.num_columns-1) {	//we are in the middle of last row, check up, up right, up left, right, left
				if(number == sudoku.getValue(i-1, j-1)) return false;	//check up left
				if(number == sudoku.getValue(i-1, j+1)) return false;	//check up right	
				if(number == sudoku.getValue(i-1, j)) return false;		//check up
				if(number == sudoku.getValue(i, j-1)) return false;		//check left
				if(number == sudoku.getValue(i, j+1)) return false;		//check right
			}

			if(i!=0 && j==0 && i!=sudoku.num_rows-1) {	//we are in the middle of the first column, check right, up, down, up right, down right
				if(number == sudoku.getValue(i-1, j+1)) return false;	//check up right	
				if(number == sudoku.getValue(i-1, j)) return false;		//check up
				if(number == sudoku.getValue(i+1, j)) return false;		//check down
				if(number == sudoku.getValue(i-1, j+1)) return false;	//check up right
				if(number == sudoku.getValue(i+1, j+1)) return false;	//check down right
			}

			if(i!=sudoku.num_rows-1 && i!=0 && j==sudoku.num_columns-1) {	//we are in the middle of the last column, check left, up left, down left, up, down
				if(number == sudoku.getValue(i-1, j)) return false;		//check up
				if(number == sudoku.getValue(i+1, j)) return false;		//check down
				if(number == sudoku.getValue(i, j-1)) return false;		//check left
				if(number == sudoku.getValue(i+1, j-1)) return false;	//check down left
				if(number == sudoku.getValue(i-1, j-1)) return false;	//check up left
			}

			if(i==0 && j==0) {	//we are in the cell [0][0] only check right, down, down right
				if(number == sudoku.getValue(i, j+1)) return false;		//check right
				if(number == sudoku.getValue(i+1, j)) return false;		//check down
				if(number == sudoku.getValue(i+1, j+1)) return false;	//check down right
			}
			if(i==0 && j==sudoku.num_columns-1) {//we are on top right only check left, down left, down
				if(number == sudoku.getValue(i+1, j-1)) return false;	//check down left
				if(number == sudoku.getValue(i+1, j)) return false;		//check down
				if(number == sudoku.getValue(i, j-1)) return false;		//check left
			}
			if(i==sudoku.num_rows-1 && j==0) { // we are on bottom left, check right, up right, up
				if(number == sudoku.getValue(i-1, j+1)) return false;	//check up right	
				if(number == sudoku.getValue(i-1, j)) return false;		//check up
				if(number == sudoku.getValue(i, j+1)) return false;		//check right
			}
			if(i==sudoku.num_rows-1 && j==sudoku.num_columns-1) { //we are on bottom right, check for left, up, up left
				if(number == sudoku.getValue(i-1, j-1)) return false;	//check up left
				if(number == sudoku.getValue(i, j-1)) return false;		//check left
				if(number == sudoku.getValue(i-1, j)) return false;		//check up
			}
		}

		return true;
	}

	private Region getRegion(int i, int j) {
		for(int k=0; k<sudoku.getRegions().length; k++) {
			for(int m=0; m<sudoku.getRegion(k).getCells().length;m++) {
				if(sudoku.getRegion(k).getCells()[m].getRow() == i && sudoku.getRegion(k).getCells()[m].getColumn() == j) {
					return sudoku.getRegion(k);
				}
			}
		}
		return null;
	}


	private boolean chkRegionSudoku2(int i, int j, int number) {
		//checks the determined region in the new sudoku game
		for(int k=0; k<getRegion(i,j).getCells().length;k++) {
			if(number == sudoku.getValue (getRegion(i,j).getCells()[k].getRow(),getRegion(i,j).getCells()[k].getColumn())) return false;
		}
		return true;
	}

	public boolean isLegalSudoku(int i, int j, int number) {

		//checks the row of the current number
		for(int k = 0; k<sudoku.num_columns;k++) {
			if(sudoku.getValue(i, k)==number && k!=j) return false;
		}

		//checks the column of the current number
		for(int k = 0; k<sudoku.num_rows;k++) {
			if(sudoku.getValue(k, j)==number && k!=i) return false;
		}

		//checks the region of the current number
		int startRow = i - i % 3;
		int startCol = j - j % 3;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (sudoku.getValue(row + startRow,col + startCol) == number && (row + startRow)!=i && (col + startCol)!=j)return false;
			}
		}
		return true;
	}

	private boolean isLegalSudoku2(int i, int j, int number) {
		if(chkRegionSudoku2(i,j,number) && chkAround(i,j,number)) return true;
		return false;
	}

	public boolean sudokuSolver() {
		int row =-1;
		int column=-1;
		boolean isEmpty = true;
		for(int i = 0; i<sudoku.num_rows;i++) {
			for(int j =0; j<sudoku.num_columns;j++) {
				if(sudoku.getValue(i, j) == -1) {
					row=i;
					column=j;
					isEmpty=false;	//row and col to work on
					break;
				}
			}
			if(!isEmpty) break;	//we need to work on filling current row and col
		}
		if(isEmpty) {
			return true;
		}
		for(int num = 1; num <= getRegion(row,column).getCells().length; num++) {	//possible values in current region
			if(isLegalSudoku(row,column,num)) {	//check if current value works with previous values filled
				sudoku.setValue(row, column, num);
				if(sudokuSolver()) return true;	//if the solution doesn't fail
				else sudoku.setValue(row, column, -1);	//empty current slot, this solution did not work
			}
		}

		return false;
	}

	public int[][] solver() {
		sudokuSolver();
		return sudoku.getValues();
	}

	public Game.Board initializeSudoku() throws FileNotFoundException {
		Scanner sc = new Scanner(new File("src/test.in"));
		int rows = sc.nextInt();
		int columns = sc.nextInt();
		int[][] board = new int[rows][columns];
		//Reading the board
		for (int i=0; i<rows; i++){
			for (int j=0; j<columns; j++){
				String value = sc.next();
				if (value.equals("-")) {
					board[i][j] = -1;
				}else {
					try {
						board[i][j] = Integer.valueOf(value);
					}	catch(Exception e) {
						System.out.println("Ups, something went wrong");
					}
				}	
			}
		}
		int regions = sc.nextInt();
		Game game = new Game();
		game.sudoku = game.new Board(rows, columns, regions);
		game.sudoku.setValues(board);
		for (int i=0; i< regions;i++) {
			int num_cells = sc.nextInt();
			Game.Region new_region = game.new Region(num_cells);
			for (int j=0; j< num_cells; j++) {
				String cell = sc.next();
				String value1 = cell.substring(cell.indexOf("(") + 1, cell.indexOf(","));
				String value2 = cell.substring(cell.indexOf(",") + 1, cell.indexOf(")"));
				Game.Cell new_cell = game.new Cell(Integer.valueOf(value1)-1,Integer.valueOf(value2)-1);
				new_region.setCell(j, new_cell);
			}
			game.sudoku.setRegion(i, new_region);
		}
//		int[][] answer = game.solver();
//		for (int i=0; i<answer.length;i++) {
//			for (int j=0; j<answer[0].length; j++) {
//				System.out.print(answer[i][j]);
//				if (j<answer[0].length -1) {
//					System.out.print(" ");
//				}
//			}
//			System.out.println();
//		}
		return game.sudoku;
	}



}


