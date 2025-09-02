package com.pedro.financeiro.ui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SaldoUI {

    public void start(Stage stage) {
        Label lblSaldo = new Label("💰 Saldo atual: R$ 850,00");
        lblSaldo.setStyle("-fx-font-size: 20px; -fx-text-fill: green;");

        VBox layout = new VBox(lblSaldo);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 300, 150);
        stage.setTitle("Saldo Atual");
        stage.setScene(scene);
        stage.show();
    }
}
