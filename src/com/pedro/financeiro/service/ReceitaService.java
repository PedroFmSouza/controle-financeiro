package com.pedro.financeiro.service;

import com.pedro.financeiro.db.Database;
import com.pedro.financeiro.dao.TransacaoDAO;

import java.sql.*;
import java.time.LocalDate;

public class ReceitaService {

    public static void processarReceitasPendentes() {
        String sql = "SELECT id, categoria_id, valor, frequencia, repeticoes, data_inicio FROM receitas_recorrentes";

        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            LocalDate hoje = LocalDate.now();

            while (rs.next()) {
                int categoriaId = rs.getInt("categoria_id");
                double valor = rs.getDouble("valor");
                String freq = rs.getString("frequencia"); // diaria | semanal | mensal
                int repeticoes = rs.getInt("repeticoes");
                LocalDate inicio = LocalDate.parse(rs.getString("data_inicio"));

                for (int i = 0; i < repeticoes; i++) {
                    LocalDate dataLanc = switch (freq) {
                        case "diaria"  -> inicio.plusDays(i);
                        case "semanal" -> inicio.plusWeeks(i);
                        case "mensal"  -> inicio.plusMonths(i);
                        default -> inicio;
                    };

                    if (dataLanc.isAfter(hoje)) break; // só lança até hoje

                    // Descrição padrão para permitir o índice único evitar duplicatas
                    String descricao = "Receita recorrente";

                    // Insere se não existir (o índice único também protege)
                    TransacaoDAO.salvarTransacaoComData(descricao, categoriaId, valor, "receita", dataLanc);
                }
            }

            System.out.println("✅ Receitas recorrentes processadas.");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao processar receitas recorrentes: " + e.getMessage());
        }
    }
}
