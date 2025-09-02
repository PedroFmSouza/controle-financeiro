package com.pedro.financeiro.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CadastroTransacaoUI {

    public void start(Stage stage, String tipo) {
        Label lblValor = new Label("Valor:");
        TextField txtValor = new TextField();

        Label lblCategoria = new Label("Categoria:");
        TextField txtCategoria = new TextField();

        Button btnSalvar = new Button("Salvar " + tipo);
        Button btnCancelar = new Button("Cancelar");

        GridPane layout = new GridPane();
        layout.setStyle("-fx-padding: 20; -fx-hgap: 10; -fx-vgap: 10; -fx-background-color: #ffffff;");
        layout.add(lblValor, 0, 0);
        layout.add(txtValor, 1, 0);
        layout.add(lblCategoria, 0, 1);
        layout.add(txtCategoria, 1, 1);
        layout.add(btnSalvar, 0, 2);
        layout.add(btnCancelar, 1, 2);

        Scene scene = new Scene(layout, 350, 200);
        stage.setTitle("Cadastrar " + tipo);
        stage.setScene(scene);
        stage.show();

        btnCancelar.setOnAction(e -> stage.close());
        btnSalvar.setOnAction(e -> {
            System.out.println(tipo + " salva: " + txtValor.getText() + " (" + txtCategoria.getText() + ")");
            stage.close();
        });
    }
}
