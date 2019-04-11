// a representation of the tic tac toe game
// by Valerie Sawirja

package com.example.tictactoe;

import android.util.Log;
import java.io.Serializable;


public class Game implements Serializable {

    // a constant variable that holds the board
    final private int BOARD_SIZE = 3;

    // board is an array of TileState s
    private TileState[][] board;

    // counters
    private Boolean playerOneTurn;  // true if player 1's turn, false if player 2's turn
    private int movesPlayed;
    private Boolean gameOver;

    // a constructor, or initializer
    public Game() {
        board = new TileState[BOARD_SIZE][BOARD_SIZE];
        for(int i=0; i<BOARD_SIZE; i++)
            for(int j=0; j<BOARD_SIZE; j++)
                board[i][j] = TileState.BLANK;

        playerOneTurn = true;
        gameOver = false;
        movesPlayed = 0;
    }

    // method to let the player place a cross or circle
    public TileState choose(int row, int column) {

        // check if the tile is empty
        if (board[row][column] == TileState.BLANK) {
            // increase movesPlayed
            movesPlayed++;

            // if player One: place cross
            if (playerOneTurn) {
                board[row][column] = TileState.CROSS;
                // give the other player a turn
                playerOneTurn = false;
                // update UI
                return TileState.CROSS;
            }
            // if player Two: place circle
            else {
                board[row][column] = TileState.CIRCLE;
                // give the other player a turn
                playerOneTurn = true;
                // update UI
                return TileState.CIRCLE;
            }
        }
        // return invalid if the tile is not empty
        else {
            return TileState.INVALID;
        }
    }

    // a method to verify the state of the board (in progress/won/draw)
    public GameState won(int row, int column) {

        // save the symbol that was just played
        TileState symbol = board[row][column];
        int horizontals = 0;
        int verticals = 0;
        int diagonalOne = 0;
        int diagonalTwo = 0;

        // checks if three of the same symbols are placed in a row
        for (int i = 0; i < BOARD_SIZE; i++) {

            if (board[i][column] == symbol) {
                verticals++;
            }
            if (board[row][i] == symbol) {
                horizontals++;
            }
            if (board[i][i] == symbol) {
                diagonalOne++;
            }
            if (board[i][BOARD_SIZE - 1 - i] == symbol) {
                diagonalTwo++;
            }
        }

        // check if the current move won the game
        if ((verticals == 3) || (horizontals == 3) || (diagonalOne == 3) || (diagonalTwo == 3)) {

            if (symbol == TileState.CROSS) {
                return GameState.PLAYER_ONE;
            } else {
                return GameState.PLAYER_TWO;
            }
        }

        // if no winner, is there a draw or is the game in progress
        if (movesPlayed == 9) {
            return GameState.DRAW;
        }
        else {
            return GameState.IN_PROGRESS;
        }
    }
}
