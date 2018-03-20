import java.awt.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.*;

import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

@SuppressWarnings("serial")
public class TTTComponent extends JComponent {

	private final TTTPresenter game;
	private final Player me;

	//Makes the board
	public TTTComponent(TTTPresenter game, Player player) {
		this.game = game;
		this.me = player;

		setLayout(new BorderLayout());
		JPanel outline= new JPanel();
		add(outline);
				
		//Event listener to check win or draw
		game.addListener(ce -> {
			if(game.getWin() != null){
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(this, game.getWinner() + " wins.");
					game.nextRound(me);
				});
			} else if(game.isDraw() == true){
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(this, "Draw.");
					game.nextRound(me);
				});
			}
		});

		//panel that displays the stats and event handlers to change them.
		JPanel stats= new JPanel();
		add(stats, BorderLayout.NORTH);
		stats.setLayout(new GridLayout(1,3));
		String[] dat = {"You are " + me, game.getWhosMove() + "'s move", "Score: X:" + game.getScore()[0] + " O: " + game.getScore()[1]};
		for ( int i = 0; i < dat.length; i++){
			final int textStuff = i;
			JLabel stuff = new JLabel(dat[i]);
			game.addListener(pce -> {
				String[] dat2 = {"You are " + me, game.getWhosMove() + "'s move", "Score: X:" + game.getScore()[0] + " O: " + game.getScore()[1]};
				if(game.getWinner() != null){
					dat2[1] = game.getWinner() + " Wins.";
				} else if(game.isDraw()){
					dat2[1] = "Draw.";
				}
				stuff.setText(dat2[textStuff]);
			});
			stats.add(stuff);
		}

		//Panel that contains the buttons with event handlers to change them
		JPanel grid= new JPanel();
		add(grid, BorderLayout.CENTER);
		grid.setLayout(new GridLayout(3,3));

		for(int i = 0; i < game.getBoard().length; i++){
			final int index = i;
				JButton button = new JButton(" ");
				button.addActionListener( ae -> {
							if(me == game.getWhosMove()){
							game.move(game.getWhosMove(), index);
							}
			    	});
				game.addListener(pce -> {
						Player ps = game.getBoard()[index];
						String s;
						if(ps == null){
							s = " ";
							button.setEnabled(true);
						} else{
							s = ps.toString();
							button.setEnabled(false);
						}
						button.setText(s);
				});
			grid.add(button);
		}
	}
}
