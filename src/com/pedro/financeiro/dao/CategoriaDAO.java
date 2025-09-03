package com.pedro.financeiro.dao;

import com.pedro.financeiro.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public static void salvarCategoria(String nome) {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO categorias(nome) VALUES(?)")) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar categoria: " + e.getMessage());
        }
    }

    public static String getNomeById(int id) {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT nome FROM categorias WHERE id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("nome");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar nome da categoria: " + e.getMessage());
        }
        return null;
    }

    public static List<String> listarCategorias() {
        List<String> categorias = new ArrayList<>();
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nome FROM categorias ORDER BY nome")) {
            while (rs.next()) {
                categorias.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao listar categorias: " + e.getMessage());
        }
        return categorias;
    }

    public static Integer getCategoriaId(String nome) {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM categorias WHERE nome = ?")) {
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao buscar categoria: " + e.getMessage());
        }
        return null;
    }
}
