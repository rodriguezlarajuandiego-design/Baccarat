package datos;

import java.sql.Timestamp;

public class ReporteDiario {
    private int idPartida;
    private String nombreUsuario;
    private TipoApuesta apuesta;
    private double monto;
    private String resultado;
    private double ganancia;
    private Timestamp fecha;

    public ReporteDiario(int idPartida, String nombreUsuario, TipoApuesta apuesta, double monto, String resultado, double ganancia, Timestamp fecha) {
        this.idPartida = idPartida;
        this.nombreUsuario = nombreUsuario;
        this.apuesta = apuesta;
        this.monto = monto;
        this.resultado = resultado;
        this.ganancia = ganancia;
        this.fecha = fecha;
    }

    public int getIdPartida() { return idPartida; }
    public String getNombreUsuario() { return nombreUsuario; }
    public TipoApuesta getApuesta() { return apuesta; }
    public double getMonto() { return monto; }
    public String getResultado() { return resultado; }
    public double getGanancia() { return ganancia; }
    public Timestamp getFecha() { return fecha; }
}
