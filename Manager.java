import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Dimension;
import java.awt.Color;

public class Manager extends JPanel implements MouseInputListener {

    private static final long serialVersionUID = 1L;
    private JFrame frame;
    private int mouseX;
	private int mouseY;
    private int radius;
    
    private double[] circuitInputs;
    private Circuit circuit;

    public Manager() {
        setupCircuit();

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
    }

    public static void main(String[] args) {        
        new Manager();
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

    private void setupCircuit() {
        circuitInputs = new double[]{0,0};
        int[] layerSizes = {3,1};
        double[][] thresholds = setupThresholds(layerSizes, circuitInputs.length);
        double[][] connectionStrength = setupConnectionStrength(layerSizes, circuitInputs.length);
        circuit = new Circuit(circuitInputs.length, layerSizes, thresholds, connectionStrength);
        circuit.process(circuitInputs);
    }

    private double[][] setupThresholds(int[] layerSizes, int circuitInputSize) {
        double[][] thresholds = new double[layerSizes.length][];
        for(int x = 0; x < layerSizes.length; x++) {
            thresholds[x] = new double[layerSizes[x]];
            int max;
            if(x == 0) {
                max = circuitInputSize;
            } else {
                max = thresholds[x - 1].length;
            }

            for(int y = 0; y < thresholds[x].length; y++) {
                thresholds[x][y] = Math.random() * max;
            }
        }
        return thresholds;
    }

    private double[][] setupConnectionStrength(int[] layerSizes, int circuitInputSize) {
        double[][] connectionStrength = new double[layerSizes.length][];
        for(int i = 0; i < connectionStrength.length; i++) {
            if (i == 0) { // Connection between input and first layer
                connectionStrength[i] = new double[circuitInputSize * layerSizes[i]]; // Total connections = inputs * first layer nodes
            } else { // Connection between current layer and next layer
                connectionStrength[i] = new double[layerSizes[i - 1] * layerSizes[i]]; // Total connections = current layer nodes * next layer nodes
            }
        }

        for(int x = 0; x < connectionStrength.length; x++) {
            for(int y = 0; y < connectionStrength[x].length; y++) {
                connectionStrength[x][y] = Math.random();
            }
        }
        return connectionStrength;
    }

    private void drawCircuit(Graphics g) {
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
            if(layer == 0) { // Input layer?
                drawNodeCentered(g, x, y, radius, (int)Math.ceil(circuitInputs[i]));
                g.drawString("" + (double)Math.round(circuitInputs[i] * 1000d) / 1000d, x, y);
            } else {
                drawNodeCentered(g, x, y, radius, circuit.getLayers()[layer - 1][i].getState());
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

    private void drawNodeCentered(Graphics g, int x, int y, int radius, int nodeState) {
        if(nodeState > 0) {
            g.setColor(new Color(255, 255, 0, 255 / 2));
            g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        } else if(nodeState == 0) {
            g.setColor(new Color(0, 0, 0, 255 / 2));
            g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }
        g.setColor(Color.BLACK);
        g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        circuitInputs = new double[]{(double)mouseX / this.getWidth(), (double)mouseY / this.getHeight()};
        circuit.process(circuitInputs);
        repaint();
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}