package datos;

public class Carta {
    private final Palo palo;
    private final String valorNominal;

    public Carta(Palo palo, String valorNominal) {
        this.palo = palo;
        this.valorNominal = valorNominal;
    }

    public Palo getPalo() {
        return palo;
    }

    public String getValorNominal() {
        return valorNominal;
    }

    public int getValorBaccarat() {
        switch (valorNominal) {
            case "A": return 1;
            case "10": case "J": case "Q": case "K": return 0;
            default: return Integer.parseInt(valorNominal);
        }
    }

    @Override
    public String toString() {
        return "[" + valorNominal + " de " + palo + "]";
    }
}
