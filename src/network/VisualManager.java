package src.network;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import src.Main;

import java.awt.Dimension;
import java.awt.Color;

public class VisualManager extends JPanel implements MouseInputListener, Manager {

    private static final long serialVersionUID = 1L;
    private JFrame frame;
    private int mouseX;
	private int mouseY;
    private int radius;
    private String fileName;
    double[] outputs;
    
    private double[] circuitInputs;
    private Circuit circuit;

    /**
     * @param guiEnabled Will this manager run with a graphical user interface?
     */
    public VisualManager(boolean guiEnabled, String fileName) {
        this.fileName = fileName;
        setupCircuit();
        if(guiEnabled) {setupGUI();}
    }

    public void setupCircuit() {
        this.circuit = readCircuitFromFile(fileName);
        circuitInputs = new double[circuit.getInputs()];
        outputs = new double[circuit.getNumOutputs()];
    }

    private void setupGUI() {
        frame = new JFrame("Neural Net Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setContentPane(this);
        this.setPreferredSize(new Dimension(300, 300));
        this.setFocusTraversalKeysEnabled(false);
        this.addMouseListener(this);
        frame.pack();

        repaint();
        frame.toFront();
        frame.requestFocus();
        boolean keep = true;

        while(keep) {
            for(int i = 0; i < circuitInputs.length; i++) {
                circuitInputs[i] = Main.getDoubleFromUser("Enter input " + i + ": ");
            }
            circuit.process(circuitInputs);
            repaint();
            int cont = 1;
            try{
                cont = Integer.valueOf(System.console().readLine("Enter 0 to continue, or a non-number to quit: "));
                if(cont != 0) {
                    keep = false;
                }
            } catch(Exception e) {
                System.out.println("Simulation terminated.");
                keep = false;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, (int)frame.getWidth(), (int)frame.getHeight()); // Clears the window

		if(this.getWidth() < this.getHeight()) {
			radius = (int)(this.getWidth() * (25.0/300.0));
		} else {
			radius = (int)(this.getHeight() * (25.0/300.0));
        } if (radius > 50) {
            radius = 50;
        }		

        drawCircuit(g);
    }

    private void drawCircuit(Graphics g) {
        outputs = circuit.process(circuitInputs);
        drawRows(g);
    }
    
    private void drawRows(Graphics g) {
        int circuitLayers = circuit.getLayers().length;
        int width = this.getWidth();
        double xBarrierSize = width / (circuitLayers + 1) / 2;
        for(int i = 0; i < circuitLayers + 1; i++) { // Includes the input layer
            int x = (int)(xBarrierSize + i * 2 * xBarrierSize); 
            drawNodes(g, i, x, xBarrierSize);
        }
    }

    private void drawNodes(Graphics g, int layer, int x, double xBarrierSize) {
        int height = this.getHeight();
        double yBarrierSize;
		int nodes;
        if(layer == 0) { // Is this the input layer?
            nodes = circuit.getInputs();
        } else {
            nodes = circuit.getLayers()[layer - 1].length;
        }
        yBarrierSize = height / (nodes + 1);

        for(int i = 0; i < nodes; i++) {
            int y = (int)(yBarrierSize + i * yBarrierSize);
            if(layer == 0) { // Input layer
                drawNodeCentered(g, x, y, radius, circuitInputs[i]);
                g.drawString("" + (double)Math.round(circuitInputs[i] * 1000d) / 1000d, x, y);
            } else if(layer == circuit.getLayers().length) { // Output layer
                drawNodeCentered(g, x, y, radius, outputs[i]);
                g.drawString("" + (double)Math.round(outputs[i] * 1000d) / 1000d, x, y);
            } else {
                drawNodeCentered(g, x, y, radius, circuit.getLayers()[layer - 1][i].getState().value);
                g.drawString("" + (double)Math.round(circuit.getLayers()[layer - 1][i].getThreshold() * 1000d) / 1000d, x, y);
            }
			
			if(layer != circuit.getLayers().length) { // Not an output layer
				drawConnections(g, x + radius, y, layer, i, xBarrierSize);
			}
        }
	}
	
	private void drawConnections(Graphics g, int x, int y, int layer, int horizontal, double xBarrierSize) {
		int height = this.getHeight();
		int nodes = circuit.getLayers()[layer].length; // Nodes in next layer
		double yBarrierSize = height / (nodes + 1);
		for(int i = 0; i < nodes; i++) { // Current node to draw line to
			int x1 = (int)(x + xBarrierSize * 2) - radius * 2;
			int y1 = (int)(yBarrierSize + i * yBarrierSize);
            g.drawLine(x, y, x1, y1);
            double strength;
            strength = circuit.getConnectionStrengths()[layer][i + (nodes * horizontal)];
            g.drawString("" + (double)Math.round(strength * 1000d) / 1000d, (x + x1) / 2, (y + y1) / 2);
		}
	}

    private void drawNodeCentered(Graphics g, int x, int y, int radius, double strength) {
        if(strength > 0) {
            int colorStrength = (int)(255 * strength);
            g.setColor(new Color(colorStrength, colorStrength, 0, 255 / 2));
            g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        } else if(strength == 0) {
            g.setColor(new Color(0, 0, 0, 255 / 2));
            g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }
        g.setColor(Color.BLACK);
        g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void mouseClicked(MouseEvent e) {
        /* -- Legacy code
        mouseX = e.getX();
        mouseY = e.getY();
        circuitInputs = new double[]{(double)mouseX / this.getWidth(), (double)mouseY / this.getHeight()};
        repaint();
        */
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}