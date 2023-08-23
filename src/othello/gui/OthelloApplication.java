package othello.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphHandler;

import othello.OthelloGame;
import othello.OthelloGameMode;
import othello.ai.OthelloAI;
import othello.ai.OthelloNode;
import othello.ai.OthelloSearchAlgorithm;
import othello.gui.component.OthelloCheckBox;
import othello.gui.component.OthelloPieceLabel;
import othello.gui.component.OthelloPieceLabelIcon;
import othello.model.OthelloBoard;
import othello.model.OthelloDifficulty;
import othello.model.OthelloMove;
import othello.model.OthelloPiece;
import othello.model.OthelloPlayer;
import othello.util.LogUtil;

public class OthelloApplication {

	private static final int FRAME_WIDTH = 890;
	private static final int FRAME_HEIGHT = 815;

	private static final Cursor DEFAULT_CURSOR = Cursor.getDefaultCursor();
	private static final Cursor HOVER_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

	private OthelloGame model;
	private JFrame frame;

	private JMenuItem menuItemNewGame;
	private JMenuItem menuItemLoadGame;
	private JMenuItem menuItemSaveGame;
	private JMenuItem menuItemQuit;
	private JMenuItem menuItemRules;

	private OthelloPieceLabel[][] piecesLabel;
	private JComboBox<String> comboBoxDifficulties;
	private JButton buttonReset;
	private JButton buttonPass;
	private OthelloCheckBox checkBoxPossiblesMoves;
	private JComboBox<String> comboBoxSearchAlgorithms;

	private JLabel labelNameFirstPlayer;
	private JLabel pieceLabelIconFirstPlayer;
	private JLabel labelNameSecondPlayer;
	private JLabel pieceLabelIconSecondPlayer;
	private SpinnerLoader spinnerLoaderSecondPlayer;

	private JCheckBox checkBoxMaterial;
	private JCheckBox checkBoxMobility;
	private JCheckBox checkBoxPositionStrengh;
	private JButton buttonFrameTree;

	// Graphe
	private JFrame frameTree;
	private JScrollPane scrollPaneTree;
	private JGraphXAdapter<OthelloNode, DefaultEdge> jgxAdapter;
	private mxGraphComponent component;
	private OthelloNode rootNode;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new OthelloApplication(OthelloGameMode.PLAYER_VS_MACHINE, OthelloSearchAlgorithm.MINMAX,
				OthelloDifficulty.MEDIUM, OthelloPiece.BLACK, OthelloPiece.WHITE).display();
	}

	public OthelloApplication(OthelloGameMode gameMode, OthelloSearchAlgorithm searchAlgorithm,
			OthelloDifficulty difficulty, OthelloPiece pieceFirstPlayer, OthelloPiece pieceSecondPlayerColor) {
		createModel(gameMode, searchAlgorithm, difficulty, pieceFirstPlayer, pieceSecondPlayerColor);
		createView();
		placeComponents();
		createController();
	}

	/*-----------*/
	/* COMMANDES */
	/*-----------*/

	public void display() {
		displayApplicationFrame();
		displayTreeFrame();
	}

	private void displayApplicationFrame() {
		this.frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}

	private void displayTreeFrame() {
		this.frameTree.setPreferredSize(new Dimension(600, 600));
		this.frameTree.pack();
		this.frameTree.setLocationRelativeTo(null);
		this.frameTree.setVisible(false);
		frameTree.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		if (model.getGameMode() == OthelloGameMode.PLAYER_VS_PLAYER) {
			frameTree.dispose();
		}
	}

	/*--------*/
	/* OUTILS */
	/*--------*/

	private void createModel(OthelloGameMode gameMode, OthelloSearchAlgorithm searchAlgorithm,
			OthelloDifficulty difficulty, OthelloPiece pieceFirstPlayer, OthelloPiece pieceSecondPlayerColor) {
		this.model = OthelloGame.createGame(gameMode, searchAlgorithm, difficulty, pieceFirstPlayer,
				pieceSecondPlayerColor);
	}

	private void createView() {
		this.frame = new JFrame("Othello - Jeu");
		this.frame.setLayout(new BorderLayout());
		this.menuItemNewGame = new JMenuItem("Nouvelle partie");
		this.menuItemNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK));
		this.menuItemLoadGame = new JMenuItem("Charger une partie");
		this.menuItemSaveGame = new JMenuItem("Sauvegarder une partie");
		this.menuItemQuit = new JMenuItem("Quitter");
		this.menuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK));
		this.menuItemRules = new JMenuItem("Règles");
		this.menuItemRules.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK));
		this.pieceLabelIconFirstPlayer = new OthelloPieceLabelIcon(
				model.getFirstPlayer().getPiece().getColor().getColor(),
				Integer.toString(model.getFirstPlayer().getNbPiece()));
		this.labelNameFirstPlayer = new JLabel(model.getFirstPlayer().getName());
		this.labelNameFirstPlayer.setForeground(Color.WHITE);
		this.pieceLabelIconFirstPlayer.setPreferredSize(new Dimension(50, 50));
		this.labelNameSecondPlayer = new JLabel(model.getSecondPlayer().getName());
		this.labelNameSecondPlayer.setForeground(Color.WHITE);
		this.pieceLabelIconSecondPlayer = new OthelloPieceLabelIcon(
				model.getSecondPlayer().getPiece().getColor().getColor(),
				Integer.toString(model.getSecondPlayer().getNbPiece()));
		this.spinnerLoaderSecondPlayer = new SpinnerLoader();
		this.refreshCurrentPlayer();
		this.pieceLabelIconSecondPlayer.setPreferredSize(new Dimension(50, 50));
		this.spinnerLoaderSecondPlayer.setPreferredSize(new Dimension(50, 50));
		final OthelloBoard board = this.model.getBoard();
		this.piecesLabel = new OthelloPieceLabel[board.getRows()][board.getColumns()];
		OthelloDifficulty[] difficulties = OthelloDifficulty.values();
		int indexDifficultiesSelected = 0;
		String[] names = new String[difficulties.length];
		for (int i = 0; i < difficulties.length; i++) {
			names[i] = difficulties[i].getName();
			if (difficulties[i].getName() == model.getDifficulty().getName()) {
				indexDifficultiesSelected = i;
			}
		}
		this.comboBoxDifficulties = new JComboBox<String>(names);
		comboBoxDifficulties.setSelectedIndex(indexDifficultiesSelected);
		this.buttonReset = new JButton("Nouvelle partie");
		this.buttonPass = new JButton("Passer son tour");
		this.checkBoxPossiblesMoves = new OthelloCheckBox("Afficher les coup(s) possible(s)");
		OthelloSearchAlgorithm[] searchAlgorithms = OthelloSearchAlgorithm.values();
		names = new String[searchAlgorithms.length];
		int indexSearchAlgorithmsSelected = 0;
		for (int i = 0; i < searchAlgorithms.length; i++) {
			names[i] = searchAlgorithms[i].getName();
			if (searchAlgorithms[i].getName() == model.getSearchAlgorithm().getName()) {
				indexSearchAlgorithmsSelected = i;
			}
		}
		this.comboBoxSearchAlgorithms = new JComboBox<String>(names);
		comboBoxSearchAlgorithms.setSelectedIndex(indexSearchAlgorithmsSelected);
		this.checkBoxMaterial = new JCheckBox("Matériel");
		this.checkBoxMobility = new JCheckBox("Mobilité");
		this.checkBoxPositionStrengh = new JCheckBox("Force de position");
		this.buttonFrameTree = new JButton("Afficher - Arbre");
		this.frameTree = new JFrame("Othello - Arbre");
		this.frameTree.setLayout(new BorderLayout());
	}

	private void placeComponents() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menuGame = new JMenu("Jeu");
		JMenu menuHelp = new JMenu("Aide");
		menuGame.add(menuItemNewGame);
		menuGame.add(menuItemLoadGame);
		menuGame.add(menuItemSaveGame);
		menuGame.add(menuItemQuit);
		menuHelp.add(menuItemRules);
		menuBar.add(menuGame);
		menuBar.add(menuHelp);
		frame.setJMenuBar(menuBar);
		JPanel p = new JPanel(new BorderLayout());
		{
			JPanel q = new JPanel(new BorderLayout());
			{
				q.setBackground(new Color(35, 35, 35));
				q.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
				JPanel r = new JPanel();
				{
					r.setBackground(new Color(35, 35, 35));
					JPanel s = new JPanel();
					s.setBackground(new Color(35, 35, 35));
					s.add(pieceLabelIconSecondPlayer);
					s.add(labelNameSecondPlayer);
					s.add(spinnerLoaderSecondPlayer);
					r.add(s);
				}
				q.add(r, BorderLayout.NORTH);
				final int rows = model.getBoard().getRows();
				final int columns = model.getBoard().getColumns();
				final int hgap = 2;
				final int vgap = 2;
				r = new JPanel(new GridLayout(rows, columns, hgap, vgap));
				{
					r.setBackground(new Color(35, 35, 35));
					final Border border1 = BorderFactory.createLineBorder(new Color(20, 20, 20), 4);
					final Border border2 = BorderFactory.createLineBorder(new Color(10, 10, 10), 4);
					final Border border3 = BorderFactory.createLineBorder(new Color(20, 20, 20), 2);
					final Border bc1 = new CompoundBorder(border1, border2);
					final Border bc2 = new CompoundBorder(border1, border3);
					final Border compoundBorder = new CompoundBorder(bc1, bc2);
					r.setBorder(compoundBorder);

					OthelloBoard board = model.getBoard();
					List<OthelloMove> moves = model.getValidMoves(model.getCurrentPlayer());

					for (Integer row = 0; row < rows; row++) {
						for (Integer col = 0; col < columns; col++) {
							OthelloMove move = OthelloMove.createMove(row, col);
							OthelloPiece piece = board.getPiece(move);
							final boolean isOccupied = piece != OthelloPiece.EMPTY;
							boolean isPossibleMove = moves.contains(move);
							for (OthelloMove m : moves) {
								if (m.getRow() == move.getRow() && m.getColumn() == move.getColumn()) {
									isPossibleMove = true;
								}
							}
							final Color pieceColor = piece.getColor().getColor();
							Color pieceBorderColor = null;
							if (pieceColor != null) {
								pieceBorderColor = pieceColor.getRGB() == Color.BLACK.getRGB() ? Color.BLACK
										: Color.WHITE;
							}
							OthelloPieceLabel label = new OthelloPieceLabel(pieceColor, pieceBorderColor, isOccupied,
									false, isPossibleMove);
							piecesLabel[row][col] = label;
							r.add(label);
						}
					}
				}
				q.add(r, BorderLayout.CENTER);

				r = new JPanel(new FlowLayout());
				{
					r.setBackground(new Color(35, 35, 35));
					JPanel s = new JPanel();
					s.setBackground(new Color(35, 35, 35));
					s.add(pieceLabelIconFirstPlayer);
					s.add(labelNameFirstPlayer);
					r.add(s);
				}
				q.add(r, BorderLayout.SOUTH);
			}
			p.add(q, BorderLayout.CENTER);

			q = new JPanel();
			{
				q.setBackground(new Color(80, 80, 80));
				JPanel r = new JPanel(new GridLayout(2, 1));
				{
					r.setBackground(new Color(80, 80, 80));
					r.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
					JPanel s = new JPanel(new FlowLayout());
					{
						final Border border1 = BorderFactory.createLineBorder(new Color(20, 20, 20), 2);
						final Border border2 = BorderFactory.createLineBorder(new Color(10, 10, 10), 2);
						final Border border3 = BorderFactory.createLineBorder(new Color(20, 20, 20), 1);
						final Border bc1 = new CompoundBorder(border1, border2);
						final Border bc2 = new CompoundBorder(border1, border3);
						final Border compoundBorder = new CompoundBorder(bc1, bc2);
						s.setBackground(new Color(0, 130, 92));
						s.setBorder(compoundBorder);
						s.add(this.buttonReset);
						s.add(this.buttonPass);
						s.add(this.checkBoxPossiblesMoves);
					}
					r.add(s);
					s = new JPanel(new GridLayout(1, 1));
					{
						JPanel t = new JPanel(new FlowLayout());
						{
							final Border border1 = BorderFactory.createLineBorder(new Color(20, 20, 20), 2);
							final Border border2 = BorderFactory.createLineBorder(new Color(10, 10, 10), 2);
							final Border border3 = BorderFactory.createLineBorder(new Color(20, 20, 20), 1);
							final Border bc1 = new CompoundBorder(border1, border2);
							final Border bc2 = new CompoundBorder(border1, border3);
							final Border compoundBorder = new CompoundBorder(bc1, bc2);
							t.setBackground(new Color(0, 130, 92));
							t.setBorder(compoundBorder);
							t.add(new JLabel("Niveau de difficulté"));
							t.add(this.comboBoxDifficulties);
							t.add(new JLabel("Algorithmes de recherche"));
							t.add(this.comboBoxSearchAlgorithms);
							t.add(new JLabel("Fonctions d'évaluation"));
							t.add(this.checkBoxMaterial);
							t.add(this.checkBoxMobility);
							t.add(this.checkBoxPositionStrengh);
							t.add(this.buttonFrameTree);
						}
						s.add(t);
					}
					if (model.getGameMode() == OthelloGameMode.PLAYER_VS_MACHINE) {
						r.add(s);
					}
				}
				q.add(r);
			}
			p.add(q, BorderLayout.SOUTH);
		}
		this.frame.add(p, BorderLayout.CENTER);

		this.scrollPaneTree = new JScrollPane();
		p = new JPanel();
		{
			createTree();
			Graph<OthelloNode, DefaultEdge> graph = buildGraph(rootNode, -1);
			jgxAdapter = new JGraphXAdapter<OthelloNode, DefaultEdge>(graph);
			jgxAdapter.getEdgeToCellMap().forEach((edge, cell) -> cell.setValue(null));
			component = new mxGraphComponent(jgxAdapter);
			// Positioning via jgraphx layouts
			mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
			// Exécuter la mise en page
			layout.execute(jgxAdapter.getDefaultParent());
			p.add(component);
			component.setPreferredSize(new Dimension(
					/* (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.80) */ 600, 700));
			component.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			component.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			component.setAutoscrolls(true);
			component.setConnectable(false);
			component.getGraph().setAllowDanglingEdges(false);
			component.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
			component.setCenterZoom(false);
			component.getGraphControl().addMouseWheelListener(new MouseWheelListener() {
				public void mouseWheelMoved(MouseWheelEvent e) {
					if (e.getWheelRotation() < 0) {
						// Zoom avant
						component.zoomIn();
					} else {
						// Zoom arrière
						component.zoomOut();
					}
				}
			});
			component.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					int keyCode = e.getKeyCode();
					switch (keyCode) {
					case KeyEvent.VK_UP:
						scrollPaneTree.getVerticalScrollBar().setValue(scrollPaneTree.getVerticalScrollBar().getValue()
								- scrollPaneTree.getVerticalScrollBar().getUnitIncrement());
						break;
					case KeyEvent.VK_DOWN:
						scrollPaneTree.getVerticalScrollBar().setValue(scrollPaneTree.getVerticalScrollBar().getValue()
								+ scrollPaneTree.getVerticalScrollBar().getUnitIncrement());
						break;
					case KeyEvent.VK_LEFT:
						scrollPaneTree.getHorizontalScrollBar()
								.setValue(scrollPaneTree.getHorizontalScrollBar().getValue()
										- scrollPaneTree.getHorizontalScrollBar().getUnitIncrement());
						break;
					case KeyEvent.VK_RIGHT:
						scrollPaneTree.getHorizontalScrollBar()
								.setValue(scrollPaneTree.getHorizontalScrollBar().getValue()
										+ scrollPaneTree.getHorizontalScrollBar().getUnitIncrement());
						break;
					default:
						break;
					}
				}
			});
			component.addMouseListener(new MouseAdapter() {
				private Point origin;

				public void mousePressed(MouseEvent e) {
					if (SwingUtilities.isMiddleMouseButton(e)) {
						origin = new Point(e.getPoint());
					}
				}

				public void mouseReleased(MouseEvent e) {
					origin = null;
				}

				public void mouseDragged(MouseEvent e) {
					if (origin != null && SwingUtilities.isMiddleMouseButton(e)) {
						JViewport viewPort = scrollPaneTree.getViewport();
						Point point = e.getPoint();
						int deltaX = origin.x - point.x;
						int deltaY = origin.y - point.y;
						Rectangle view = viewPort.getViewRect();
						view.x += deltaX;
						view.y += deltaY;
						component.scrollRectToVisible(view);
					}
				}
			});
			component.refresh();
		}
		;
		scrollPaneTree.setViewportView(p);
		this.frameTree.add(scrollPaneTree, BorderLayout.CENTER);

	}

	public static Graph<OthelloNode, DefaultEdge> buildGraph(OthelloNode node, Integer parentNodeNum) {
		Graph<OthelloNode, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
		graph.addVertex(node);
		for (OthelloNode child : node.getChildren()) {
			graph.addVertex(child);
			graph.addEdge(node, child);
			Graph<OthelloNode, DefaultEdge> childGraph = buildGraph(child, node.getNodeNum());
			Graphs.addGraph(graph, childGraph);
		}
		return graph;
	}

	private void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameTree.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				final int width = (int) (frameTree.getWidth() * 0.80);
				final int height = (int) (frameTree.getHeight() * 0.80);
				component.setPreferredSize(new Dimension(width, height));
			}
		});
		frameTree.addWindowStateListener(new WindowStateListener() {

			@Override
			public void windowStateChanged(WindowEvent e) {
				final int width = (int) (frameTree.getWidth() * 0.80);
				final int height = (int) (frameTree.getHeight() * 0.80);
				component.setPreferredSize(new Dimension(width, height));
			}
		});
		menuItemNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.resetGame();
			}
		});
		menuItemNewGame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK), "newGame");
		menuItemNewGame.getActionMap().put("newGame", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				model.resetGame();
			}
		});
		menuItemLoadGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OthelloApplication.this.loadGame();
			}
		});
		menuItemLoadGame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK), "loadGame");
		menuItemLoadGame.getActionMap().put("loadGame", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				OthelloApplication.this.loadGame();
			}
		});
		menuItemSaveGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OthelloApplication.this.saveGame();
			}
		});
		menuItemSaveGame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK), "saveGame");
		menuItemSaveGame.getActionMap().put("saveGame", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				OthelloApplication.this.saveGame();
			}
		});
		menuItemQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
		menuItemQuit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK), "quitGame");
		menuItemQuit.getActionMap().put("quitGame", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
		menuItemRules.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						File pdfFile = new File("resources/rules/Othello_rules.pdf");
						if (pdfFile.exists()) {
							Desktop.getDesktop().open(pdfFile);
						} else {
							Desktop.getDesktop().browse(new URI("https://www.ffothello.org/othello/regles-du-jeu/"));
						}
					} catch (IOException | URISyntaxException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		menuItemRules.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_DOWN_MASK), "rules");
		menuItemRules.getActionMap().put("rules", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						File pdfFile = new File("resources/rules/Othello_rules.pdf");
						if (pdfFile.exists()) {
							Desktop.getDesktop().open(pdfFile);
						} else {
							Desktop.getDesktop().browse(new URI("https://www.ffothello.org/othello/regles-du-jeu/"));
						}
					} catch (IOException | URISyntaxException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				final OthelloBoard board = OthelloApplication.this.model.getBoard();
				final Integer rows = board.getRows();
				final Integer columns = board.getColumns();
				OthelloPieceLabel pieceLabel;
				OthelloMove move;
				OthelloPiece piece;
				Boolean isOccupied = false, isPossibleMove = false;
				List<OthelloMove> moves = model.getValidMoves(model.getCurrentPlayer());
				for (Integer row = 0; row < rows; row++) {
					for (Integer col = 0; col < columns; col++) {
						pieceLabel = piecesLabel[row][col];
						move = OthelloMove.createMove(row, col);
						piece = board.getPiece(move);
						isOccupied = piece != OthelloPiece.EMPTY;
						isPossibleMove = moves.contains(move);
						pieceLabel.setPieceColor(piece.getColor().getColor());
						final Color pieceColor = piece.getColor().getColor();
						Color pieceBorderColor = null;
						if (pieceColor != null) {
							pieceBorderColor = pieceColor.getRGB() == Color.BLACK.getRGB() ? Color.BLACK : Color.WHITE;
						}
						pieceLabel.setPieceBorderColor(pieceBorderColor);
						pieceLabel.setIsOccupied(isOccupied);
						pieceLabel.setIsPossibleMove(isPossibleMove);
					}
				}
				OthelloApplication.this.refreshCurrentPlayer();
			}
		});
		final OthelloBoard board = this.model.getBoard();
		final int rows = board.getRows();
		final int columns = board.getColumns();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				final OthelloPieceLabel label = (OthelloPieceLabel) piecesLabel[row][col];
				final Color pieceBorderColor = label.getPieceBorderColor();
				final Integer rowSelected = row, colSelected = col;
				final OthelloPieceLabel pieceLabel = piecesLabel[row][col];
				pieceLabel.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseEntered(MouseEvent e) {
						if (label.getIsOccupied()) {
							return;
						}
						if (!label.getIsPossibleMove()) {
							return;
						}
						// label.setPieceBorderColor(HOVER_POSSIBLE_MOVE_COLOR);
						label.setCursor(HOVER_CURSOR);
					}

					@Override
					public void mouseClicked(MouseEvent e) {
						if (label.getIsOccupied() || !label.getIsPossibleMove()) {
							return;
						}
						if (model.getGameMode() == OthelloGameMode.PLAYER_VS_MACHINE) {
							OthelloMove newMove = OthelloMove.createMove(rowSelected, colSelected);
							if (model.isValidMove(newMove, model.getCurrentPlayer())) {
								model.validateMove(newMove, model.getCurrentPlayer());
								model.changeCurrentPlayer();
								executeAIPlay();
							}
						} else {
							OthelloMove move = OthelloMove.createMove(rowSelected, colSelected);
							if (model.isValidMove(move, model.getCurrentPlayer())) {
								model.validateMove(move, model.getCurrentPlayer());
								model.changeCurrentPlayer();
							}
						}
						label.setCursor(DEFAULT_CURSOR);
						if (model.isGameOver()) {
							final OthelloPlayer winner = model.getWinner();
							String message;
							if (winner != null) {
								message = "Le joueur " + winner.getName() + " a gagné.";
							} else {
								message = "Égalité.";
							}
							final int dialogButton = JOptionPane.YES_NO_OPTION;
							final int dialogResult = JOptionPane.showConfirmDialog(frame,
									message + "\n" + "Voulez-vous rejouer une nouvelle partie ?",
									"Othello - Fin de la partie", dialogButton);
							if (dialogResult == JOptionPane.YES_OPTION) {
								OthelloApplication.this.resetGame();
							} else {
								frame.dispose();
								frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
							}
						}
					}

					@Override
					public void mouseExited(MouseEvent e) {
						if (label.getIsOccupied()) {
							return;
						}
						if (!label.getIsPossibleMove()) {
							return;
						}
						label.setPieceBorderColor(pieceBorderColor);
						label.setCursor(DEFAULT_CURSOR);
					}

				});
			}
		}
		comboBoxDifficulties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedName = (String) comboBoxDifficulties.getSelectedItem();
				OthelloDifficulty currentDifficulty = model.getDifficulty();
				for (OthelloDifficulty difficulty : OthelloDifficulty.values()) {
					if (difficulty.getName().equals(selectedName)) {
						currentDifficulty = difficulty;
						break;
					}
				}
				model.setDifficulty(currentDifficulty);
				refreshTree(createTree());
			}
		});
		buttonReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.resetGame();
				// Reinit
				OthelloBoard board = model.getBoard();
				List<OthelloMove> moves = model.getValidMoves(model.getCurrentPlayer());
				for (Integer row = 0; row < rows; row++) {
					for (Integer col = 0; col < columns; col++) {
						OthelloMove move = OthelloMove.createMove(row, col);
						OthelloPiece piece = board.getPiece(move);
						final boolean isOccupied = piece != OthelloPiece.EMPTY;
						boolean isPossibleMove = moves.contains(move);
						for (OthelloMove m : moves) {
							if (m.getRow() == move.getRow() && m.getColumn() == move.getColumn()) {
								isPossibleMove = true;
							}
						}
						final Color pieceColor = piece.getColor().getColor();
						Color pieceBorderColor = null;
						if (pieceColor != null) {
							pieceBorderColor = pieceColor.getRGB() == Color.BLACK.getRGB() ? Color.BLACK : Color.WHITE;
						}
						OthelloPieceLabel label = new OthelloPieceLabel(pieceColor, pieceBorderColor, false, false,
								false);
						piecesLabel[row][col] = label;
					}
				}
				refreshTree(createTree());
			}
		});
		buttonPass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.changeCurrentPlayer();
				if (model.getGameMode() == OthelloGameMode.PLAYER_VS_MACHINE) {
					executeAIPlay();
					if (model.isGameOver()) {
						final OthelloPlayer winner = model.getWinner();
						String message;
						if (winner != null) {
							message = "Le joueur " + winner.getName() + " a gagné.";
						} else {
							message = "Égalité.";
						}
						final int dialogButton = JOptionPane.YES_NO_OPTION;
						final int dialogResult = JOptionPane.showConfirmDialog(frame,
								message + "\n" + "Voulez-vous rejouer une nouvelle partie ?",
								"Othello - Fin de la partie", dialogButton);
						if (dialogResult == JOptionPane.YES_OPTION) {
							OthelloApplication.this.resetGame();
						} else {
							frame.dispose();
							frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
						}
					}
				}
			}
		});
		checkBoxPossiblesMoves.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OthelloPieceLabel pieceLabel;
				for (Integer row = 0; row < rows; row++) {
					for (Integer col = 0; col < columns; col++) {
						pieceLabel = piecesLabel[row][col];
						pieceLabel.setIsEnabledPossibleMove(!pieceLabel.getIsEnabledPossibleMove());
					}
				}
			}
		});
		comboBoxSearchAlgorithms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedName = (String) comboBoxSearchAlgorithms.getSelectedItem();
				OthelloSearchAlgorithm currentSearchAlgorithm = model.getSearchAlgorithm();
				for (OthelloSearchAlgorithm searchAlgorithm : OthelloSearchAlgorithm.values()) {
					if (searchAlgorithm.getName().equals(selectedName)) {
						searchAlgorithm.setMaterialEnabled(currentSearchAlgorithm.isMaterialEnabled());
						searchAlgorithm.setMobilityEnabled(currentSearchAlgorithm.isMobilityEnabled());
						searchAlgorithm.setPositionStrengthEnabled(currentSearchAlgorithm.isPositionStrengthEnabled());
						currentSearchAlgorithm = searchAlgorithm;
						break;
					}
				}
				model.setSearchAlgorithm(currentSearchAlgorithm);
				refreshTree(createTree());
			}
		});
		checkBoxMaterial.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.getSearchAlgorithm().setMaterialEnabled(checkBoxMaterial.isSelected());
			}
		});
		checkBoxMobility.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.getSearchAlgorithm().setMobilityEnabled(checkBoxMaterial.isSelected());
			}
		});
		checkBoxPositionStrengh.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.getSearchAlgorithm().setPositionStrengthEnabled(checkBoxMaterial.isSelected());
			}
		});
		buttonFrameTree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getGameMode() != OthelloGameMode.PLAYER_VS_MACHINE) {
					return;
				}
				if (!frameTree.isVisible()) {
					frameTree.setVisible(true);
					buttonFrameTree.setText("Masquer - Arbre");
				} else {
					frameTree.setVisible(false);
					buttonFrameTree.setText("Afficher - Arbre");
				}
			}
		});
	}

	private void refreshCurrentPlayer() {
		pieceLabelIconFirstPlayer.setText(Integer.toString(model.getFirstPlayer().getNbPiece()));
		pieceLabelIconSecondPlayer.setText(Integer.toString(model.getSecondPlayer().getNbPiece()));
		if (model.isFirstPlayerCurrentPlayer()) {
			((OthelloPieceLabelIcon) pieceLabelIconFirstPlayer).setIsActive(true);
			((OthelloPieceLabelIcon) pieceLabelIconSecondPlayer).setIsActive(false);
			((OthelloPieceLabelIcon) pieceLabelIconFirstPlayer).setTextColor(Color.YELLOW);
			((OthelloPieceLabelIcon) pieceLabelIconSecondPlayer).setTextColor(Color.BLACK);
			this.labelNameFirstPlayer.setForeground(Color.YELLOW);
			this.labelNameSecondPlayer.setForeground(Color.WHITE);
		} else {
			((OthelloPieceLabelIcon) pieceLabelIconFirstPlayer).setIsActive(false);
			((OthelloPieceLabelIcon) pieceLabelIconSecondPlayer).setIsActive(true);
			((OthelloPieceLabelIcon) pieceLabelIconFirstPlayer).setTextColor(Color.BLACK);
			((OthelloPieceLabelIcon) pieceLabelIconSecondPlayer).setTextColor(Color.YELLOW);
			this.labelNameFirstPlayer.setForeground(Color.WHITE);
			this.labelNameSecondPlayer.setForeground(Color.YELLOW);
		}
	}

	private void resetGame() {
		OthelloApplication.this.createModel(model.getGameMode(), model.getSearchAlgorithm(), model.getDifficulty(),
				model.getFirstPlayer().getPiece(), model.getSecondPlayer().getPiece());
		OthelloApplication.this.createController();
		model.setCurrentPlayer(model.getCurrentPlayer());
	}

	private String showFileDialog(int dialogType) {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers Othello (*.oth)", "oth");
		fileChooser.setFileFilter(filter);
		final int returnValue = dialogType == JFileChooser.OPEN_DIALOG ? fileChooser.showOpenDialog(frame)
				: fileChooser.showSaveDialog(frame);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			final File selectedFile = fileChooser.getSelectedFile();
			return selectedFile.getAbsolutePath();
		}
		return null;
	}

	private void loadGame() {
		final String absolutePath = showFileDialog(JFileChooser.OPEN_DIALOG);
		final File file = new File(absolutePath);
		if (absolutePath == null || !file.exists()) {
			JOptionPane.showMessageDialog(frame, "Le fichier sélectionné est introuvable.", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		SwingWorker<OthelloGame, Void> worker = new SwingWorker<OthelloGame, Void>() {
			@Override
			protected OthelloGame doInBackground() throws Exception {
				return model.loadGame(absolutePath);
			}

			@Override
			protected void done() {
				try {
					OthelloGame game = get();
					if (game != null) {
						model.setGame(game);
						refreshTree(createTree());
					} else {
						JOptionPane.showMessageDialog(frame, "Le fichier sélectionné est illisible.", "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Le fichier sélectionné est illisible.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		worker.execute();
	}

	private void saveGame() {
		final String absolutePath = showFileDialog(JFileChooser.SAVE_DIALOG);
		if (absolutePath != null) {
			SwingWorker<OthelloGame, Void> worker = new SwingWorker<OthelloGame, Void>() {
				@Override
				protected OthelloGame doInBackground() throws Exception {
					return model.saveGame(absolutePath + ".oth");
				}

				@Override
				protected void done() {
					try {
						OthelloGame game = get();
						if (game != null) {
							model.setGame(game);
						}
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			};
			worker.execute();
		}
	}

	private void executeAIPlay() {
		spinnerLoaderSecondPlayer.restartSpinnerLoader();
		Timer timer = new Timer(0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						rootNode = createTree();
						long startTime;
						Runtime runtime;
						long endTime;
						long duration;
						long memoryUsage;
						startTime = System.nanoTime();
						runtime = Runtime.getRuntime();
						if (model.getSearchAlgorithm() == OthelloSearchAlgorithm.MINMAX) {
							List<OthelloMove> possibleMoves = model.getValidMoves(model.getCurrentPlayer());
							OthelloMove bestMove = null;
							double bestValue = Double.NEGATIVE_INFINITY;
							for (OthelloMove move : possibleMoves) {
								double value = OthelloAI.minimax(rootNode, model.getDifficulty().getDepth());
								if (value > bestValue) {
									bestValue = value;
									bestMove = move;
								}
							}
							model.validateMove(bestMove, model.getCurrentPlayer());
							model.changeCurrentPlayer();
							spinnerLoaderSecondPlayer.stopSpinnerLoader();
						} else if (model.getSearchAlgorithm() == OthelloSearchAlgorithm.ALPHABETA_MINIMAX) {
							List<OthelloMove> possibleMoves = model.getValidMoves(model.getCurrentPlayer());
							OthelloMove bestMove = null;
							double bestValue = Double.NEGATIVE_INFINITY;
							for (OthelloMove move : possibleMoves) {
								double value = OthelloAI.minimaxAlphaBeta(rootNode, model.getDifficulty().getDepth(),
										Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
								if (value > bestValue) {
									bestValue = value;
									bestMove = move;
								}
							}
							model.validateMove(bestMove, model.getCurrentPlayer());
							model.changeCurrentPlayer();
							spinnerLoaderSecondPlayer.stopSpinnerLoader();
						}
						endTime = System.nanoTime();
						duration = (endTime - startTime) / 1_000_000;
						memoryUsage = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
						LogUtil.log(model.getSearchAlgorithm().getName() + " en " + duration + " ms.");
						LogUtil.log("Mémoire utilisée pour " + model.getSearchAlgorithm().getName() + " " + memoryUsage
								+ " Mo.");
						refreshTree(rootNode);
						return null;
					}
				};
				worker.execute();
			}
		});
		timer.setRepeats(false);
		timer.start();
		refreshCurrentPlayer();
	}

	private OthelloNode createTree() {
		OthelloNode.setNodeCount(0);
		long startTime;
		Runtime runtime;
		long endTime;
		long duration;
		long memoryUsage;
		startTime = System.nanoTime();
		runtime = Runtime.getRuntime();
		rootNode = OthelloAI.createTree(model, model.getDifficulty().getDepth());
		List<OthelloMove> possibleMoves = model.getValidMoves(model.getCurrentPlayer());
		for (OthelloMove move : possibleMoves) {
			if (model.getSearchAlgorithm() == OthelloSearchAlgorithm.MINMAX) {
				OthelloAI.minimax(rootNode, model.getDifficulty().getDepth());
			} else if (model.getSearchAlgorithm() == OthelloSearchAlgorithm.ALPHABETA_MINIMAX) {
				OthelloAI.minimaxAlphaBeta(rootNode, model.getDifficulty().getDepth(), Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY);
			}
		}
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1_000_000;
		memoryUsage = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
		LogUtil.log("Arbre crée en " + duration + " ms.");
		LogUtil.log("Mémoire utilisée pour l'arbre " + memoryUsage + " Mo.");
		startTime = System.nanoTime();
		runtime = Runtime.getRuntime();
		return rootNode;
	}

	private void refreshTree(OthelloNode node) {
		Graph<OthelloNode, DefaultEdge> graph = buildGraph(node, -1);
		jgxAdapter = new JGraphXAdapter<OthelloNode, DefaultEdge>(graph);
		jgxAdapter.getEdgeToCellMap().forEach((edge, cell) -> cell.setValue(null));
		component.setGraph(jgxAdapter);
		mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
		layout.execute(jgxAdapter.getDefaultParent());
		component.refresh();
	}

}
