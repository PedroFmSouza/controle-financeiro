package com.pedro.financeiro.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ListaTransacoesUI {

    public void start(Stage stage) {
        TableView<Transacao> tabela = new TableView<>();
        ObservableList<Transacao> dados = FXCollections.observableArrayList(
                new Transacao("Receita", "Salário", 2500.00),
                new Transacao("Despesa", "Aluguel", 1200.00),
                new Transacao("Despesa", "Mercado", 450.00)
        );

        TableColumn<Transacao, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Transacao, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Transacao, Double> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        tabela.getColumns().addAll(colTipo, colCategoria, colValor);
        tabela.setItems(dados);

        Scene scene = new Scene(tabela, 400, 300);
        stage.setTitle("📋 Lista de Transações");
        stage.setScene(scene);
        stage.show();
    }

    public static class Transacao {
        private final String tipo;
        private final String categoria;
        private final double valor;

        public Transacao(String tipo, String categoria, double valor) {
            this.tipo = tipo;
            this.categoria = categoria;
            this.valor = valor;
        }

        public String getTipo() { return tipo; }
        public String getCategoria() { return categoria; }
        public double getValor() { return valor; }
    }
}
