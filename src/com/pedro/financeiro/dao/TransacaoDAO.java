package com.pedro.financeiro.dao;

import com.pedro.financeiro.db.Database;
import java.sql.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;

public class TransacaoDAO {

    public static void salvarTransacao(String descricao, int categoriaId, double valor, String tipo) {
        salvarTransacaoComData(descricao, categoriaId, valor, tipo, LocalDate.now());
    }

    public static void salvarTransacaoComData(String descricao, int categoriaId, double valor, String tipo, LocalDate data) {
        String sql = "INSERT OR IGNORE INTO transacoes(descricao, categoria_id, valor, tipo, data) VALUES(?,?,?,?,?)";
        try (Connection conn = Database.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            ps.setInt(2, categoriaId);
            ps.setDouble(3, valor);
            ps.setString(4, tipo);
            ps.setString(5, data.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Erro ao salvar transação: " + e.getMessage());
        }
    }

    public static Map<String, Double> somarPorCategoria(String tipo) {
        Map<String, Double> mapa = new HashMap<>();
        String sql = """
        SELECT c.nome, SUM(t.valor) AS total
        FROM transacoes t
        JOIN categorias c ON t.categoria_id = c.id
        WHERE t.tipo = ?
        GROUP BY c.nome
    """;

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mapa.put(rs.getString("nome"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao somar por categoria: " + e.getMessage());
        }
        return mapa;
    }


    public static boolean existe(String descricao, int categoriaId, double valor, String tipo, LocalDate data) {
        String sql = "SELECT 1 FROM transacoes WHERE descricao=? AND categoria_id=? AND valor=? AND tipo=? AND data=? LIMIT 1";
        try (Connection conn = Database.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            ps.setInt(2, categoriaId);
            ps.setDouble(3, valor);
            ps.setString(4, tipo);
            ps.setString(5, data.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao verificar transação: " + e.getMessage());
            return false;
        }
    }
}
