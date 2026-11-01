package pruebas;

import datos.*;
import operaciones.GestionBaccarat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PruebasBaccarat {

    private GestionBaccarat juego;

    @BeforeEach
    public void setUp() {
        juego = new GestionBaccarat();
    }

    @Test
    public void testValoresCartasBaccarat() {
        Carta as = new Carta(Palo.CORAZONES, "A");
        Carta rey = new Carta(Picas, "K");
        Carta siete = new Carta(Palo.DIAMANTES, "7");

        assertEquals(1, as.getValorBaccarat());
        assertEquals(0, rey.getValorBaccarat());
        assertEquals(7, siete.getValorBaccarat());
    }

    @Test
    public void testCalculoPuntajeMano() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(Palo.TREBOLES, "8"));
        mano.agregarCarta(new Carta(Palo.DIAMANTES, "7")); // 8 + 7 = 15 -> Módulo 10 = 5

        assertEquals(5, mano.calcularPuntaje());
    }

    @Test
    public void testManoNatural() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(Palo.CORAZONES, "9"));
        mano.agregarCarta(new Carta(Palo.PICAS, "K")); // 9 + 0 = 9

        assertTrue(mano.esNatural());
    }

    @Test
    public void testRepartoInicial() {
        juego.repartirManosIniciales();
        assertEquals(2, juego.getManoJugador().getCartas().size());
        assertEquals(2, juego.getManoBanca().getCartas().size());
    }
}
