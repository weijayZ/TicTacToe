import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.stream.*;

public class TTTPresenterTester {

	public static void main(String[] args) {
		new TestRunner(TTTPresenterTester.class).run();
	}

	/** Does a toString of objects, and of arrays */
	public static String deepToString(Object o) {
		return o == null ? null :
			!o.getClass().isArray() ? o.toString() :
			o instanceof Object[] ? Arrays.toString((Object[])o) :
			o instanceof int[] ? Arrays.toString((int[])o) :
			o instanceof double[] ? Arrays.toString((double[])o) :
			o instanceof float[] ? Arrays.toString((float[])o) :
			o instanceof char[] ? Arrays.toString((char[])o) :
			o instanceof long[] ? Arrays.toString((long[])o) :
			o instanceof byte[] ? Arrays.toString((byte[])o) :
			o instanceof short[] ? Arrays.toString((short[])o) :
			o instanceof boolean[] ? Arrays.toString((boolean[])o) :
			o.toString();
	}
	private static void assertEquals(Object lhs, Object rhs) {
		assertEquals(lhs, rhs, "");
	}
	private static void assertEquals(Object lhs, Object rhs, String msg) {
		assert Objects.deepEquals(lhs, rhs) :
			msg
			+ "\nlhs: " + deepToString(lhs)
			+ "\nrhs: " + deepToString(rhs);
	}

	public void test_init() {
		TTTPresenter p = new TTTPresenterImpl();
		assertEquals(new int[] {0,0}, p.getScore());
		assertEquals(new Player[9], p.getBoard());
		assertEquals(Player.X, p.getWhosMove());
		assertEquals(null, p.getWin());
		assertEquals(null, p.getWinner());
		assertEquals(false, p.isRoundOver());
		assertEquals(false, p.isDraw());
	}

	public void test_move_Legal() {
		TTTPresenter p = new TTTPresenterImpl();
		p.move(Player.X, 4); // legal: the middle

		Player[] expected = new Player[9];
		expected[4] = Player.X;
		assertEquals(new int[] {0,0}, p.getScore());
		assertEquals(expected, p.getBoard());
		assertEquals(Player.O, p.getWhosMove());
		assertEquals(null, p.getWin());
		assertEquals(null, p.getWinner());
		assertEquals(false, p.isRoundOver());
		assertEquals(false, p.isDraw());
	}

	public void test_move_illegal_notYourTurn() {
		TTTPresenter p = new TTTPresenterImpl();
		p.move(Player.O, 4); // illegal, X's turn, not O's
		// no change to state
		assertEquals(new int[] {0,0}, p.getScore());
		assertEquals(new Player[9], p.getBoard());
		assertEquals(Player.X, p.getWhosMove());
		assertEquals(null, p.getWin());
		assertEquals(null, p.getWinner());
		assertEquals(false, p.isRoundOver());
		assertEquals(false, p.isDraw());
	}

	public void test_win() {
		TTTPresenter p = new TTTPresenterImpl();
		p.move(Player.X, 0); 
		p.move(Player.O, 1); 
		p.move(Player.X, 2); 
		p.move(Player.O, 3); 
		p.move(Player.X, 4); 
		p.move(Player.O, 5); 
		p.move(Player.X, 6); 
	
		assertEquals(new int[] {1,0}, p.getScore());//test score increase
		assertEquals(Win.DIAG_NE_SW, p.getWin());//test win scenario
		assertEquals(Player.X, p.getWinner());
		assertEquals(true, p.isRoundOver());
		assertEquals(false, p.isDraw());

		//test the restart with a win
		p.nextRound(Player.X);
		p.nextRound(Player.O);
		assertEquals(new Player[9], p.getBoard());//test if new board is made
		assertEquals(Player.O, p.getWhosMove());//test if loser gets the next move
		assertEquals(new int[] {1,0}, p.getScore());
	}
	
	public void test_draw() {
		TTTPresenter p = new TTTPresenterImpl();
		p.move(Player.X, 0); 
		p.move(Player.O, 1); 
		p.move(Player.X, 2); 
		p.move(Player.O, 4); 
		p.move(Player.X, 3); 
		p.move(Player.O, 5); 
		p.move(Player.X, 7);
		p.move(Player.O, 6); 
		p.move(Player.X, 8);
		
		assertEquals(new int[] {0,0}, p.getScore());//test score 
		assertEquals(null, p.getWin());
		assertEquals(null, p.getWinner());
		assertEquals(true, p.isRoundOver());
		assertEquals(true, p.isDraw());

		//Test restart on a draw
		p.nextRound(Player.X);
		p.nextRound(Player.O);

		assertEquals(new int[] {0,0}, p.getScore());//Draw scenario so score doesn't increase
		assertEquals(new Player[9], p.getBoard());//Checks if board is reset
		assertEquals(Player.O, p.getWhosMove());//Test that it is the second player's turn to move first
		assertEquals(null, p.getWin());
		assertEquals(null, p.getWinner());
		assertEquals(false, p.isRoundOver());
		assertEquals(false, p.isDraw());
	}
}
