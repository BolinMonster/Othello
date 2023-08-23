package othello.gui.music;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MusicPlayer extends JFrame implements ActionListener {
    
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    
    private Clip clip;
    private AudioInputStream audioInputStream;
    private long clipTimePosition;
    private boolean isPlaying;
    private boolean isPaused;
    private boolean isStopped;
    
    /**
     * Constructeur de la classe MusicPlayer.
     * Initialise les boutons de lecture, de pause et d'arrêt de la musique.
     * Charge le fichier audio à partir du chemin spécifié.
     */
    public MusicPlayer() {
        super("Music Player");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");
        
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        stopButton.addActionListener(this);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(playButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        
        add(buttonPanel, BorderLayout.CENTER);
        pack();
        
        // charger le fichier audio
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File("src/othello/gui/music/music.wav").toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            isPlaying = false;
            isPaused = false;
            isStopped = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Méthode de gestion des événements pour les boutons de lecture, de pause et d'arrêt.
     * @param e L'événement déclenché.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            if (isStopped) {
                clip.setFramePosition(0);
                isStopped = false;
            }
            clip.start();
            isPlaying = true;
        } else if (e.getSource() == pauseButton) {
            if (isPlaying) {
                clipTimePosition = clip.getMicrosecondPosition();
                clip.stop();
                isPaused = true;
                isPlaying = false;
            } else if (isPaused) {
                clip.setMicrosecondPosition(clipTimePosition);
                clip.start();
                isPaused = false;
                isPlaying = true;
            }
        } else if (e.getSource() == stopButton) {
            clip.stop();
            clip.setFramePosition(0);
            isStopped = true;
            isPlaying = false;
            isPaused = false;
        }
    }
    
    public static void main(String[] args) {
    	MusicPlayer frame = new MusicPlayer();
        frame.setVisible(true);
    }

}
