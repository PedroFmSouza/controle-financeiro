package com.pedro.financeiro.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainUI extends Application {

    @Override
    public void start(Stage stage) {
        // Botões do menu
        Button btnReceita = new Button("➕ Cadastrar Receita");
        Button btnDespesa = new Button("➖ Cadastrar Despesa");
        Button btnListar = new Button("📋 Listar Transações");
        Button btnSaldo = new Button("💰 Ver Saldo Atual");
        Button btnSair = new Button("❌ Sair");

        // Layout
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f4f4f4;");
        layout.getChildren().addAll(btnReceita, btnDespesa, btnListar, btnSaldo, btnSair);

        // Cena principal
        Scene scene = new Scene(layout, 350, 300);
        stage.setTitle("📊 Controle Financeiro Pessoal");
        stage.setScene(scene);
        stage.show();

        // Ações de navegação
        btnReceita.setOnAction(e -> new CadastroTransacaoUI().start(new Stage(), "Receita"));
        btnDespesa.setOnAction(e -> new CadastroTransacaoUI().start(new Stage(), "Despesa"));
        btnListar.setOnAction(e -> new ListaTransacoesUI().start(new Stage()));
        btnSaldo.setOnAction(e -> new SaldoUI().start(new Stage()));
        btnSair.setOnAction(e -> stage.close());
        
    }

    public static void main(String[] args) {
        launch();
    }
}
