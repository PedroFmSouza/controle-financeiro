package com.pedro.financeiro.ui;

import com.pedro.financeiro.dao.CategoriaDAO;
import com.pedro.financeiro.dao.ReceitaRecorrenteDAO;
import com.pedro.financeiro.dao.TransacaoDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;


public class MainUI extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showMenu();
        stage.setTitle("💰 Controle Financeiro");
        stage.show();
        com.pedro.financeiro.db.Database.initialize();

        com.pedro.financeiro.service.ReceitaService.processarReceitasPendentes();

        showMenu();
        stage.setTitle("💰 Controle Financeiro");
        stage.show();
    }

    // ================= MENU PRINCIPAL =================
    private void showMenu() {
        Text titulo = new Text("💰 Controle Financeiro");
        titulo.setFont(Font.font("Arial", 28));
        titulo.setFill(Color.WHITE);

        Button btnTransacoes = new Button("📋 Gerenciar Transações");
        Button btnRegistrarDespesa = new Button("➖ Registrar Despesa");
        Button btnRegistrarReceita = new Button("➕ Registrar Receita");
        Button btnGastos = new Button("💸 Meus Gastos");
        Button btnRelatorios = new Button("📊 Relatórios");
        Button btnSair = new Button("🚪 Sair");

        estilizarBotao(btnTransacoes);
        estilizarBotao(btnRegistrarDespesa);
        estilizarBotao(btnRegistrarReceita);
        estilizarBotao(btnGastos);
        estilizarBotao(btnRelatorios);
        estilizarBotao(btnSair);

        btnTransacoes.setOnAction(e -> showTransacoes());
        btnRegistrarDespesa.setOnAction(e -> showRegistrarDespesa());
        btnRegistrarReceita.setOnAction(e -> showRegistrarReceita());
        btnGastos.setOnAction(e -> showMeusGastos());
        btnRelatorios.setOnAction(e -> showRelatorios());
        btnSair.setOnAction(e -> primaryStage.close());

        VBox layout = new VBox(20, titulo, btnTransacoes, btnRegistrarDespesa, btnRegistrarReceita, btnGastos, btnRelatorios, btnSair);

        layout.setStyle("-fx-background-color: #0A1A2F;");
        layout.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(layout, 700, 500));
    }
    private void showRegistrarDespesa() {
        Text titulo = new Text("➖ Registrar Despesa");
        titulo.setFont(Font.font("Arial", 24));
        titulo.setFill(Color.WHITE);

        TextField descricaoField = new TextField();
        descricaoField.setPromptText("Descrição da despesa");

        TextField valorField = new TextField();
        valorField.setPromptText("Valor (R$)");

        ComboBox<String> categoriaBox = new ComboBox<>();
        categoriaBox.getItems().addAll(CategoriaDAO.listarCategorias());
        categoriaBox.setEditable(true); // permite digitar novas categorias
        categoriaBox.setPromptText("Categoria");

        Button btnSalvar = new Button("Salvar Despesa");
        estilizarBotao(btnSalvar);

        Button btnVoltar = new Button("⬅ Voltar");
        estilizarBotao(btnVoltar);
        btnVoltar.setOnAction(e -> showMenu());

        btnSalvar.setOnAction(e -> {
            try {
                String descricao = descricaoField.getText();
                String categoria = categoriaBox.getValue();
                double valor = Double.parseDouble(valorField.getText());

                if (descricao.isEmpty() || categoria == null || valor <= 0) {
                    System.out.println("⚠ Preencha todos os campos corretamente.");
                    return;
                }

                CategoriaDAO.salvarCategoria(categoria);
                Integer catId = CategoriaDAO.getCategoriaId(categoria);

                if (catId != null) {
                    TransacaoDAO.salvarTransacao(descricao, catId, valor, "despesa");
                    System.out.println("✅ Despesa registrada com sucesso!");
                    showTransacoes(); // volta para a lista atualizada
                }
            }catch (Exception ex) {
                System.out.println("⚠ Erro ao salvar despesa: " + ex.getMessage());
            }

        });

        VBox layout = new VBox(15, titulo, descricaoField, valorField, categoriaBox, btnSalvar, btnVoltar);
        layout.setStyle("-fx-background-color: #0A1A2F;");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 700, 500));
    }

    private void showRegistrarReceita() {
        Text titulo = new Text("➕ Registrar Receita Recorrente");
        titulo.setFont(Font.font("Arial", 24));
        titulo.setFill(Color.WHITE);

        TextField valorField = new TextField();
        valorField.setPromptText("Valor (R$)");

        ComboBox<String> categoriaBox = new ComboBox<>();
        categoriaBox.getItems().addAll(CategoriaDAO.listarCategorias());
        categoriaBox.setEditable(true);
        categoriaBox.setPromptText("Categoria");

        ComboBox<String> frequenciaBox = new ComboBox<>();
        frequenciaBox.getItems().addAll("diaria", "semanal", "mensal");
        frequenciaBox.setPromptText("Frequência");

        TextField repeticoesField = new TextField();
        repeticoesField.setPromptText("Número de repetições");

        DatePicker dataInicioPicker = new DatePicker();
        dataInicioPicker.setPromptText("Data de início");

        Button btnSalvar = new Button("Salvar Receita");
        estilizarBotao(btnSalvar);

        Button btnVoltar = new Button("⬅ Voltar");
        estilizarBotao(btnVoltar);
        btnVoltar.setOnAction(e -> showMenu());

        btnSalvar.setOnAction(e -> {
            try {
                double valor = Double.parseDouble(valorField.getText());
                String categoria = categoriaBox.getValue();
                String frequencia = frequenciaBox.getValue();
                int repeticoes = Integer.parseInt(repeticoesField.getText());
                LocalDate dataInicio = dataInicioPicker.getValue();

                CategoriaDAO.salvarCategoria(categoria);
                Integer catId = CategoriaDAO.getCategoriaId(categoria);

                if (catId != null) {
                    ReceitaRecorrenteDAO.salvarReceita(catId, valor, frequencia, repeticoes, dataInicio);
                    showMenu();
                }
            } catch (Exception ex) {
                System.out.println("⚠ Erro ao salvar receita: " + ex.getMessage());
            }
        });

        VBox layout = new VBox(15, titulo, valorField, categoriaBox, frequenciaBox, repeticoesField, dataInicioPicker, btnSalvar, btnVoltar);
        layout.setStyle("-fx-background-color: #0A1A2F;");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 700, 500));
    }
    // ================= TELA DE TRANSAÇÕES =================


    private void showTransacoes() {
        Text titulo = new Text("📋 Minhas Transações");
        titulo.setFont(Font.font("Arial", 24));
        titulo.setFill(Color.WHITE);

        TableView<Transacao> tabela = new TableView<>();
        tabela.setItems(getMockTransacoes());

        TableColumn<Transacao, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Transacao, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Transacao, Double> colValor = new TableColumn<>("Valor (R$)");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        tabela.getColumns().addAll(colDescricao, colCategoria, colValor);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button btnVoltar = new Button("⬅ Voltar ao Menu");
        estilizarBotao(btnVoltar);
        btnVoltar.setOnAction(e -> showMenu());

        VBox layout = new VBox(20, titulo, tabela, btnVoltar);
        layout.setStyle("-fx-background-color: #0A1A2F;");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 700, 500));
    }

    // ================= MOCK DE TRANSAÇÕES =================
    private ObservableList<Transacao> getMockTransacoes() {
        return FXCollections.observableArrayList(
                new Transacao("Mercado", "Alimentação", 450.0),
                new Transacao("Uber", "Transporte", 80.0),
                new Transacao("Netflix", "Entretenimento", 40.0),
                new Transacao("Academia", "Saúde", 120.0)
        );
    }

    // ================= TELA DE RELATÓRIOS =================
    private void showRelatorios() {
        Text titulo = new Text("📊 Relatórios Financeiros");
        titulo.setFont(Font.font("Arial", 24));
        titulo.setFill(Color.WHITE);

        Button btnVoltar = new Button("⬅ Voltar ao Menu");
        estilizarBotao(btnVoltar);
        btnVoltar.setOnAction(e -> showMenu());

        VBox layout = new VBox(20, titulo, btnVoltar);
        layout.setStyle("-fx-background-color: #0A1A2F;");
        layout.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(layout, 700, 500));
    }

    // ================= NOVA VIEW: MEUS GASTOS =================
    private void showMeusGastos() {
        Text titulo = new Text("💸 Meus Gastos (por categoria)");
        titulo.setFont(Font.font("Arial", 24));
        titulo.setFill(Color.WHITE);

        // consulta categorias e soma valores
        Map<String, Double> dados = TransacaoDAO.somarPorCategoria("despesa");

        PieChart grafico = new PieChart();
        for (var entry : dados.entrySet()) {
            String categoria = entry.getKey();
            double valor = entry.getValue();
            grafico.getData().add(new PieChart.Data(categoria + " - R$ " + valor, valor));
        }

        grafico.setLegendVisible(true);
        grafico.setLabelsVisible(true);

        // Ajustar visibilidade das labels
        grafico.setStyle("-fx-pie-label-visible: true; -fx-font-size: 16px; -fx-text-fill: white;");

        Button btnVoltar = new Button("⬅ Voltar ao Menu");
        estilizarBotao(btnVoltar);
        btnVoltar.setOnAction(e -> showMenu());

        VBox layout = new VBox(20, titulo, grafico, btnVoltar);
        layout.setStyle("-fx-background-color: #0A1A2F;");
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        primaryStage.setScene(new Scene(layout, 700, 500));
    }

    // ================= ESTILO PADRÃO DOS BOTÕES =================
    private void estilizarBotao(Button btn) {
        btn.setFont(Font.font("Arial", 18));
        btn.setStyle(
                "-fx-background-color: #1E3A5F; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 10 20;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #2E5C8A; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 10 20;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #1E3A5F; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 10 20;"
        ));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
