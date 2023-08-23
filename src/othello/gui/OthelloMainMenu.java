package othello.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import othello.OthelloGameMode;
import othello.ai.OthelloSearchAlgorithm;
import othello.model.OthelloDifficulty;
import othello.model.OthelloPiece;

public class OthelloMainMenu {

	private JFrame frame;

	private JLabel labelDifficulty;
	private JComboBox<String> comboBoxDifficulties;
	private JButton buttonPlay;
	private JButton buttonRules;
	private JButton buttonQuit;

	private JLabel labelFirstPlayerColor;
	private JRadioButton radioButtonFirstPlayerColorBlack;
	private JRadioButton radioButtonFirstPlayerColorWhite;
	
	private JLabel labelSecondPlayerColor;
	private JRadioButton radioButtonSecondPlayerColorBlack;
	private JRadioButton radioButtonSecondPlayerColorWhite;

	private OthelloGameMode gameModeSelected;
	private OthelloDifficulty difficultySelected;
	private OthelloPiece pieceFirstPlayerSelected;
	private OthelloPiece pieceSecondPlayerColorSelected;

	public OthelloMainMenu() {
		createModel();
		createView();
		placeComponents();
		createController();
	}

	public void display() {
		frame.setPreferredSize(new Dimension(700, 700));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void createModel() {
		this.gameModeSelected = OthelloGameMode.PLAYER_VS_PLAYER;
		this.difficultySelected = OthelloDifficulty.EASY;
		this.pieceFirstPlayerSelected = OthelloPiece.BLACK;
		this.pieceSecondPlayerColorSelected = OthelloPiece.WHITE;
	}

	private void createView() {
		this.frame = new JFrame("Othello - Accueil");
		this.frame.setLayout(new BorderLayout());
		this.labelDifficulty = new JLabel("Difficulté :");
		OthelloDifficulty[] difficulties = OthelloDifficulty.values();
		String[] names = new String[difficulties.length];
		for (int i = 0; i < difficulties.length; i++) {
			names[i] = difficulties[i].getName();
		}
		this.comboBoxDifficulties = new JComboBox<String>(names);
		((JLabel) this.comboBoxDifficulties.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		this.buttonPlay = new JButton("Jouer");
		this.buttonRules = new JButton("Règles");
		this.buttonQuit = new JButton("Quitter");
		this.labelFirstPlayerColor = new JLabel("Choix de la couleur du joueur 1 : ");
		this.labelFirstPlayerColor.setForeground(Color.WHITE);
		this.labelSecondPlayerColor = new JLabel("Choix de la couleur du joueur 2 : ");
		this.labelSecondPlayerColor.setForeground(Color.WHITE);
	}

	private void placeComponents() {
		JPanel p = new JPanel() {
			private static final long serialVersionUID = 1L;
			// private Image backgroundImage;
			// Constructor
			{
				// this.backgroundImage = new ImageIcon("src/othello/gui/music/wallpaper.png").getImage();
			}

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				int width = getWidth();
				int height = getHeight();
				GradientPaint gp = new GradientPaint(0, 0, new Color(64, 64, 64), width, height, new Color(25, 25, 25));
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, width, height);
			}
		};
		{
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

			final int padding = 10;
			final int width = 400;

			JLabel label = new JLabel("Othello");
			label.setFont(new Font("SansSerif", Font.BOLD, 48));
			label.setForeground(Color.WHITE);
			label.setHorizontalTextPosition(JLabel.CENTER);
			label.setVerticalTextPosition(JLabel.CENTER);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			label.setMaximumSize(new Dimension(width - 2 * padding, 100));
			p.add(label);
			p.add(Box.createVerticalStrut(padding));

			JPanel q = new JPanel(new FlowLayout(FlowLayout.CENTER));
			{
				q.setOpaque(false);
				JLabel labelGameMode = new JLabel("Mode de jeu :");
				labelGameMode.setForeground(Color.WHITE);
				q.add(labelGameMode);
				ButtonGroup buttonGroupGameMode = new ButtonGroup();
				for (OthelloGameMode gameMode : OthelloGameMode.values()) {
					JRadioButton radioButton = new JRadioButton(gameMode.getGameModeName());
					radioButton.setForeground(Color.WHITE);
					radioButton.setOpaque(false);
					radioButton.setActionCommand(gameMode.getGameModeName());
					radioButton.addItemListener(new ItemListener() {
						@Override
						public void itemStateChanged(ItemEvent e) {
							if (e.getStateChange() == ItemEvent.SELECTED) {
								String selectedModeName = buttonGroupGameMode.getSelection().getActionCommand();
								OthelloGameMode selectedGameMode = OthelloGameMode.findByGameModeName(selectedModeName);
								if (selectedGameMode != null) {
									OthelloMainMenu.this.gameModeSelected = selectedGameMode;
								}
								if (selectedGameMode == OthelloGameMode.PLAYER_VS_MACHINE) {
									OthelloMainMenu.this.labelFirstPlayerColor.setText("Choix de votre couleur : ");
									OthelloMainMenu.this.labelSecondPlayerColor.setText("Choix de la couleur de la machine : ");
								} else {
									OthelloMainMenu.this.labelFirstPlayerColor.setText("Choix de la couleur du joueur 1 : ");
									OthelloMainMenu.this.labelSecondPlayerColor.setText("Choix de la couleur du joueur 2 : ");
								}
							}
						}
					});
					buttonGroupGameMode.add(radioButton);
					q.add(radioButton);
				}
				buttonGroupGameMode.setSelected(buttonGroupGameMode.getElements().nextElement().getModel(), true);
				q.setAlignmentX(Component.CENTER_ALIGNMENT);
				q.setMaximumSize(new Dimension(width - 2 * padding, 40));
			}
			p.add(q);
			p.add(Box.createVerticalStrut(padding));

			q = new JPanel(new FlowLayout(FlowLayout.CENTER));
			{
				q.setOpaque(false);
				q.add(labelFirstPlayerColor);
				ButtonGroup buttonGroupChoiceColor = new ButtonGroup();
				radioButtonFirstPlayerColorBlack = new JRadioButton(OthelloPiece.BLACK.getColor().getColorName());
				radioButtonFirstPlayerColorBlack.setForeground(Color.WHITE);
				radioButtonFirstPlayerColorBlack.setOpaque(false);
				radioButtonFirstPlayerColorBlack.setActionCommand(OthelloPiece.BLACK.getColor().getColorName());
				radioButtonFirstPlayerColorBlack.setSelected(true);
				buttonGroupChoiceColor.add(radioButtonFirstPlayerColorBlack);
				q.add(radioButtonFirstPlayerColorBlack);
				radioButtonFirstPlayerColorWhite = new JRadioButton(OthelloPiece.WHITE.getColor().getColorName());
				radioButtonFirstPlayerColorWhite.setForeground(Color.WHITE);
				radioButtonFirstPlayerColorWhite.setOpaque(false);
				radioButtonFirstPlayerColorWhite.setActionCommand(OthelloPiece.WHITE.getColor().getColorName());
				buttonGroupChoiceColor.add(radioButtonFirstPlayerColorWhite);
				q.add(radioButtonFirstPlayerColorWhite);
				q.setAlignmentX(Component.CENTER_ALIGNMENT);
				q.setMaximumSize(new Dimension(width - 2 * padding, 40));
			}
			p.add(q);
			p.add(Box.createVerticalStrut(padding));

			q = new JPanel(new FlowLayout(FlowLayout.CENTER));
			{
				q.setOpaque(false);
				q.add(labelSecondPlayerColor);
				ButtonGroup buttonGroupChoiceColor = new ButtonGroup();
				radioButtonSecondPlayerColorBlack = new JRadioButton(OthelloPiece.BLACK.getColor().getColorName());
				radioButtonSecondPlayerColorBlack.setForeground(Color.WHITE);
				radioButtonSecondPlayerColorBlack.setOpaque(false);
				radioButtonSecondPlayerColorBlack.setActionCommand(OthelloPiece.BLACK.getColor().getColorName());
				buttonGroupChoiceColor.add(radioButtonSecondPlayerColorBlack);
				q.add(radioButtonSecondPlayerColorBlack);
				radioButtonSecondPlayerColorWhite = new JRadioButton(OthelloPiece.WHITE.getColor().getColorName());
				radioButtonSecondPlayerColorWhite.setForeground(Color.WHITE);
				radioButtonSecondPlayerColorWhite.setOpaque(false);
				radioButtonSecondPlayerColorWhite.setActionCommand(OthelloPiece.WHITE.getColor().getColorName());
				radioButtonSecondPlayerColorWhite.setSelected(true);
				buttonGroupChoiceColor.add(radioButtonSecondPlayerColorWhite);
				q.add(radioButtonSecondPlayerColorWhite);
				q.setAlignmentX(Component.CENTER_ALIGNMENT);
				q.setMaximumSize(new Dimension(width - 2 * padding, 40));
			}
			p.add(q);
			p.add(Box.createVerticalStrut(padding));

			labelDifficulty.setForeground(Color.WHITE);
			labelDifficulty.setAlignmentX(Component.CENTER_ALIGNMENT);
			labelDifficulty.setMaximumSize(new Dimension(width - 2 * padding, 30));
			comboBoxDifficulties.setAlignmentX(Component.CENTER_ALIGNMENT);
			comboBoxDifficulties.setMaximumSize(new Dimension(width - 2 * padding, 30));
			buttonPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
			buttonPlay.setMaximumSize(new Dimension(width - 2 * padding, 40));
			buttonRules.setAlignmentX(Component.CENTER_ALIGNMENT);
			buttonRules.setMaximumSize(new Dimension(width - 2 * padding, 40));
			buttonQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
			buttonQuit.setMaximumSize(new Dimension(width - 2 * padding, 40));

			p.add(labelDifficulty);
			p.add(comboBoxDifficulties);
			p.add(Box.createVerticalStrut(padding));
			p.add(buttonPlay);
			p.add(Box.createVerticalStrut(padding));
			p.add(buttonRules);
			p.add(Box.createVerticalStrut(padding));
			p.add(buttonQuit);
			p.add(Box.createVerticalStrut(padding));
		}
		this.frame.add(p, BorderLayout.CENTER);
	}

	private void createController() {
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.buttonPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				new OthelloApplication(OthelloMainMenu.this.gameModeSelected, 
						OthelloSearchAlgorithm.MINMAX,
						OthelloMainMenu.this.difficultySelected,
						OthelloMainMenu.this.pieceFirstPlayerSelected,
						OthelloMainMenu.this.pieceSecondPlayerColorSelected).display();
			}
		});
		this.buttonRules.addActionListener(new ActionListener() {
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
		this.buttonQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
		this.comboBoxDifficulties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedOption = (String) OthelloMainMenu.this.comboBoxDifficulties.getSelectedItem();
				OthelloDifficulty difficulty = OthelloDifficulty.findByName(selectedOption);
				if (difficulty != null) {
					OthelloMainMenu.this.difficultySelected = difficulty;
				}
			}
		});
		this.radioButtonFirstPlayerColorBlack.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					radioButtonSecondPlayerColorBlack.setSelected(false);
					radioButtonSecondPlayerColorWhite.setSelected(true);
					OthelloMainMenu.this.pieceFirstPlayerSelected = OthelloPiece.BLACK;
				}
			}
		});
		this.radioButtonFirstPlayerColorWhite.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					radioButtonSecondPlayerColorBlack.setSelected(true);
					radioButtonSecondPlayerColorWhite.setSelected(false);
					OthelloMainMenu.this.pieceFirstPlayerSelected = OthelloPiece.WHITE;
				}
			}
		});
		this.radioButtonSecondPlayerColorBlack.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					radioButtonFirstPlayerColorBlack.setSelected(false);
					radioButtonFirstPlayerColorWhite.setSelected(true);
					OthelloMainMenu.this.pieceSecondPlayerColorSelected = OthelloPiece.BLACK;
				}
			}
		});
		this.radioButtonSecondPlayerColorWhite.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					radioButtonFirstPlayerColorBlack.setSelected(true);
					radioButtonFirstPlayerColorWhite.setSelected(false);
					OthelloMainMenu.this.pieceSecondPlayerColorSelected = OthelloPiece.WHITE;

				}
			}
		});
	}
	
}
