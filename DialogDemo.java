import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

// Demo of incokeLater on the Event Dispatch Thread
@SuppressWarnings("serial")
public class DialogDemo extends JComponent {

	private final Pres p;

	public DialogDemo(Pres p) {
		this.p = p;

		final JButton b = new JButton("Drink me");
		setLayout(new GridLayout(1,1));
		add(b);
		b.addActionListener(ae -> {
			p.update();
		});

		p.addListener(ce -> {
			b.setEnabled(false);
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(this, this.toString());
			});
		});
	}

	public static void main(String[] args) {
		Pres p = new Pres();
		for(int i = 0; i < 2; i++) {
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setContentPane(new DialogDemo(p));
			f.setMinimumSize(new Dimension(400,400));
			f.setVisible(true);
		}
	}

	private static class Pres {

		List<ChangeListener> listeners = new LinkedList<ChangeListener>();

		public void update() {
			listeners.stream().sequential()
				.forEachOrdered(cl -> {
					cl.stateChanged(null);
				});
		}

		public void addListener(ChangeListener cl) {
			listeners.add(cl);
		}
	}
}
