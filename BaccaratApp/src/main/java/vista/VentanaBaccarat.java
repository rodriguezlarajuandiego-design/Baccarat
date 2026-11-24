package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VentanaBaccarat extends JFrame {

    private String nombreUsuario;
    private double saldo = 1000.00;
    private double montoApuesta = 50.00;
    private String apuestaSeleccionada = "";

    private JLabel lblSaldo;
    private JLabel lblApuestaActual;
    private JLabel lblEstado;
    private JLabel lblPuntosJugador;
    private JLabel lblPuntosBanquero;

    private JPanel contenedorCartasJugador;
    private JPanel contenedorCartasBanquero;
    private PanelGraficoHistorial panelGrafico;

    private final List<String> historialResultados = new ArrayList<>();
    private final Random random = new Random();

    // Colores Casino
    private final Color COLOR_FONDO = new Color(11, 29, 19);
    private final Color COLOR_FIELTRO = new Color(20, 64, 43);
    private final Color COLOR_DORADO = new Color(212, 175, 55);
    private final Color COLOR_PANEL_OSCURO = new Color(15, 23, 42);

    public VentanaBaccarat() {
        pedirNombreUsuario();

        setTitle("BaccaratApp - Casino Virtual (" + nombreUsuario + ")");
        setSize(1020, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(15, 20, 15, 20));

        // 1. BARRA SUPERIOR (Usuario, Saldo y Apuesta)
        panelPrincipal.add(crearBarraSuperior(), BorderLayout.NORTH);

        // 2. CENTRO: MESA VERDE CON CARTAS GRÁFICAS + PANEL LATERAL DE GRÁFICO
        JPanel centroContenedor = new JPanel(new BorderLayout(15, 0));
        centroContenedor.setOpaque(false);

        centroContenedor.add(crearMesaDeJuego(), BorderLayout.CENTER);
        centroContenedor.add(crearPanelLateralGrafico(), BorderLayout.EAST);

        panelPrincipal.add(centroContenedor, BorderLayout.CENTER);

        // 3. ZONA INFERIOR: FICHAS, BOTONES DE APUESTA Y REPARTIR
        panelPrincipal.add(crearPanelControles(), BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void pedirNombreUsuario() {
        JPanel panelRegistro = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel lbl = new JLabel("Bienvenido a BaccaratApp. Ingresa tu nombre de jugador:");
        JTextField txtNombre = new JTextField();
        panelRegistro.add(lbl);
        panelRegistro.add(txtNombre);

        int opcion = JOptionPane.showConfirmDialog(
                null, panelRegistro, "Registro de Jugador",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION && !txtNombre.getText().trim().isEmpty()) {
            nombreUsuario = txtNombre.getText().trim();
        } else {
            nombreUsuario = "Jugador 1";
        }
    }

    private JPanel crearBarraSuperior() {
        JPanel barra = new JPanel(new GridLayout(1, 3));
        barra.setBackground(COLOR_PANEL_OSCURO);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_DORADO, 1),
                new EmptyBorder(12, 20, 12, 20)
        ));

        JLabel lblJugador = new JLabel("JUGADOR: " + nombreUsuario.toUpperCase());
        lblJugador.setForeground(Color.WHITE);
        lblJugador.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblApuestaActual = new JLabel("FICHA ACTUAL: $" + String.format("%.0f", montoApuesta), SwingConstants.CENTER);
        lblApuestaActual.setForeground(COLOR_DORADO);
        lblApuestaActual.setFont(new Font("Segoe UI", Font.BOLD, 14));

        lblSaldo = new JLabel("SALDO: $" + String.format("%.2f", saldo), SwingConstants.RIGHT);
        lblSaldo.setForeground(new Color(74, 222, 128));
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 15));

        barra.add(lblJugador);
        barra.add(lblApuestaActual);
        barra.add(lblSaldo);
        return barra;
    }

    private JPanel crearMesaDeJuego() {
        JPanel mesa = new JPanel(new GridLayout(1, 2, 20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_FIELTRO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(COLOR_DORADO);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 40, 40);
            }
        };
        mesa.setOpaque(false);
        mesa.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Lado Jugador
        JPanel ladoJugador = crearZonaCartas("JUGADOR", true);
        // Lado Banquero
        JPanel ladoBanquero = crearZonaCartas("BANQUERO", false);

        mesa.add(ladoJugador);
        mesa.add(ladoBanquero);
        return mesa;
    }

    private JPanel crearZonaCartas(String titulo, boolean esJugador) {
        JPanel zona = new JPanel(new BorderLayout(0, 10));
        zona.setBackground(new Color(0, 0, 0, 65));
        zona.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 175, 55, 130), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_DORADO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel contenedorCartas = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 25));
        contenedorCartas.setOpaque(false);

        // Cartas boca abajo iniciales
        contenedorCartas.add(new CartaGrafica("?", "", false, true));
        contenedorCartas.add(new CartaGrafica("?", "", false, true));

        JLabel lblPuntos = new JLabel("Puntos: -", SwingConstants.CENTER);
        lblPuntos.setForeground(Color.WHITE);
        lblPuntos.setFont(new Font("Segoe UI", Font.BOLD, 15));

        if (esJugador) {
            contenedorCartasJugador = contenedorCartas;
            lblPuntosJugador = lblPuntos;
        } else {
            contenedorCartasBanquero = contenedorCartas;
            lblPuntosBanquero = lblPuntos;
        }

        zona.add(lblTitulo, BorderLayout.NORTH);
        zona.add(contenedorCartas, BorderLayout.CENTER);
        zona.add(lblPuntos, BorderLayout.SOUTH);
        return zona;
    }

    private JPanel crearPanelLateralGrafico() {
        JPanel lateral = new JPanel(new BorderLayout(0, 10));
        lateral.setPreferredSize(new Dimension(250, 0));
        lateral.setBackground(COLOR_PANEL_OSCURO);
        lateral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_DORADO, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titulo = new JLabel("ESTADÍSTICAS EN VIVO", SwingConstants.CENTER);
        titulo.setForeground(COLOR_DORADO);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 13));

        panelGrafico = new PanelGraficoHistorial();

        lateral.add(titulo, BorderLayout.NORTH);
        lateral.add(panelGrafico, BorderLayout.CENTER);
        return lateral;
    }

    private JPanel crearPanelControles() {
        JPanel controles = new JPanel(new BorderLayout(0, 12));
        controles.setOpaque(false);

        // Fila 1: Fichas de casino
        JPanel panelFichas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelFichas.setOpaque(false);
        JLabel lblFichas = new JLabel("Valor de Ficha: ");
        lblFichas.setForeground(Color.WHITE);
        lblFichas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelFichas.add(lblFichas);

        panelFichas.add(crearBotonFicha(25, new Color(59, 130, 246)));
        panelFichas.add(crearBotonFicha(50, new Color(16, 185, 129)));
        panelFichas.add(crearBotonFicha(100, new Color(239, 68, 68)));
        panelFichas.add(crearBotonFicha(500, new Color(168, 85, 247)));

        // Fila 2: Zonas de Apuesta
        JPanel panelApuestas = new JPanel(new GridLayout(1, 3, 15, 0));
        panelApuestas.setOpaque(false);

        JButton btnJugador = crearBotonApuesta("APOSTAR JUGADOR (1:1)", new Color(30, 58, 138));
        JButton btnEmpate = crearBotonApuesta("APOSTAR EMPATE (8:1)", new Color(20, 83, 45));
        JButton btnBanquero = crearBotonApuesta("APOSTAR BANQUERO (0.95:1)", new Color(127, 29, 29));

        btnJugador.addActionListener(e -> seleccionarApuesta("JUGADOR"));
        btnEmpate.addActionListener(e -> seleccionarApuesta("EMPATE"));
        btnBanquero.addActionListener(e -> seleccionarApuesta("BANQUERO"));

        panelApuestas.add(btnJugador);
        panelApuestas.add(btnEmpate);
        panelApuestas.add(btnBanquero);

        // Fila 3: Estado y Botón Repartir
        JPanel panelAccion = new JPanel(new BorderLayout());
        panelAccion.setOpaque(false);

        lblEstado = new JLabel("1. Elige el valor de tu ficha  |  2. Selecciona dónde apostar  |  3. Reparte las cartas");
        lblEstado.setForeground(COLOR_DORADO);
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton btnRepartir = new JButton("REPARTIR CARTAS");
        btnRepartir.setBackground(COLOR_DORADO);
        btnRepartir.setForeground(Color.BLACK);
        btnRepartir.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRepartir.setFocusPainted(false);
        btnRepartir.setPreferredSize(new Dimension(210, 42));
        btnRepartir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRepartir.addActionListener(e -> ejecutarRonda());

        panelAccion.add(lblEstado, BorderLayout.CENTER);
        panelAccion.add(btnRepartir, BorderLayout.EAST);

        controles.add(panelFichas, BorderLayout.NORTH);
        controles.add(panelApuestas, BorderLayout.CENTER);
        controles.add(panelAccion, BorderLayout.SOUTH);

        return controles;
    }

    private JButton crearBotonFicha(int valor, Color color) {
        JButton ficha = new JButton("$" + valor) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(5, 5, getWidth() - 10, getHeight() - 10);
                super.paintComponent(g);
            }
        };
        ficha.setPreferredSize(new Dimension(56, 56));
        ficha.setContentAreaFilled(false);
        ficha.setBorderPainted(false);
        ficha.setFocusPainted(false);
        ficha.setForeground(Color.WHITE);
        ficha.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ficha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ficha.addActionListener(e -> {
            montoApuesta = valor;
            lblApuestaActual.setText("FICHA ACTUAL: $" + valor);
        });
        return ficha;
    }

    private JButton crearBotonApuesta(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 45));
        btn.setBorder(BorderFactory.createLineBorder(COLOR_DORADO, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void seleccionarApuesta(String tipo) {
        apuestaSeleccionada = tipo;
        lblEstado.setText("Apuesta lista: $" + String.format("%.0f", montoApuesta) + " al " + tipo + ". ¡Presiona REPARTIR CARTAS!");
    }

    private void ejecutarRonda() {
        if (apuestaSeleccionada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero selecciona JUGADOR, EMPATE o BANQUERO.", "Falta Apuesta", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (montoApuesta > saldo) {
            JOptionPane.showMessageDialog(this, "No tienes saldo suficiente para esa ficha.", "Saldo Insuficiente", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generar cartas aleatorias visuales
        CartaData cJ1 = generarCartaAleatoria();
        CartaData cJ2 = generarCartaAleatoria();
        CartaData cB1 = generarCartaAleatoria();
        CartaData cB2 = generarCartaAleatoria();

        int ptsJugador = (cJ1.valorBaccarat + cJ2.valorBaccarat) % 10;
        int ptsBanquero = (cB1.valorBaccarat + cB2.valorBaccarat) % 10;

        // Actualizar cartas gráficas en pantalla
        contenedorCartasJugador.removeAll();
        contenedorCartasJugador.add(new CartaGrafica(cJ1.etiqueta, cJ1.palo, cJ1.esRoja, false));
        contenedorCartasJugador.add(new CartaGrafica(cJ2.etiqueta, cJ2.palo, cJ2.esRoja, false));
        lblPuntosJugador.setText("Puntos: " + ptsJugador);

        contenedorCartasBanquero.removeAll();
        contenedorCartasBanquero.add(new CartaGrafica(cB1.etiqueta, cB1.palo, cB1.esRoja, false));
        contenedorCartasBanquero.add(new CartaGrafica(cB2.etiqueta, cB2.palo, cB2.esRoja, false));
        lblPuntosBanquero.setText("Puntos: " + ptsBanquero);

        // Determinar ganador
        String ganador;
        if (ptsJugador > ptsBanquero) {
            ganador = "JUGADOR";
        } else if (ptsBanquero > ptsJugador) {
            ganador = "BANQUERO";
        } else {
            ganador = "EMPATE";
        }

        historialResultados.add(ganador);
        panelGrafico.repaint();

        // Calcular pago
        saldo -= montoApuesta;
        if (apuestaSeleccionada.equals(ganador)) {
            double ganancia = ganador.equals("EMPATE") ? montoApuesta * 9 : (ganador.equals("BANQUERO") ? montoApuesta * 1.95 : montoApuesta * 2);
            saldo += ganancia;
            lblEstado.setText("¡GANASTE! Salió " + ganador + ". Nuevo saldo: $" + String.format("%.2f", saldo));
        } else {
            lblEstado.setText("Perdiste la ronda. Ganó " + ganador + ". Vuelve a apostar.");
        }

        lblSaldo.setText("SALDO: $" + String.format("%.2f", saldo));
        revalidate();
        repaint();
    }

    private CartaData generarCartaAleatoria() {
        String[] etiquetas = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        int[] valores = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0};
        String[] palos = {"♥", "♦", "♠", "♣"};

        int idx = random.nextInt(etiquetas.length);
        int paloIdx = random.nextInt(palos.length);
        boolean esRoja = (paloIdx == 0 || paloIdx == 1);

        return new CartaData(etiquetas[idx], palos[paloIdx], valores[idx], esRoja);
    }

    private static class CartaData {
        String etiqueta;
        String palo;
        int valorBaccarat;
        boolean esRoja;

        CartaData(String etiqueta, String palo, int valorBaccarat, boolean esRoja) {
            this.etiqueta = etiqueta;
            this.palo = palo;
            this.valorBaccarat = valorBaccarat;
            this.esRoja = esRoja;
        }
    }

    // Componente personalizado que dibuja una carta de póker realista
    private static class CartaGrafica extends JPanel {
        private final String numero;
        private final String palo;
        private final boolean roja;
        private final boolean oculta;

        CartaGrafica(String numero, String palo, boolean roja, boolean oculta) {
            this.numero = numero;
            this.palo = palo;
            this.roja = roja;
            this.oculta = oculta;
            setPreferredSize(new Dimension(95, 135));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Sombra
            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRoundRect(4, 4, getWidth() - 6, getHeight() - 6, 14, 14);

            if (oculta) {
                g2.setColor(new Color(153, 27, 27));
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 14, 14);
                g2.setColor(new Color(212, 175, 55));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(5, 5, getWidth() - 16, getHeight() - 16, 10, 10);
                return;
            }

            // Fondo blanco de la carta
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 14, 14);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 14, 14);

            g2.setColor(roja ? new Color(220, 38, 38) : Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.drawString(numero, 10, 24);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 36));
            FontMetrics fm = g2.getFontMetrics();
            int x = ((getWidth() - 6) - fm.stringWidth(palo)) / 2;
            int y = ((getHeight() - 6) + fm.getAscent()) / 2 - 6;
            g2.drawString(palo, x, y);
        }
    }

    // Panel que dibuja un gráfico de barras en vivo con los ganadores de cada ronda
    private class PanelGraficoHistorial extends JPanel {
        PanelGraficoHistorial() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int winsJ = 0, winsB = 0, winsE = 0;
            for (String r : historialResultados) {
                if (r.equals("JUGADOR")) winsJ++;
                else if (r.equals("BANQUERO")) winsB++;
                else winsE++;
            }

            int total = Math.max(1, historialResultados.size());
            int anchoBarra = 45;
            int maxAltura = getHeight() - 90;
            int baseY = getHeight() - 45;

            dibujarBarra(g2, 20, baseY, anchoBarra, (winsJ * maxAltura) / total, new Color(59, 130, 246), "JUG", winsJ);
            dibujarBarra(g2, 85, baseY, anchoBarra, (winsB * maxAltura) / total, new Color(239, 68, 68), "BAN", winsB);
            dibujarBarra(g2, 150, baseY, anchoBarra, (winsE * maxAltura) / total, new Color(34, 197, 94), "EMP", winsE);

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("Rondas jugadas: " + historialResultados.size(), 20, 20);
        }

        private void dibujarBarra(Graphics2D g2, int x, int baseY, int ancho, int alto, Color color, String label, int conteo) {
            g2.setColor(color);
            int alturaReal = Math.max(6, alto);
            g2.fillRoundRect(x, baseY - alturaReal, ancho, alturaReal, 8, 8);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString(String.valueOf(conteo), x + 17, baseY - alturaReal - 6);
            g2.drawString(label, x + 8, baseY + 18);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaBaccarat().setVisible(true));
    }
}