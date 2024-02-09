import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ConwayGameOfLife extends JFrame {
    private static final int DEFAULT_GRID_SIZE = 20;
    private static final int CELL_SIZE = 20;

    private int gridSize;
    private CellManager cellManager;
    private Timer timer;

    private JButton startButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton resetButton;
    private JButton nextButton;
    private JPanel gridPanel;

    public ConwayGameOfLife(int gridSize) {
        this.gridSize = gridSize;
        this.cellManager = new CellManager(gridSize);
        this.timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evolve();
            }
        });

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Conway's Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel controlPanel = new JPanel();
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });
        controlPanel.add(startButton);

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseSimulation();
            }
        });
        controlPanel.add(pauseButton);

        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeSimulation();
            }
        });
        controlPanel.add(resumeButton);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSimulation();
            }
        });
        controlPanel.add(resetButton);

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evolve();
            }
        });
        controlPanel.add(nextButton);

        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
            }
        };
        gridPanel.setPreferredSize(new Dimension(gridSize * CELL_SIZE, gridSize * CELL_SIZE));
        gridPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = evt.getY() / CELL_SIZE;
                int col = evt.getX() / CELL_SIZE;
                cellManager.setCellState(row, col, !cellManager.isCellAlive(row, col));
                gridPanel.repaint();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (cellManager.isCellAlive(i, j)) {
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                    g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void startSimulation() {
        timer.start();
    }

    private void pauseSimulation() {
        timer.stop();
    }

    private void resumeSimulation() {
        timer.start();
    }

    private void resetSimulation() {
        timer.stop();
        cellManager = new CellManager(gridSize);
        gridPanel.repaint();
    }

    private void evolve() {
        cellManager.evolve();
        gridPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int gridSize = DEFAULT_GRID_SIZE;
            ConwayGameOfLife game = new ConwayGameOfLife(gridSize);
            game.setVisible(true);
        });
    }
}
