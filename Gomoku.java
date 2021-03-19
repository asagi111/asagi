import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.Node;
import javafx.application.Platform;

/**
 * This class represents the Gomoku
 * @author Sherry Zhu
 */

public class Gomoku extends Application
{
  /** ==================================== FIELDS ==================================== */
  
  /** Represents the entire gameBoard */
  private static Button[][] gameBoard;
  
  /** Represents the number of rows of the gameBoard */
  private static int numRow = 0;
  
  /**Represents the number of columns of the gameBoard */
  private static int numColumn = 0;
  
  /**Represents number of pieces to be together that you can win */
  private static int numWin = 5;
  
  /** 
   * Represents the background of the game board
   * The default color is green
   */
  private BackgroundFill emptyBoard = new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, new Insets(1.0));
  
  /** Represents a spot where a black piece is placed */
  private BackgroundFill blackPiece = new BackgroundFill(Color.BLACK, new CornerRadii(25.0), new Insets(3.0));
  
  /** Represents a spot where a white piece is placed */
  private BackgroundFill whitePiece = new BackgroundFill(Color.WHITE, new CornerRadii(25.0), new Insets(3.0));
  
  /** 
   * Represents if it's a black piece's turn
   * If not, then it's white piece's turn
   */
  private boolean isBlackTurn = true;
  
  /** Represents if a winner has occuered yet */
  private static boolean hasWinner = false;
  
  /**Stores the grid pane of the application */
  private GridPane pane = new GridPane();
  
  /** ==================================== GETTER/SETTER METHODS ==================================== */
  /**
   * Gets the value of isBlackTurn
   */
  public boolean getIsBlackTurn()
  {
    return isBlackTurn;
  }
  
  /**
   * Sets the value of isBlackTurn
   * @param isBlackTurn desired boolean value
   */
  public void setIsBlackTurn(boolean isBlackTurn)
  {
    this.isBlackTurn = isBlackTurn;
  }
  
  /**
   * Gets the value of emptyBoard
   */
  public BackgroundFill getEmptyBoard()
  {
    return emptyBoard;
  }
  
  /**
   * Sets the value of emptyBoard
   * @param emptyBoard the desired background fill for a empty board background
   */
  public void setEmptyBoard(BackgroundFill emptyBoard)
  {
    this.emptyBoard = emptyBoard;
  }
  
  /**
   * Gets the blackPiece field
   */
  public BackgroundFill getBlackPiece()
  {
    return blackPiece;
  }
  
  /**
   * Sets the blackPiece field
   * @param blackPiece desired black piece
   */
  public void setBlackPiece(BackgroundFill blackPiece)
  {
    this.blackPiece = blackPiece;
  }
  
  /**
   * Gets the whitePiece field
   */
  public BackgroundFill getWhitePiece()
  {
    return whitePiece;
  }
  
  /**
   * Sets the whitePiece field
   * @param whitePiece desired white piece
   */
  public void setWhitePiece(BackgroundFill whitePiece)
  {
    this.whitePiece = whitePiece;
  }
  
  /** ==================================== IMPORTANT METHODS ==================================== */
  
  /**
   * Starts the program
   * @param primaryStage the stage to start
   */
  public void start(Stage primaryStage)
  { 
    /**
     * Creates a gameBoard so that the location of a button in the array corresponds to its location in the display grid
     * Also defines the actions for the buttons
     */
    for(int row = 0; row < gameBoard.length; row++)
    {
      for(int column = 0; column < gameBoard[row].length; column++)
      {
        gameBoard[row][column] = new Button();
        this.setButtonBlank(gameBoard[row][column]);
        pane.add(gameBoard[row][column], column, row);
        gameBoard[row][column].setOnAction(new ClickBlank());
      }
    }
    
    /** Sets the panel on the scene */
    Scene scene = new Scene(pane);
    
    /** Sets the scene on the stage */
    primaryStage.setScene(scene);
    
    /** Shows the stage */
    primaryStage.show();
  }
  /** ==================================== HELPER METHODS ==================================== */
  
  /**
   * Sets the button so it appears to be a blank board
   * Note: The argument is in Region so that the method can be tested in JUnit
   * @param button the desired button to be blank
   */
  public void setButtonBlank(Region button)
  {
    /** Sets a blank spot to be a perfect square */
    button.setPrefSize(25.0, 25.0);
    
    /**Sets a blank spot to be green and shows the edges */
    button.setBackground(new Background(getEmptyBoard()));
  }
  
  /**
   * Returns the piece that's going next and updates the turn
   * If the piece that will be placed is black, return 0
   * If the piece that will be placed is white, return 1
   */
  public int nextPiece()
  {
    if(isBlackTurn)
    {
      /** Updates the turn */
      setIsBlackTurn(false);
      return 0;
    }
    
    else
    {
      /** Updates the turn */
      setIsBlackTurn(true);
      return 1;
    }
  }
  
  /**
   * Determines whether the current square is occupied by a game piece
   * Note: The argument is in Region so that the method can be tested in JUnit
   * @param button the square that's being examined
   */
  public boolean isBlank(Region button)
  {
    /** If the background of button matches the background of emptyBoard, it is blank */
    if (button.getBackground().equals(new Background(getEmptyBoard())))
      return true;
    
    else
      return false;
  }
  
  /**
   * Determines if the selected piece has the same color as the current piece
   * @param piece1 the current piece
   * @param piece2 the selected piece
   */
  public boolean hasSameColor(Region piece1, Region piece2)
  {
    /** If two piece's have the same background, they have the same color */
    if (piece1.getBackground().equals(piece2.getBackground()))
      return true;
    
    else
      return false;
  }
  
  /**
   * Returns the number of pieces of the same color in a straight line
   * @param board the game board
   * @param row the row the first piece is at
   * @param column the column the first piece is at
   * @param direction the direction to check
   */
  public int numberInLine(Region[][] board, int row, int column, String direction)
  { 
    int count = 1;
    
    if (direction.equals("right"))
    {
      while(column + count < numColumn && !isBlank(board[row][column + count]) && hasSameColor(board[row][column], board[row][column + count]))
      {
        count++;
      }
    }
    
    if (direction.equals("left"))
    {
      while(column - count >= 0 && !isBlank(board[row][column - count]) && hasSameColor(board[row][column], board[row][column - count]))
      {
        count++;
      }
    }
    
    if (direction.equals("down"))
    {
      while(row + count < numRow && !isBlank(board[row + count][column]) && hasSameColor(board[row][column], board[row + count][column]))
      {
        count++;
      }
    }
    
    if (direction.equals("up"))
    {
      while(row - count >= 0 && !isBlank(board[row - count][column]) && hasSameColor(board[row][column], board[row - count][column]))
      {
        count++;
      }
    }
    
    if (direction.equals("left up corner"))
    {
      while(row - count >= 0 && column - count >= 0 && !isBlank(board[row - count][column - count]) && hasSameColor(board[row][column], board[row - count][column - count]))
      {
        count++;
      }
    }
    
    if (direction.equals("right up corner"))
    {
      while(row - count >= 0 && column + count < numColumn && !isBlank(board[row - count][column + count]) && hasSameColor(board[row][column], board[row - count][column + count]))
      {
        count++;
      }
    }
    
    if (direction.equals("left down corner"))
    {
      while(row + count < numRow && column - count >= 0 && !isBlank(board[row + count][column - count]) && hasSameColor(board[row][column], board[row + count][column - count]))
      {
        count++;
      }
    }
    
    if (direction.equals("right down corner"))
    {
      while(row + count < numRow && column + count < numColumn && !isBlank(board[row + count][column + count]) && hasSameColor(board[row][column], board[row + count][column + count]))
      {
        count++;
      }
    }
    
    return count;
  }
  
  /**
   * Check if the current piece is qualified for winning 
   * @param row the piece's row number of the game board
   * @param column the piece's column number of the game board
   */
  public boolean checkWin(int row, int column)
  {
    if(numberInLine(gameBoard, row, column, "left") + numberInLine(gameBoard, row, column, "right") - 1 > numWin || numberInLine(gameBoard, row, column, "up") + 
       numberInLine(gameBoard, row, column, "down") - 1 > numWin || numberInLine(gameBoard, row, column, "left up corner") + 
       numberInLine(gameBoard, row, column, "right down corner") - 1 > numWin || numberInLine(gameBoard, row, column, "right up corner") 
         + numberInLine(gameBoard, row, column, "left down corner") - 1 > numWin)  
      return false;
    
    else if(numberInLine(gameBoard, row, column, "left") == numWin || numberInLine(gameBoard, row, column, "right") == numWin || numberInLine(gameBoard, row, column, "up") == numWin 
              || numberInLine(gameBoard, row, column, "down") == numWin || numberInLine(gameBoard, row, column, "left up corner") == numWin
              || numberInLine(gameBoard, row, column, "right up corner") == numWin || numberInLine(gameBoard, row, column, "left down corner") == numWin
              || numberInLine(gameBoard, row, column, "right down corner") == numWin)
      return true;
    
    else
      return false;
  }
  
  /**
   * Check if starting at the currently played piece and following pieces of the same color in the direction specified has an empty space in the end
   * @param board the game board
   * @param row the row the first piece is at
   * @param column the column the first piece is at
   * @param direction the direction to check
   */
  public boolean isOpen(Region[][] board, int row, int column, String direction)
  {
    if(direction.equals("left"))
    {
      if (column - numberInLine(board, row, column, "left") < 0)
        return false;
      
      else if (!isBlank(board[row][column - numberInLine(board, row, column, "left")]))
        return false;
    }
    
    if(direction.equals("right"))
    {
      if (column + numberInLine(board, row, column, "right") >= numColumn)
        return false;
      
      else if (!isBlank(board[row][column + numberInLine(board, row, column, "right")]))
        return false; 
    }
    
    if(direction.equals("up"))
    {
      if (row - numberInLine(board, row, column, "up") < 0)
        return false;
      
      else if (!isBlank(board[row - numberInLine(board, row, column, "up")][column]))
        return false;
    }
    
    if(direction.equals("down"))
    {
      if (row + numberInLine(board, row, column, "down") >= numRow)
        return false;
      
      else if (!isBlank(board[row + numberInLine(board, row, column, "down")][column]))
        return false;
    }
    
    if(direction.equals("left up corner"))
    {
      if (column - numberInLine(board, row, column, "left up corner") < 0 || row - numberInLine(board, row, column, "left up corner") < 0 )
        return false;
      
      else if (!isBlank(board[row - numberInLine(board, row, column, "left up corner")][column - numberInLine(board, row, column, "left up corner")]))
        return false;
    }
    
    if(direction.equals("right up corner"))
    {
      if (column + numberInLine(board, row, column, "right up corner") >= numColumn || row - numberInLine(board, row, column, "right up corner") < 0 )
        return false;
      
      else if (!isBlank(board[row - numberInLine(board, row, column, "right up corner")][column + numberInLine(board, row, column, "right up corner")]))
        return false;
    }
    
    if(direction.equals("left down corner"))
    {
      if (column - numberInLine(board, row, column, "left down corner") < 0 || row + numberInLine(board, row, column, "left down corner") >= numRow )
        return false;
      
      else if (!isBlank(board[row + numberInLine(board, row, column, "left down corner")][column - numberInLine(board, row, column, "left down corner")]))
        return false;
    }
    
    if(direction.equals("right down corner"))
    {
      if (column + numberInLine(board, row, column, "right down corner") >= numColumn || row + numberInLine(board, row, column, "right down corner") >= numRow )
        return false;
      
      else if (!isBlank(board[row + numberInLine(board, row, column, "right down corner")][column + numberInLine(board, row, column, "right down corner")]))
        return false;
    }
    
    return true;
  }
  
  /**
   * Determine if the select piece has the same color as the will-put-down piece
   * @param row select piece's row number
   * @param column select piece's column number
   */
  public boolean isSameColorAsTurn(int row, int column)
  {
    if ((isBlackTurn && gameBoard[row][column].getBackground().equals(new Background(getEmptyBoard(), blackPiece))) || 
        (!isBlackTurn && gameBoard[row][column].getBackground().equals(new Background(getEmptyBoard(), whitePiece))))
      return true;
    
    else
      return false;
  }
  
  /**
   * Helper method for Four-Four and Three-Three
   * @param row the will-be-put piece's row number
   * @param column the will-be-put piece's column number
   * @param ruleNumber the rule's number (e.g. Four-Four = number win - 1)
   */
  public boolean isAgainstHelper(int row, int column, int ruleNumber)
  {
    int count = 0;
    
    if (column - 1 >= 0 && isSameColorAsTurn(row, column - 1) && numberInLine(gameBoard, row, column - 1, "left") == ruleNumber)
      count++;
    
    if (column + 1 < numColumn && isSameColorAsTurn(row, column + 1) && numberInLine(gameBoard, row, column + 1, "right") == ruleNumber)
      count++;
    
    if (row - 1 >= 0 && isSameColorAsTurn(row - 1, column) && numberInLine(gameBoard, row - 1, column, "up") == ruleNumber)
      count++;
    
    if (row + 1 < numRow && isSameColorAsTurn(row + 1, column) &&  numberInLine(gameBoard, row + 1, column, "down") == ruleNumber)
      count++;
    
    if (column - 1 >= 0 && row - 1 >= 0 && isSameColorAsTurn(row - 1, column - 1) && numberInLine(gameBoard, row - 1, column - 1, "left up corner") == ruleNumber)
      count++;
    
    if (column - 1 >= 0 && row + 1 < numRow && isSameColorAsTurn(row + 1, column - 1) && numberInLine(gameBoard, row + 1, column - 1, "left down corner") == ruleNumber)
      count++;
    
    if (column + 1 < numColumn && row - 1 >= 0 && isSameColorAsTurn(row - 1, column + 1) && numberInLine(gameBoard, row - 1, column + 1, "right up corner") == ruleNumber)
      count++;
    
    if (column + 1 < numColumn && row + 1 < numRow && isSameColorAsTurn(row + 1, column + 1) && numberInLine(gameBoard, row + 1, column + 1, "right down corner") == ruleNumber)
      count++;
    
    if (count >= 2)
      return true;
    
    else
    {
      return false;
    }
  }
  
  /**
   * Determine whether the move violates the Four-Four rule (or corresponding number)
   * @param row the will-be-put piece's row number
   * @param column the will-be-put piece's column number
   */
  public boolean isAgainstFourFour(int row, int column)
  {
    if(isAgainstHelper(row, column, numWin - 1))
      return true;
    
    else
      return false;
  }
  
  /**
   * Determine whether the move violates the Three-Three rule (or corresponding number)
   * @param row the will-be-put piece's row number
   * @param column the will-be-put piece's column number
   */
  public boolean isAgainstThreeThree(int row, int column)
  {
    int count = 0;
    
    if(column - 1 >= 0 && !isBlank(gameBoard[row][column - 1]) && isOpen(gameBoard, row, column - 1, "left"))
      count++;
    
    if(column + 1 < numColumn && !isBlank(gameBoard[row][column + 1]) && isOpen(gameBoard, row, column + 1, "right"))
      count++;
    
    if(row - 1 >= 0 && !isBlank(gameBoard[row - 1][column]) && isOpen(gameBoard, row - 1, column, "up"))
      count++;
    
    if(row + 1 < numRow && !isBlank(gameBoard[row + 1][column]) && isOpen(gameBoard, row + 1, column, "down"))
      count++;
    
    if(column - 1 >= 0 && row - 1 >= 0 && !isBlank(gameBoard[row - 1][column - 1]) && isOpen(gameBoard, row - 1, column - 1, "left up corner"))
      count++;
    
    if(column - 1 >= 0 && row + 1 < numRow && !isBlank(gameBoard[row + 1][column - 1]) && isOpen(gameBoard, row + 1, column - 1, "left down corner"))
      count++;
    
    if(row - 1 >= 0 && column + 1 < numColumn && !isBlank(gameBoard[row - 1][column + 1]) &&isOpen(gameBoard, row - 1, column + 1, "right up corner"))
      count++;
    
    if(row + 1 < numRow && column + 1 < numColumn && !isBlank(gameBoard[row + 1][column + 1]) && isOpen(gameBoard, row + 1, column + 1, "right down corner"))
      count++;
    
    if(isAgainstHelper(row, column, numWin - 2) && count >= 2)
    {
      return true;
    }
    
    else
      return false;
  }
  
  /** ==================================== NESTED CLASSES ==================================== */
  /**
   * A button click event that adds a piece on to the board if that spot is blank
   */
  public class ClickBlank implements EventHandler<ActionEvent>
  {
    @Override
    public void handle(ActionEvent e)
    {
      Button clicked = (Button)e.getSource();
      
      if (!hasWinner && isBlank(clicked) && !isAgainstFourFour(pane.getRowIndex(clicked), pane.getColumnIndex(clicked)) 
            && !isAgainstThreeThree(pane.getRowIndex(clicked), pane.getColumnIndex(clicked)))
      {
        if(nextPiece() == 0)
          clicked.setBackground(new Background(getEmptyBoard(), blackPiece));
        
        else
          clicked.setBackground(new Background(getEmptyBoard(), whitePiece));
      }
      
      for (int row = 0; !hasWinner && row < numRow; row ++)
      {
        for (int column = 0; !hasWinner && column < numColumn; column ++)
        {
          if(checkWin(row, column))
          {
            if(gameBoard[row][column].getBackground().equals(new Background(getEmptyBoard(), blackPiece)))
            {
              System.out.println("BLACK WINS!");
              hasWinner = true;
            }
            
            else if (gameBoard[row][column].getBackground().equals(new Background(getEmptyBoard(), whitePiece)))
            {
              System.out.println("WHITE WINS!");
              hasWinner = true;
            }
          }
        }
      } 
    }
  }
  
  /**
   * Initialize a Gomoku Game
   * @param rows sets the number of rows of the game board
   * @param columns sets the number of columns of the game board
   */
  public static void setGomoku(int rows, int columns)
  {
    gameBoard = new Button[rows][columns];
    numRow = rows;
    numColumn = columns;
  }
  
  public static void main(String[] args)
  {
    try
    {
      if (args.length == 0)
      {
        setGomoku(19, 19);
      }
      
      else if (args.length == 1)
      {
        numWin = Integer.parseInt(args[0]);
      }
      
      else if (args.length == 2)
      {
        setGomoku(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
      }
      
      else if (args.length == 3)
      {
        numWin = Integer.parseInt(args[0]);
        setGomoku(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
      }
      
      else
        System.out.println("Please input maximum three integers");
    }
    
    catch (NumberFormatException e)
    {
      System.out.println("Please input integers only");
    }
    
    Application.launch(args);
  }
}