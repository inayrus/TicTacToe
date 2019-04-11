// A mobile game of tic tac toe for Android
// by Valerie Sawirja

package com.example.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    // a variable to hold the game
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // initialize a new game
            game = new Game();

            // set text, whose turn it is
            TextView turn = findViewById(R.id.Turns);
            turn.setText("Player One starts!");
            turn.setTextColor(Color.parseColor("#FF0099CC"));
        }
        else {
            // restore game variable
            game = (Game) savedInstanceState.getSerializable("gameObj");

            // restore textboxes and colours
            ((TextView)findViewById(R.id.Turns)).setText(savedInstanceState.getString("turnBox"));
            ((TextView)findViewById(R.id.Won)).setText(savedInstanceState.getString("wonBox"));
            ((TextView) findViewById(R.id.Turns)).setTextColor(savedInstanceState.getInt("turnColor"));
            ((TextView) findViewById(R.id.Won)).setTextColor(savedInstanceState.getInt("wonColor"));

            // restore symbols in buttons
            ArrayList<String> buttonTexts = savedInstanceState.getStringArrayList("buttonSyms");

            for (int i=1; i<10; i++) {
                // call the view of every button
                int resID = getResources().getIdentifier("button" + i, "id", getPackageName());
                Button currButton = findViewById(resID);
                // assign the saved text (symbol) to the button
                String text = buttonTexts.get(i - 1);
                currButton.setText(text);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save the game instance (will this also restore the texts and the symbols?
        outState.putSerializable("gameObj", game);

        // LOOP THROUGH ALL THE BUTTONS TO STORE THEIR IMAGE/TEXT
        ArrayList<String> buttonSymbols = new ArrayList<String>(9);

        for (int i=1; i<10; i++){
            // call the view of every button
            int resID = getResources().getIdentifier("button"+i, "id", getPackageName());
            Button currButton = findViewById(resID);
            // get text of button
            String text = currButton.getText().toString();;
            // add text to the array
            buttonSymbols.add(text);
        }
        outState.putStringArrayList("buttonSyms", buttonSymbols);

        // get text from the boxes
        outState.putString("turnBox", ((TextView)findViewById(R.id.Turns)).getText().toString());
        outState.putString("wonBox", ((TextView)findViewById(R.id.Won)).getText().toString());
        outState.putInt("turnColor", ((TextView) findViewById(R.id.Turns)).getCurrentTextColor());
        outState.putInt("wonColor", ((TextView) findViewById(R.id.Won)).getCurrentTextColor());
    }


    // a method to process tile clicks
    public void tileClicked(View view) {
        Button clickedButton = (Button) view;

        // prevent that the tiles can be clicked twice (and unnecessarily increase the moveCounter)
        clickedButton.setClickable(false);

        // get coordinates of the clicked button in the grid
        float xPosition = clickedButton.getX();
        int column = (int) xPosition / clickedButton.getWidth();

        float yPosition = clickedButton.getY();
        int row = (int) yPosition / clickedButton.getHeight();

        // check if player can perform their chosen move
        TileState tile = game.choose(row, column);
        // get view of the Turn textbox
        TextView turn = findViewById(R.id.Turns);

        // update state of selected button and whose turn it is above the board
        switch(tile) {
            case CROSS:
                clickedButton.setText("X");
                turn.setText("Player Two:");
                turn.setTextColor(Color.parseColor("#FFCC0000"));
                break;
            case CIRCLE:
                clickedButton.setText("O");
                turn.setText("Player One:");
                turn.setTextColor(Color.parseColor("#FF0099CC"));
                break;
            case INVALID:
                // what do??
                break;
        }

        // check if the game is won
        GameState state = game.won(row, column);

        // give player feedback when the game is over
        displayFinalState(state);
    }

    public void displayFinalState(GameState state) {

        // get View of the turn textbox
        TextView turn = findViewById(R.id.Turns);

        if (state != GameState.IN_PROGRESS) {

            // disable all buttons
            disableButtons(findViewById(R.id.GridLayout));

            // set turn text box empty
            turn.setText("");

            TextView wonBox = findViewById(R.id.Won);

            // display the final game state under the board
            if (state == GameState.PLAYER_ONE) {
                wonBox.setText("Player One won!");
                wonBox.setTextColor(Color.parseColor("#FF0099CC"));
            }
            else if (state == GameState.PLAYER_TWO) {
                wonBox.setText("Player Two won!");
                wonBox.setTextColor(Color.parseColor("#FFCC0000"));
            }
            else if (state == GameState.DRAW) {
                wonBox.setText("It's a Draw!");
            }
        }
    }


    public void disableButtons(View GL) {
        // get all the buttons in the View
        ArrayList allButtons = GL.getTouchables();

        // loop through the buttons to disable them
        for (Object button: allButtons) {
            ((Button)button).setClickable(false);
        }
    }

    public void resetClicked(View reset_button) {
        // reset the user interface
        setContentView(R.layout.activity_main);

        // reset the TileStates of the game
        game = new Game();

        // set start text, whose turn is it
        TextView turn = findViewById(R.id.Turns);
        turn.setText("Player One starts!");
        turn.setTextColor(Color.parseColor("#FF0099CC"));
    }
}
