package datos;

public class Usuario {
    private int id;
    private String nombre;
    private double saldo;

    public Usuario(int id, String nombre, double saldo) {
        this.id = id;
        this.nombre = nombre;
        this.saldo = saldo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
