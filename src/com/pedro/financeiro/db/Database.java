package com.pedro.financeiro.db;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:financeiro.db";

    public static Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC"); // garante driver
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite não encontrado: " + e.getMessage());
            // continua: DriverManager lançará SQLException se não achar driver
        }
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            // 1) criar tabela categorias (se não existir)
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS categorias (" +
                            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " nome TEXT UNIQUE NOT NULL" +
                            ")"
            );

            // 2) criar tabela transacoes com a estrutura desejada (se não existir)
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS transacoes (" +
                            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " descricao TEXT NOT NULL," +
                            " categoria_id INTEGER," +
                            " valor REAL NOT NULL," +
                            " tipo TEXT NOT NULL CHECK(tipo IN ('receita','despesa'))," +
                            " data TEXT NOT NULL," +
                            " FOREIGN KEY (categoria_id) REFERENCES categorias(id)" +
                            ")"
            );

            // 3) criar tabela receitas_recorrentes (se não existir)
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS receitas_recorrentes (" +
                            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " categoria_id INTEGER," +
                            " valor REAL NOT NULL," +
                            " frequencia TEXT NOT NULL," + // diaria|semanal|mensal
                            " repeticoes INTEGER NOT NULL," +
                            " data_inicio TEXT NOT NULL," +
                            " FOREIGN KEY (categoria_id) REFERENCES categorias(id)" +
                            ")"
            );

            // 4) MIGRAÇÃO: garantir colunas que podem faltar em DBs antigos
            // verifica e adiciona categoria_id em transacoes se faltar
            if (!colunaExiste(conn, "transacoes", "categoria_id")) {
                try {
                    stmt.execute("ALTER TABLE transacoes ADD COLUMN categoria_id INTEGER");
                    System.out.println("ℹ️ Coluna 'categoria_id' adicionada em 'transacoes'.");
                } catch (SQLException ex) {
                    System.err.println("❌ Falha ao adicionar coluna categoria_id: " + ex.getMessage());
                }
            }

            // verifica e adiciona categoria_id em receitas_recorrentes se faltar
            if (!colunaExiste(conn, "receitas_recorrentes", "categoria_id")) {
                try {
                    stmt.execute("ALTER TABLE receitas_recorrentes ADD COLUMN categoria_id INTEGER");
                    System.out.println("ℹ️ Coluna 'categoria_id' adicionada em 'receitas_recorrentes'.");
                } catch (SQLException ex) {
                    System.err.println("❌ Falha ao adicionar coluna categoria_id em receitas_recorrentes: " + ex.getMessage());
                }
            }

            // 5) Criar índice único (vai falhar se colunas não existirem — por isso roda depois)
            try {
                stmt.execute(
                        "CREATE UNIQUE INDEX IF NOT EXISTS idx_transacoes_unique " +
                                "ON transacoes(descricao, categoria_id, valor, tipo, data)"
                );
            } catch (SQLException ex) {
                System.err.println("❌ Não foi possível criar índice idx_transacoes_unique: " + ex.getMessage());
            }

            System.out.println("✅ Banco inicializado / migrado com sucesso.");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao inicializar banco: " + e.getMessage());
        }
    }

    // utilitário: verifica existência de coluna via PRAGMA table_info
    private static boolean colunaExiste(Connection conn, String tabela, String coluna) {
        String sql = "PRAGMA table_info(" + tabela + ")";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String nome = rs.getString("name");
                if (coluna.equalsIgnoreCase(nome)) return true;
            }
        } catch (SQLException e) {
            // se a tabela não existir, PRAGMA não retornará linhas — tratamos como "coluna não existe"
        }
        return false;
    }
}
