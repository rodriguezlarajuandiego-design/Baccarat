package vista;

import datos.Carta;
import datos.Palo;
import java.util.List;

public class DibujadorAscii {

    // Convierte el Palo a su símbolo gráfico correspondiente
    public static String obtenerSimboloPalo(Palo palo) {
        if (palo == null) return "?";
        switch (palo) {
            case CORAZONES: return "♥";
            case DIAMANTES: return "♦";
            case TREBOLES:  return "♣";
            case PICAS:     return "♠";
            default:        return "?";
        }
    }

    // Dibuja una lista de cartas una al lado de la otra en formato de bloque ASCII
    public static void dibujarCartas(List<Carta> mano) {
        if (mano == null || mano.isEmpty()) {
            System.out.println("(No hay cartas para mostrar)");
            return;
        }

        // Fila 1: Bordes superiores de las cartas
        for (int i = 0; i < mano.size(); i++) {
            System.out.print("┌─────────┐ ");
        }
        System.out.println();


// Fila 2: Valor superior izquierdo
        for (Carta c : mano) {
            String val = String.format("%-2s", c.getValorNominal()); // <--- Aquí
            System.out.print("│ " + val + "      │ ");
        }
        System.out.println();

        // Fila 3: Símbolo del palo centrado
        for (Carta c : mano) {
            String sim = obtenerSimboloPalo(c.getPalo());
            System.out.print("│    " + sim + "    │ ");
        }
        System.out.println();

        // Fila 4: Valor inferior derecho
        for (Carta c : mano) {
            String val = String.format("%2s", c.getValorNominal()); // <--- Y aquí
            System.out.print("│      " + val + " │ ");
        }
        System.out.println();

        // Fila 5: Bordes inferiores de las cartas
        for (int i = 0; i < mano.size(); i++) {
            System.out.print("└─────────┘ ");
        }
        System.out.println();
    }
}
