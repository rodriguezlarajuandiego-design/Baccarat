package vista;

import datos.*;
import operaciones.ConexionBD;
import operaciones.GestionBaccarat;

import java.util.List;
import java.util.Scanner;

public class VentanaBaccarat {
    private final Scanner scanner;
    private final ConexionBD conexionBD;
    private final GestionBaccarat juego;
    private Usuario usuarioActual;

    public VentanaBaccarat() {
        this.scanner = new Scanner(System.in);
        this.conexionBD = new ConexionBD();
        this.juego = new GestionBaccarat();
    }

    public void iniciar() {
        System.out.println("==========================================");
        System.out.println("   BIENVENIDO A BACCARAT APP - CONSOLA    ");
        System.out.println("==========================================");
        System.out.print("Ingrese su nombre de usuario: ");
        String nombre = scanner.nextLine();

        this.usuarioActual = conexionBD.obtenerOCrearUsuario(nombre);
        if (usuarioActual == null) {
            System.out.println("No se pudo conectar a la base de datos. Modo local temporal.");
            usuarioActual = new Usuario(1, nombre, 1000.00);
        }

        System.out.println("\n¡Hola " + usuarioActual.getNombre() + "! Saldo actual: $" + usuarioActual.getSaldo());

        boolean salir = false;
        while (!salir && usuarioActual.getSaldo() > 0) {
            mostrarMenu();
            int opc = Integer.parseInt(scanner.nextLine());
            switch (opc) {
                case 1:
                    jugarRonda(TipoApuesta.JUGADOR);
                    break;
                case 2:
                    jugarRonda(TipoApuesta.BANCA);
                    break;
                case 3:
                    jugarRonda(TipoApuesta.EMPATE);
                    break;
                case 4:
                    mostrarReporte();
                    break;
                case 5:
                    salir = true;
                    System.out.println("¡Gracias por jugar! Saldo final: $" + usuarioActual.getSaldo());
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n---------------- MENU PRINCIPAL ----------------");
        System.out.println("Saldo disponible: $" + usuarioActual.getSaldo());
        System.out.println("1. Apostar a JUGADOR (Pago 1:1)");
        System.out.println("2. Apostar a BANCA (Pago 0.95:1)");
        System.out.println("3. Apostar a EMPATE (Pago 8:1)");
        System.out.println("4. Ver Reporte de Historial");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void jugarRonda(TipoApuesta tipoApuesta) {
        System.out.print("Ingrese monto a apostar: ");
        double monto = Double.parseDouble(scanner.nextLine());

        if (monto <= 0 || monto > usuarioActual.getSaldo()) {
            System.out.println("Monto inválido.");
            return;
        }

        juego.repartirManosIniciales();
        juego.ejecutarReglasTerceraCarta();

        String resultado = juego.determinarGanador();

        System.out.println("\n--- RESULTADO DE LA MANO ---");
        System.out.println("Jugador (" + juego.getManoJugador().calcularPuntaje() + " pts): " + juego.getManoJugador().getCartas());
        System.out.println("Banca (" + juego.getManoBanca().calcularPuntaje() + " pts): " + juego.getManoBanca().getCartas());
        System.out.println("Ganador de la ronda: " + resultado);

        double ganancia = 0;
        String estado;

        if (tipoApuesta.name().equals(resultado)) {
            estado = "GANADO";
            if (tipoApuesta == TipoApuesta.JUGADOR) ganancia = monto * 1.0;
            else if (tipoApuesta == TipoApuesta.BANCA) ganancia = monto * 0.95;
            else if (tipoApuesta == TipoApuesta.EMPATE) ganancia = monto * 8.0;

            usuarioActual.setSaldo(usuarioActual.getSaldo() + ganancia);
            System.out.println("¡GANASTE! Ganancia: $" + ganancia);
        } else if (resultado.equals("EMPATE") && tipoApuesta != TipoApuesta.EMPATE) {
            estado = "EMPATE";
            ganancia = 0;
            System.out.println("Empate. Se reembolsa tu apuesta.");
        } else {
            estado = "PERDIDO";
            ganancia = -monto;
            usuarioActual.setSaldo(usuarioActual.getSaldo() - monto);
            System.out.println("Has perdido $" + monto);
        }

        conexionBD.actualizarSaldo(usuarioActual.getId(), usuarioActual.getSaldo());
        conexionBD.registrarPartida(usuarioActual.getId(), tipoApuesta, monto, resultado, estado, ganancia);
    }

    private void mostrarReporte() {
        System.out.println("\n================ HISTORIAL DE PARTIDAS ================");
        List<ReporteDiario> lista = conexionBD.obtenerReporteDiario();
        if (lista.isEmpty()) {
            System.out.println("No hay partidas registradas en la base de datos.");
        } else {
            for (ReporteDiario r : lista) {
                System.out.printf("ID: %d | Jugador: %s | Apuesta: %s | Monto: $%.2f | Resultado: %s | Ganancia: $%.2f | Fecha: %s%n",
                        r.getIdPartida(), r.getNombreUsuario(), r.getApuesta(), r.getMonto(), r.getResultado(), r.getGanancia(), r.getFecha());
            }
        }
    }

    public static void main(String[] args) {
        VentanaBaccarat ventana = new VentanaBaccarat();
        ventana.iniciar();
    }
}
