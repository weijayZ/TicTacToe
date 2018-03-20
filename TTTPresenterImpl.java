import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class TTTPresenterImpl implements TTTPresenter {

	private final static Logger LOG = Logger.getLogger(TTTPresenterImpl.class.getName());

	private final static String PROPERTY_NAME = TTTPresenter.class.getName();
	private final PropertyChangeSupport listeners;
	private TTTModel model;

	public TTTPresenterImpl() {
		listeners = new PropertyChangeSupport(this);
		model = new TTTModel();
	}

	/** A player takes a square. Ignores illegal moves. Detemines if the round
		is over, sets fields appropriately, updates score. Fires an event.*/
	public void move(Player p, int pos) {
		if(model.board[pos] == null && model.whosMove == p){
			model.board[pos] = p;
			model.whosMove = p.getNext();
			model.win = getWin();
			model.winner = getWinner();
			model.readyForNextRound[0] = false;
			model.readyForNextRound[1] = false;
			//increments score when round is over
			if(isRoundOver()){
				if(model.winner == Player.X){
					model.score[0] ++;
				} else if (model.winner == Player.O)  {
					model.score[1] ++;
				}
			}
			fireEvent();
		}
	}

	/** Gets the score for all players over all rounds. */
	public int[] getScore() {
		return model.score;
	}

	/** Gets the current play board. */
	public Player[] getBoard() {
		return model.board;
	}

	/** Returns the player that should move this turn; null if round is over.*/
	public Player getWhosMove() {
		if(isRoundOver()){
			return null;
		} else{
			return model.whosMove;
		}
	}

	/** Gets the winning scenario of the current round; null if no winner. */
	public Win getWin() {
		return Arrays.stream(Win.values())
			.filter(w -> w.isWin(model.board))
			.findAny()
			.orElse(null);
	}

	/** Gets the winner of the current round; null if there is no winner. */
	public Player getWinner() {
		if(model.win == null){
			return null;
		} else {
			return model.win.getWinner(model.board);
		}
	}

	/** Gets if the round is over. */
	public boolean isRoundOver() {
		//loops through board and counts all used spaces, returns true is all used and no winner
		int count = 0;
		for(Player p : model.board){
			if(p != null){
				count ++;
			}
		}
		
		if(count == 9 || model.winner != null){
			
			return true;
		} else{
			return false;
		}
	}

	/** Gets if the round is a draw. */
	//returns true if round is over with no winner
	public boolean isDraw() {
		if(model.winner == null && isRoundOver() == true){
			return true;
		} else{
			return false;
		}
	}

	/** A player indicates that they are ready for the next round.
		When all players are ready, the next round begins.
		Fires an event when next round begins.*/
	public void nextRound(Player p) {
		if(p == Player.X) {
			model.readyForNextRound[0] = true;
			resetEvent();
		} else if(p == Player.O) {
			model.readyForNextRound[1] = true;
			resetEvent();
		} 
	}

	/** Adds a listener to receive game state changes.
		Fires an event on add.
		The "source" on fired events should be this TTTPresenter
		The "property name" of fired events should be "TTTPresenter"
	*/
	public void addListener(PropertyChangeListener l) {
		this.listeners.addPropertyChangeListener(l);
	}
	
	//Helper to fire eventchanges
	private void fireEvent() {
		listeners.firePropertyChange(PROPERTY_NAME, null, this);
	}

	//checks if both players are ready and sets the new board for next game.
	private void resetEvent(){
		if(model.readyForNextRound[0] == true && model.readyForNextRound[1] == true) {
			if(model.winner != null){
				model = new TTTModel(model.winner.getNext(), model.score);
			} else{
				model = new TTTModel(model.movesFirst.getNext(), model.score);
			}
			
			System.out.println(Arrays.toString(model.board));
			fireEvent();
		}
	}


	/** A String representation of this object. */
	public String toString() {
		return this.getClass().getName() + ':' + Objects.toString(model);
	}
}
