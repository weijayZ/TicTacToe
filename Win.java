import java.util.Arrays;
import java.util.stream.IntStream;

public enum Win {
	COL_W(0,3,6), COL_M(1,4,7), COL_E(2,5,8),
	ROW_N(0,1,2), ROW_M(3,4,5), ROW_S(6,7,8),
	DIAG_NW_SE(0,4,8), DIAG_NE_SW(2,4,6);

	private final int[] pos;
	Win(int a, int b, int c) {
		this.pos = new int[] {a,b,c};
	}

	public boolean isWin(Player[] board) {
		return getWinner(board) != null;
	}

	public Player getWinner(Player[] board) {
		final Player w = board[pos[0]];
		return (w != null
			&& IntStream.of(pos) // int index
				.mapToObj(i -> board[i]) // player
				.allMatch(p -> w == p)) // boolean
			? w : null;
	}

	public int[] getPos() {
		return Arrays.copyOf(pos, pos.length);
	}
}
