import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.sun.prism.paint.Color;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class View extends Application {

	private Map<Integer, TextField> map = new HashMap<Integer, TextField>();
	private Date start;
	private Timeline timeline;
	private long countUp;
	private Stage stage;

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {

		stage = primaryStage;
		
		GridPane board = new GridPane();

		PseudoClass right = PseudoClass.getPseudoClass("right");
		PseudoClass bottom = PseudoClass.getPseudoClass("bottom");
		Game game = new Game();
		game.sudoku = game.initializeSudoku();


		Random cells = new Random();
		int max = 30, min=15;
		int randomNum = cells.nextInt((max - min) + 1) + min;
		HashMap<Integer,Integer> initial = new HashMap<Integer,Integer>();

		Game solution = new Game();
		Game.Board sol = solution.new Board(9,9,9);
		sol = solution.initializeSudoku();
		
		while(true) {
		
		for(int i=0; i<randomNum; i++) {
			Random coordinates = new Random();
			min = 0; max = 80;
			int xy = coordinates.nextInt((max - min) + 1) + min;

		//	while(true) {
				Random number = new Random();
				min = 1; max = 9;
				int num= number.nextInt((max - min) + 1) + min;

				if(game.isLegalSudoku((xy-xy%9)/9, xy%9, num)){

					int[][] copy = new int[9][9];
					for(int k = 0;k<copy.length;k++) {
						for(int j = 0; j<copy.length; j++)
							copy[k][j]=game.sudoku.getValue(k, j);
					}
					sol.setValues(copy);
					sol.setValue((xy-xy%9)/9, xy%9, num);
					solution.sudoku= sol;
				//	if(solution.sudokuSolver()) {

						game.sudoku.setValue((xy-xy%9)/9, xy%9, num);
						initial.put(xy, num);
//						break;

				//	}

		//		}
			}
				
		}
			if(solution.sudokuSolver()) break;
		}
		
		

		HashMap<Integer,Integer> printed = new HashMap<Integer, Integer>();
		for (int col = 0; col < 9; col++) {
			for (int row = 0; row < 9; row++) {
				StackPane cell = new StackPane();
				cell.getStyleClass().add("cell");
				cell.pseudoClassStateChanged(right, col == 2 || col == 5);
				cell.pseudoClassStateChanged(bottom, row == 2 || row == 5);

				cell.getChildren().add(createSudokuField(row, col, printed, game, game.sudoku, initial));

				board.add(cell, col, row);
			}
		}
		board.addRow(9, new Text(""));


		VBox pane = new VBox();		

		Button newGame = new Button("new Game"); 
		Button clear = new Button("clear");
		Button solve = new Button("solve");

		newGame.setOnAction(e -> {
			try {
				start(primaryStage);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		solve.setOnAction(e -> {
			for(int i = 0; i<81; i++) {
				if(!map.get(new Integer(i)).getStyle().equals("-fx-text-fill: blue;") && 
						!map.get(new Integer(i)).getStyle().equals("-fx-text-fill: black;")
						&& !map.get(new Integer(i)).getStyle().equals("-fx-text-fill: red;")) 
					map.get(new Integer(i)).setStyle("-fx-text-fill: green;");
				map.get(new Integer(i)).setText(new String("" + solution.sudoku.getValue(i/9, i%9)));
			}
		});

		clear.setOnAction(e -> {
			for(int i = 0; i<81; i++) {
				if(!map.get(new Integer(i)).getStyle().equals("-fx-text-fill: blue;")) {
					map.get(new Integer(i)).setText("");
					int[][] copy = game.sudoku.getValues();
					copy [i/9][i%9] = -1;
					game.sudoku.setValues(copy);
				}
			}
		});

		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(newGame, clear, solve);

		hbox.setAlignment(Pos.CENTER);

		pane.getChildren().addAll(board, hbox);
		startTimer();
		Scene scene = new Scene(pane);
		scene.getStylesheets().add("sudoku.css");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Sudoku Game	Time: 0");
		primaryStage.show();
	}
	
	private void startTimer() {
		start = Calendar.getInstance().getTime();
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			countUp = Calendar.getInstance().getTime().getTime() - start.getTime();
			stage.setTitle(
					"Sudoku - Time: " + String.valueOf(TimeUnit.SECONDS.convert(countUp, TimeUnit.MILLISECONDS)));
		}));

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	private TextField createSudokuField(int row, int column, HashMap<Integer,Integer> printed,Game game, Game.Board sudoku, HashMap<Integer,Integer> initial) {

		TextField textField = new TextField();
		textField.setAlignment(Pos.CENTER);

		if(initial.get(row*9+column)!=null && initial.get(row*9+column)!=0) {

			textField.setStyle("-fx-text-fill: blue;");		
			textField.setText((initial.get(row*9+column)).toString());
		}
		else {
			// restrict input to integers:
			textField.setTextFormatter(new TextFormatter<Integer>(c -> {

				if (c.getControlNewText().matches("[1-9]") 
						&& game.isLegalSudoku(row, column, Integer.parseInt(c.getControlNewText())) 
						&& sudokuWithNum(game,sudoku, row, column, Integer.parseInt(c.getControlNewText()))!=null) {


					if(printed.get(row*9+column)==null || printed.get(row*9+column)!=-1) {
//						System.out.println(row + " " + column);
						printed.put(row*9+column, -1);

						sudoku.setValue(row,column, Integer.parseInt(c.getControlNewText()));

					}
					return c ;

				} 

				if(c.getControlNewText().matches("[1-9]") && !game.isLegalSudoku(row, column, Integer.parseInt(c.getControlNewText())) && (sudokuWithNum(game,sudoku, row, column, Integer.parseInt(c.getControlNewText()))!=null)){
					Game.Board test = sudoku;
					int[][] solution = sudokuWithNum(game, sudoku, row, column, Integer.parseInt(c.getControlNewText()));
					sudoku.setValue(row, column, solution[row][column]);
					textField.setStyle("-fx-text-fill: red;");		
					textField.setText(new Integer(solution[row][column]).toString());
					return null ;
				}

//				if(c.getControlNewText().matches("[1-9]") && (sudokuWithNum(game,sudoku, row, column, Integer.parseInt(c.getControlNewText()))==null)) {
//					Alert box = new Alert(AlertType.NONE);
//					box.setHeaderText(null);
//					box.setContentText("There is no solution for this sudoku");
//					box.show();
//				}
				return c;
			}));
		}
		map.put(row*9+column, textField);
		return textField ;
	}



	public static int[][] sudokuWithNum(Game game, Game.Board sudoku, int row, int column, int number)  {

		int[][] copy = new int[9][9];
		for(int i = 0;i<copy.length;i++) {
			for(int j = 0; j<copy.length; j++)
				copy[i][j]=sudoku.getValue(i, j);
		}

		Game solution = new Game();
		Game.Board sol = solution.new Board(9,9,9);
		try {
			sol = solution.initializeSudoku();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sol.setValues(sudoku.getValues());

		solution.sudoku=sol;

		if(game.isLegalSudoku(row, column, number) && solution.sudokuSolver()) {
			solution.sudoku.setValue(row, column, number);

			//if(solution.sudokuSolver()) {
			int[][] test  = solution.sudoku.getValues();
			sudoku.setValues(copy);
			sudoku.setValue(row, column, solution.sudoku.getValue(row, column));	
		}

		else {
			//solution.sudoku.setValue(row, column, -1);

			if(solution.sudokuSolver()) {
				sudoku.setValues(copy);
				sudoku.setValue(row, column, solution.sudoku.getValue(row, column));
			}
			else return null;
		}
		return sudoku.getValues();
	}



	public static void main(String[] args) {
		launch(args);
	}
}
