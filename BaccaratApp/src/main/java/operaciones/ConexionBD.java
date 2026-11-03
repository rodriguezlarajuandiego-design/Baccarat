package operaciones;

import datos.Usuario;
import datos.TipoApuesta;
import datos.ReporteDiario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexionBD {
    private static final String URL = "jdbc:postgresql://db.svkrucvysievwqovmjm.supabase.co:5432/postgres?sslmode=require";
    private static final String USUARIO = "postgres";
    private static final String CLAVE = "Juan1150185216";

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    public Usuario obtenerOCrearUsuario(String nombre) {
        String querySelect = "SELECT id, nombre, saldo FROM usuario WHERE nombre = ?";
        String queryInsert = "INSERT INTO usuario (nombre, saldo) VALUES (?, 1000.00) RETURNING id, saldo";

        try (Connection conn = conectar()) {

            try (PreparedStatement pstmt = conn.prepareStatement(querySelect)) {
                pstmt.setString(1, nombre);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("saldo"));
                }
            }
            try (PreparedStatement pstmtIns = conn.prepareStatement(queryInsert)) {
                pstmtIns.setString(1, nombre);
                ResultSet rs = pstmtIns.executeQuery();
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), nombre, rs.getDouble("saldo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en BD: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void actualizarSaldo(int idUsuario, double nuevoSaldo) {
        String sql = "UPDATE usuarios SET saldo = ? WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setInt(2, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar saldo: " + e.getMessage());
        }
    }

    public void registrarPartida(int idUsuario, TipoApuesta apuesta, double monto, String resultado, String estado, double ganancia) {
        String sql = "INSERT INTO historial_partidas (usuario_id, apuesta_tipo, monto_apostado, resultado_juego, estado_apuesta, ganancia) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, apuesta.name());
            pstmt.setDouble(3, monto);
            pstmt.setString(4, resultado);
            pstmt.setString(5, estado);
            pstmt.setDouble(6, ganancia);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar partida: " + e.getMessage());
        }
    }

    public List<ReporteDiario> obtenerReporteDiario() {
        List<ReporteDiario> lista = new ArrayList<>();
        String sql = "SELECT h.id, u.nombre, h.apuesta_tipo, h.monto_apostado, h.resultado_juego, h.ganancia, h.fecha " +
                     "FROM historial_partidas h JOIN usuarios u ON h.usuario_id = u.id ORDER BY h.fecha DESC";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new ReporteDiario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        TipoApuesta.valueOf(rs.getString("apuesta_tipo")),
                        rs.getDouble("monto_apostado"),
                        rs.getString("resultado_juego"),
                        rs.getDouble("ganancia"),
                        rs.getTimestamp("fecha")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar reporte: " + e.getMessage());
        }
        return lista;
    }
}
