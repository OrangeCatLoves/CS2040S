import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.PriorityQueue;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};

	private Maze maze;
	private boolean[][] visited;
	private int[][] minScaryLevel; // To store the minFearLevel associated with each room, gets updated constantly
	private int endRow, endCol;
	private PriorityQueue<Pair> pq;
	class Pair implements Comparable<Pair> {
		int x;
		int y;
		int currFearLevel;
		public Pair(int x, int y, int currFearLevel) {
			this.x = x;
			this.y = y;
			this.currFearLevel = currFearLevel;
		}
		@Override
		public int compareTo(Pair t) {
			if (this.currFearLevel < t.currFearLevel) {
				return -1;
			} else if (this.currFearLevel > t.currFearLevel) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	public MazeSolver() {
		maze = null;
	}
	@Override
	public void initialize(Maze maze) {
		this.minScaryLevel = new int[maze.getRows()][maze.getColumns()];
		//this.parent = new int[maze.getRows()][maze.getColumns()];
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
		this.maze = maze;
	}
	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}
		if (startRow < 0 || startCol < 0 || startRow >= this.maze.getRows() || startCol >= this.maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= this.maze.getRows() || endCol >= this.maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
		for (int i = 0; i < this.maze.getRows(); ++i) { // Reinitialisation of maze
			for (int j = 0; j < this.maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				this.minScaryLevel[i][j] = Integer.MAX_VALUE;
			}
		}
		this.endRow = endRow;
		this.endCol = endCol;
		this.pq = new PriorityQueue();
		return Dijkstra(startRow, startCol); // Helper function that performs Dijkstra
	}

	public Integer Dijkstra(int startRow, int startCol) { // Should return min fear level associated with destination
		Pair p = new Pair(startRow, startCol, 0); // Starting room pair
		this.pq.add(p);
		this.visited[p.x][p.y] = true; // Starting position is always visited
		this.minScaryLevel[p.x][p.y] = 0; // Starting position associated with fear level 0

		while (!this.pq.isEmpty()) { // Dijkstra
			Pair curr = this.pq.poll(); // Dequeue min room with lowest fear level
			this.visited[curr.x][curr.y] = true; // Currently visited
			Room currRoom = maze.getRoom(curr.x, curr.y);
			// North
			if (canGo(curr.x, curr.y, 0) && currRoom.getNorthWall() != TRUE_WALL && !this.visited[curr.x - 1][curr.y]) {
				relax(curr, currRoom,0);
			}
			// East
			if (canGo(curr.x, curr.y, 1) && currRoom.getEastWall() != TRUE_WALL && !this.visited[curr.x][curr.y + 1]) {
				relax(curr, currRoom,1);
			}
			// West
			if (canGo(curr.x, curr.y, 2) && currRoom.getWestWall() != TRUE_WALL && !this.visited[curr.x][curr.y - 1]) {
				relax(curr, currRoom,2);
			}
			// South
			if (canGo(curr.x, curr.y, 3) && currRoom.getSouthWall() != TRUE_WALL && !this.visited[curr.x + 1][curr.y]) {
				relax(curr, currRoom,3);
			}
		}
		if (minScaryLevel[endRow][endCol] == Integer.MAX_VALUE) {
			return null;
		}
		return minScaryLevel[endRow][endCol];
	}
	public void relax(Pair p, Room r, int direction) {
		int fearLevelWall = 0;
		switch(direction) {
			// North, South, East, West order
			case 0:
				fearLevelWall = r.getNorthWall();
				break;
			case 1:
				fearLevelWall = r.getEastWall();
				break;
			case 2:
				fearLevelWall = r.getWestWall();
				break;
			case 3:
				fearLevelWall = r.getSouthWall();
				break;
		}
		if (fearLevelWall == EMPTY_SPACE) {
			fearLevelWall = 1;
		}
		int nextRow = p.x + DELTAS[direction][0];
		int nextCol = p.y + DELTAS[direction][1];
		int nextFearLevel = minScaryLevel[p.x][p.y] + fearLevelWall;
		int valueOfNextPoint = minScaryLevel[nextRow][nextCol];
		if (nextFearLevel < valueOfNextPoint) {
			// Relax
			minScaryLevel[nextRow][nextCol] = nextFearLevel;
		}

		Pair nextPair = new Pair(nextRow, nextCol, nextFearLevel);
		this.pq.add(nextPair);
	}
	private boolean canGo(int row, int col, int dir) { // Checks for array out of bounds

		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;

		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;
		return true;
	}

	/*public class Pair implements Comparable<Pair> {
		private int x;
		private int y ;
		private Room room = maze.getRoom(x, y);
		private int minfearLevel;
		private Pair parent;
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Pair otherPair) { // Double check on this
			int otherPairFearLevel = otherPair.minfearLevel;
			if (this.minfearLevel > otherPairFearLevel) {
				return 1;
			} else if (this.minfearLevel > otherPairFearLevel) {
				return -1;
			} else {
				return 0;
			}
		}
		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public Pair getParent() {
			return this.parent;
		}

		public void setParent(Pair parent) {
			this.parent = parent;
		}
	}

	private Maze maze;
	private PriorityQueue<Pair> pq;
	private int[][] minFearArr; // Stores min fear level for each room
	private boolean[][] addedtoPQ;

	public MazeSolver() {
		// TODO: Initialize variables.
		this.pq = new PriorityQueue<>();
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		this.minFearArr = new int[this.maze.getRows()][this.maze.getColumns()];
		this.addedtoPQ = new boolean[this.maze.getRows()][this.maze.getColumns()];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception { // Dijkstra
		// TODO: Find minimum fear level.
		//int startingfearLevel = 0;
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				this.addedtoPQ[i][j] = false;
			}
		}
		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				this.minFearArr[i][j] = Integer.MAX_VALUE;
			}
		}
		Pair startPair = new Pair(startRow, startCol);
		this.pq.add(startPair);
		startPair.minfearLevel = 0; // Starting room fear level starts at 0
		this.minFearArr[startRow][startCol] = 0;
		this.addedtoPQ[startRow][startCol] = true;
		while(!this.pq.isEmpty()) { // Should populate minFearArr with minFearLevel
			Pair lowestFearlvlPair = this.pq.poll();
			int currX = lowestFearlvlPair.x;
			int currY = lowestFearlvlPair.y;
			Room currRoom = lowestFearlvlPair.room;

			for (int i = 0; i < 4; i++) { // Check each cardinal direction
				//System.out.println("for loop iterations"); // Printed out 48 times

				// North, South, East, West order, each element in array is [x coord, y coord, wall fear level]
				int[][] direction = {{lowestFearlvlPair.x - 1, lowestFearlvlPair.y, currRoom.getNorthWall()},
						{lowestFearlvlPair.x + 1, lowestFearlvlPair.y, currRoom.getSouthWall()},
						{lowestFearlvlPair.x, lowestFearlvlPair.y + 1, currRoom.getEastWall()},
						{lowestFearlvlPair.x, lowestFearlvlPair.y - 1, currRoom.getWestWall()}};

				if (direction[i][2] != TRUE_WALL && direction[i][0] < this.maze.getRows() && direction[i][0] >= 0
				&& direction[i][1] < this.maze.getColumns() && direction[i][1] >= 0) { // Can traverse, not "#"
					if (!addedtoPQ[direction[i][0]][direction[i][1]]) { // Each node gets added only once to pq
						Pair nextPair = new Pair(direction[i][0], direction[i][1]);
						System.out.println("printed"); // Should be printed 12 times for maze-sample
						//nextPair.minfearLevel = this.minFearArr[currX][currY] + direction[i][2]; // Increment fear level
						this.pq.add(nextPair);
						this.addedtoPQ[direction[i][0]][direction[i][1]] = true; // Don't need to check if pq contains
						// the node since it's already marked as visited even before it's added to the pq

						// Relax
						if (this.minFearArr[direction[i][0]][direction[i][1]] > this.minFearArr[currX][currY] + direction[i][2]) {
							this.minFearArr[direction[i][0]][direction[i][1]] = this.minFearArr[currX][currY] + direction[i][2];
							nextPair.minfearLevel = this.minFearArr[currX][currY] + direction[i][2];
						}
					} else {
						// Relax

						if (this.minFearArr[direction[i][0]][direction[i][1]] > this.minFearArr[currX][currY] + direction[i][2]) {
							this.minFearArr[direction[i][0]][direction[i][1]] = this.minFearArr[currX][currY] + direction[i][2];
						}
					}
				}
			}
		}
		if (!addedtoPQ[endRow][endCol]) {
			return null;
		}
		int minFear = this.minFearArr[endRow][endCol];
		return minFear;
	}*/

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.maze.getRows() || startCol >= this.maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= this.maze.getRows() || endCol >= this.maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		for (int i = 0; i < this.maze.getRows(); ++i) {
			for (int j = 0; j < this.maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				this.minScaryLevel[i][j] = Integer.MAX_VALUE;
			}
		}

		this.endRow = endRow;
		this.endCol = endCol;
		this.pq = new PriorityQueue();
		return Dijkstra1(startRow, startCol);
	}
	public Integer Dijkstra1(int startRow, int startCol) {
		Pair p = new Pair(startRow, startCol, 0);
		this.pq.add(p);
		this.visited[p.x][p.y] = true;
		this.minScaryLevel[p.x][p.y] = 0;

		while (!this.pq.isEmpty()) {
			Pair currPair = this.pq.poll();
			this.visited[currPair.x][currPair.y] = true;
			Room currRoom = maze.getRoom(currPair.x, currPair.y);
			// North
			if (canGo(currPair.x, currPair.y, 0) && currRoom.getNorthWall() != TRUE_WALL && !this.visited[currPair.x - 1][currPair.y]) {
				relax1(currPair, currRoom,0);
			}
			// East
			if (canGo(currPair.x, currPair.y, 1) && currRoom.getEastWall() != TRUE_WALL && !this.visited[currPair.x][currPair.y + 1]) {
				relax1(currPair, currRoom,1);
			}
			// West
			if (canGo(currPair.x, currPair.y, 2) && currRoom.getWestWall() != TRUE_WALL && !this.visited[currPair.x][currPair.y - 1]) {
				relax1(currPair, currRoom,2);
			}
			// South
			if (canGo(currPair.x, currPair.y, 3) && currRoom.getSouthWall() != TRUE_WALL && !this.visited[currPair.x + 1][currPair.y]) {
				relax1(currPair, currRoom,3);
			}
		}
		if (minScaryLevel[endRow][endCol] == Integer.MAX_VALUE) {
			return null;
		}
		return minScaryLevel[endRow][endCol];
	}

	public void relax1(Pair t, Room r, int dir) {
		// The Bonus part 1 question is very similar to part 3 maze. Instead of considering the sum of edge and curr fear level
		// we compare if the fear level of the wall im passing through is more or less than the current fear level.
		// Essentially, it's about finding the shortest path whereby the smallest weighted edge I passed through among all other
		// edges I pass through to be the smallest among all other shortest path
		int nextfearLevelWall = 0;

		switch(dir) {
			case 0:
				nextfearLevelWall= r.getNorthWall();
				break;
			case 1:
				nextfearLevelWall = r.getEastWall();
				break;
			case 2:
				nextfearLevelWall = r.getWestWall();
				break;
			case 3:
				nextfearLevelWall = r.getSouthWall();
				break;
		}

		int nextFearLevelValue = 0;

		if (nextfearLevelWall == EMPTY_SPACE) { // If EMPTY_SPACE just increment
			nextfearLevelWall = 1;
			nextFearLevelValue = minScaryLevel[t.x][t.y] + nextfearLevelWall;
		} else { // Comparing fearlevel, if next wall fear level is higher, reassign value, if not,  fear level stays the same
			// cause the wall ain't scary enough
			int currentLevel = minScaryLevel[t.x][t.y];
			if (currentLevel < nextfearLevelWall) {
				nextFearLevelValue = nextfearLevelWall;
			} else {
				nextFearLevelValue = currentLevel;
			}
		}

		int newRow = t.x + DELTAS[dir][0];
		int newCol = t.y + DELTAS[dir][1];
		minScaryLevel[newRow][newCol] = nextFearLevelValue;
		Pair nextPair = new Pair(newRow, newCol, nextFearLevelValue);
		this.pq.add(nextPair);
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.maze.getRows() || startCol >= this.maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= this.maze.getRows() || endCol >= this.maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		for (int i = 0; i < this.maze.getRows(); ++i) {
			for (int j = 0; j < this.maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				this.minScaryLevel[i][j] = Integer.MAX_VALUE;
			}
		}

		this.endRow = endRow;
		this.endCol = endCol;
		this.pq = new PriorityQueue();
		// Perform Dijkstra twice for between start -> end and special room -> end
		// If start -> end returns null, just return null as there's definitely no path to the end path after all.
		// If special room -> end is not null that means there must exist a path from start to special
		// We only need to consider the shortest path from special to end because the fear level gets reset to -1 anyway.
		// Hence we return min(start -> end, special -> end);
		return Dijkstra2(startRow, startCol, sRow, sCol);
	}

	public Integer Dijkstra2(int startRow, int startCol, int sRow, int sCol) {
		Pair p = new Pair(startRow, startCol, 0);
		this.pq.add(p);
		this.visited[p.x][p.y] = true;
		this.minScaryLevel[p.x][p.y] = 0;

		while (!this.pq.isEmpty()) {
			Pair currPair = this.pq.poll();
			this.visited[currPair.x][currPair.y] = true;
			Room currRoom = maze.getRoom(currPair.x, currPair.y);

			// North
			if (canGo(currPair.x, currPair.y, 0) && currRoom.getNorthWall() != TRUE_WALL && !this.visited[currPair.x - 1][currPair.y]) {
				relax2(currPair, currRoom,0, sRow, sCol);
			}
			// East
			if (canGo(currPair.x, currPair.y, 1) && currRoom.getEastWall() != TRUE_WALL && !this.visited[currPair.x][currPair.y + 1]) {
				relax2(currPair, currRoom,1, sRow, sCol);
			}
			// West
			if (canGo(currPair.x, currPair.y, 2) && currRoom.getWestWall() != TRUE_WALL && !this.visited[currPair.x][currPair.y - 1]) {
				relax2(currPair, currRoom,2, sRow, sCol);
			}
			// South
			if (canGo(currPair.x, currPair.y, 3) && currRoom.getSouthWall() != TRUE_WALL && !this.visited[currPair.x + 1][currPair.y]) {
				relax2(currPair, currRoom,3, sRow, sCol);
			}
		}
		if (minScaryLevel[endRow][endCol] == Integer.MAX_VALUE) {
			return null;
		}
		return minScaryLevel[endRow][endCol];
	}

	public void relax2(Pair t, Room r, int dir, int sRow, int sCol) {

		int nextFearLevelWall = 0;

		switch(dir) {
			case 0:
				nextFearLevelWall = r.getNorthWall();
				break;
			case 1:
				nextFearLevelWall = r.getEastWall();
				break;
			case 2:
				nextFearLevelWall = r.getWestWall();
				break;
			case 3:
				nextFearLevelWall = r.getSouthWall();
				break;
		}

		int nextFearLevelValue = 0; // To be reassigned

		if (nextFearLevelWall == EMPTY_SPACE) {
			nextFearLevelWall = 1;
			nextFearLevelValue = minScaryLevel[t.x][t.y] + nextFearLevelWall;
		} else {
			int currentLevel = minScaryLevel[t.x][t.y];
			if (currentLevel < nextFearLevelWall) {
				nextFearLevelValue = nextFearLevelWall;
			} else {
				nextFearLevelValue = currentLevel;
			}
		}

		int newRow = t.x + DELTAS[dir][0];
		int newCol = t.y + DELTAS[dir][1];
		if (newRow == sRow && newCol == sCol) {
			minScaryLevel[newRow][newCol] = -1;
			Pair nextPair = new Pair(newRow, newCol, -1);
			this.pq.add(nextPair);
		} else {
			minScaryLevel[newRow][newCol] = nextFearLevelValue;
			Pair nextPair = new Pair(newRow, newCol, nextFearLevelValue);
			this.pq.add(nextPair);
		}
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 1, 5));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
