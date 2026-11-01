package operaciones;

import datos.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestionBaccarat {
    private final List<Carta> baraja;
    private final Mano manoJugador;
    private final Mano manoBanca;

    public GestionBaccarat() {
        this.baraja = new ArrayList<>();
        this.manoJugador = new Mano();
        this.manoBanca = new Mano();
        inicializarBaraja();
    }

    public void inicializarBaraja() {
        baraja.clear();
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        for (Palo palo : Palo.values()) {
            for (String val : valores) {
                baraja.add(new Carta(palo, val));
            }
        }
        Collections.shuffle(baraja);
    }

    public Carta repartirCarta() {
        if (baraja.isEmpty()) {
            inicializarBaraja();
        }
        return baraja.remove(baraja.size() - 1);
    }

    public void repartirManosIniciales() {
        manoJugador.limpiar();
        manoBanca.limpiar();
        manoJugador.agregarCarta(repartirCarta());
        manoBanca.agregarCarta(repartirCarta());
        manoJugador.agregarCarta(repartirCarta());
        manoBanca.agregarCarta(repartirCarta());
    }

    public void ejecutarReglasTerceraCarta() {
        if (manoJugador.esNatural() || manoBanca.esNatural()) {
            return;
        }

        Carta terceraCartaJugador = null;
        if (manoJugador.calcularPuntaje() <= 5) {
            terceraCartaJugador = repartirCarta();
            manoJugador.agregarCarta(terceraCartaJugador);
        }

        int pBanca = manoBanca.calcularPuntaje();
        if (terceraCartaJugador == null) {
            if (pBanca <= 5) {
                manoBanca.agregarCarta(repartirCarta());
            }
        } else {
            int val3 = terceraCartaJugador.getValorBaccarat();
            if (pBanca <= 2) {
                manoBanca.agregarCarta(repartirCarta());
            } else if (pBanca == 3 && val3 != 8) {
                manoBanca.agregarCarta(repartirCarta());
            } else if (pBanca == 4 && (val3 >= 2 && val3 <= 7)) {
                manoBanca.agregarCarta(repartirCarta());
            } else if (pBanca == 5 && (val3 >= 4 && val3 <= 7)) {
                manoBanca.agregarCarta(repartirCarta());
            } else if (pBanca == 6 && (val3 == 6 || val3 == 7)) {
                manoBanca.agregarCarta(repartirCarta());
            }
        }
    }

    public String determinarGanador() {
        int pJugador = manoJugador.calcularPuntaje();
        int pBanca = manoBanca.calcularPuntaje();

        if (pJugador > pBanca) return "JUGADOR";
        if (pBanca > pJugador) return "BANCA";
        return "EMPATE";
    }

    public Mano getManoJugador() { return manoJugador; }
    public Mano getManoBanca() { return manoBanca; }
}
