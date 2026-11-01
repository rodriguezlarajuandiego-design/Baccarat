package datos;

import java.util.ArrayList;
import java.util.List;

public class Mano {
    private final List<Carta> cartas;

    public Mano() {
        this.cartas = new ArrayList<>();
    }

    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public int calcularPuntaje() {
        int suma = 0;
        for (Carta c : cartas) {
            suma += c.getValorBaccarat();
        }
        return suma % 10;
    }

    public boolean esNatural() {
        return cartas.size() == 2 && (calcularPuntaje() == 8 || calcularPuntaje() == 9);
    }

    public void limpiar() {
        cartas.clear();
    }
}
