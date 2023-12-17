package com.example.chess;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ChooseModeController {

    private GameMode mode;

    public void setOnePlayerMode(ActionEvent actionEvent) {
        mode = GameMode.ONE_PLAYER;
        closeStage(actionEvent);
    }

    public void setTwoPlayerMode(ActionEvent actionEvent) {
        mode = GameMode.TWO_PLAYERS;
        closeStage(actionEvent);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }
}
