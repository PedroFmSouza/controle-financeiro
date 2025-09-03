package com.pedro.financeiro.dao;

import com.pedro.financeiro.db.Database;
import java.sql.*;
import java.time.LocalDate;

public class ReceitaRecorrenteDAO {
    public static void salvarReceita(int categoriaId, double valor, String frequencia, int repeticoes, LocalDate dataInicio) {
        String sql = "INSERT INTO receitas_recorrentes(categoria_id, valor, frequencia, repeticoes, data_inicio) VALUES(?,?,?,?,?)";
        try (Connection conn = Database.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoriaId);
            ps.setDouble(2, valor);
            ps.setString(3, frequencia);
            ps.setInt(4, repeticoes);
            ps.setString(5, dataInicio.toString());
            ps.executeUpdate();
            System.out.println("✅ Receita recorrente salva!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar receita recorrente: " + e.getMessage());
        }
    }
}
