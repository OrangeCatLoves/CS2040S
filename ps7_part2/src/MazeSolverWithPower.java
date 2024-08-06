import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};

	private Maze maze;
	private Map<Integer, Set<PowerPair>> map; // To preprocess data for numReachable so there's no re-computation
	public class Pair { // Pair class for no power
		private int x;
		private int y ;
		private Pair parent;
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
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

	public class PowerPair { // Pair class for with power
		public int x;
		public int y;
		public PowerPair parent;
		public int powerUsed;
		public Room room;
		public int step;


		PowerPair(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public PowerPair(int x, int y, Room room, int powerUsed) {
			this.x = x;
			this.y = y;
			this.room = room;
			this.powerUsed = powerUsed;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public PowerPair getParent() {
			return parent;
		}

		public void setParent(PowerPair parent) {
			this.parent = parent;
		}
	}
	public MazeSolverWithPower() {
		// TODO: Initialize variables.
		map = new HashMap<>();
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find shortest path.
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		map.clear();
		for (int row = 0; row < maze.getRows(); row++) {
			for (int col = 0; col < maze.getColumns(); col++) {
				maze.getRoom(row, col).onPath = false;
			}
		}

		Queue<PowerPair> currFrontier = new LinkedList<>();
		boolean[][][] powerVisited = new boolean[maze.getRows()][maze.getColumns()][1];
		PowerPair startPair = new PowerPair(startCol, startRow, maze.getRoom(startRow, startCol), 0);
		PowerPair finalPosition = null; // To be reassigned
		powerVisited[startPair.y][startPair.x][0] = true; // Starting position would always be marked true
		currFrontier.add(startPair);
		updateDist(startPair);
		addtoSet(startPair);

		if (startRow == endRow && startCol == endCol) {
			finalPosition = startPair;
		}

		while (!currFrontier.isEmpty()) {
			PowerPair curr = currFrontier.poll();
			Room room = curr.room;
			int[][] possibleCoords = {{curr.x + 1, curr.y, room.hasEastWall() ? curr.powerUsed + 1 : curr.powerUsed},
					{curr.x - 1, curr.y, room.hasWestWall() ? curr.powerUsed + 1 : curr.powerUsed},
					{curr.x, curr.y + 1, room.hasSouthWall() ? curr.powerUsed + 1 : curr.powerUsed},
					{curr.x, curr.y - 1, room.hasNorthWall() ? curr.powerUsed + 1 : curr.powerUsed}};

			for (int[] possible: possibleCoords) {
				int row = possible[1];
				int col = possible[0];
				int powerUsed = possible[2];
				if (canGo(possible, 0)) {
					if (!powerVisited[row][col][powerUsed]) {
						PowerPair next = new PowerPair(col, row, maze.getRoom(row, col), powerUsed);
						next.parent = curr;
						powerVisited[row][col][powerUsed] = true;
						currFrontier.add(next);
						updateDist(next);
						addtoSet(next);
						if (row == endRow && col == endCol && // Or that there's an even shorter with less/more powers used
								(finalPosition == null || shortestPathLen(startPair, next) < shortestPathLen(startPair, finalPosition))) {
							finalPosition = next;
						}
					}
				}
			}
		}

		if (finalPosition == null) {
			return null;
		}
		int result = 0;
		while (finalPosition != startPair) {
			result++;
			maze.getRoom(startPair.y, startPair.x).onPath = true;
			finalPosition = finalPosition.parent;
		}
		return result;
	}

	private void updateDist(PowerPair p) { // Update the shortest distance required w.r.t to its parent pair
		if (p.parent == null) {
			p.step = 0;
		} else {
			p.step = p.parent.step + 1;
		}
	}

	private void addtoSet(PowerPair p) {
		if (!map.containsKey(p.step)) {
			Set<PowerPair> xs = new HashSet<>();
			xs.add(p);
			map.put(p.step, xs);
		} else {
			map.get(p.step).add(p);
		}
	}

	private boolean canGo(int[] arr, int numOfSuperpowersGiven) {
		int col = arr[0];
		int row = arr[1];
		int powerUsed = arr[2];
		return row >= 0 && row < maze.getRows()
				&& col >= 0 && col < maze.getColumns()
				&& powerUsed <= numOfSuperpowersGiven;
	}

	private Integer shortestPathLen(PowerPair startPos, PowerPair endPos) {
		if (endPos == null) {
			return null;
		}
		int result = 0;
		while (endPos != startPos) {
			result++;
			endPos = endPos.parent;
		}
		return result;
	}


	@Override
	public Integer numReachable(int k) throws Exception {
		// TODO: Find number of reachable rooms.
		return map.get(k) == null ? 0 : map.get(k).size();
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		// TODO: Find shortest path with powers allowed.
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		map.clear(); // Reinitialise map
		for (int row = 0; row < maze.getRows(); row++) { // Reinitialise all the arrays with default values
			for (int col = 0; col < maze.getColumns(); col++) {
				maze.getRoom(row, col).onPath = false;
			}
		}

		Queue<PowerPair> queue = new LinkedList<>();

		boolean[][][] powerVisited = new boolean[maze.getRows()][maze.getColumns()][superpowers + 1];
		// To keep track of every scenario in each room given how many superPowers I have used up
		boolean[][] visited = new boolean[maze.getRows()][maze.getColumns()];
		// To keep track of min distance required to travel to a room

		PowerPair startPair = new PowerPair(startCol, startRow, maze.getRoom(startRow, startCol), 0);
		PowerPair finalPosition = null; // To be reassigned if destination is reachable

		powerVisited[startPair.y][startPair.x][0] = true; // Starting position is always visited with no superpower used
		visited[startPair.y][startPair.x] = true; // Starting position is always visited

		queue.add(startPair);
		updateDist(startPair);
		addtoSet(startPair);

		if (startRow == endRow && startCol == endCol) // Check for edge cases
			finalPosition = startPair;

		while (!queue.isEmpty()) {
			PowerPair curr = queue.poll();
			Room room = curr.room;

			int[][] possibleDirections = {{curr.x + 1, curr.y, room.hasEastWall() ? curr.powerUsed + 1 : curr.powerUsed},
					{curr.x - 1, curr.y, room.hasWestWall() ? curr.powerUsed + 1 : curr.powerUsed},
					{curr.x, curr.y + 1, room.hasSouthWall() ? curr.powerUsed + 1 : curr.powerUsed},
					{curr.x, curr.y - 1, room.hasNorthWall() ? curr.powerUsed + 1 : curr.powerUsed}};

			for (int[] direction : possibleDirections) {
				int row = direction[1]; // Coordinates of next point y
				int col = direction[0]; // Coordinates of next point x
				int powerUsed = direction[2];
				if (canGo(direction, superpowers)) {
					if (!powerVisited[row][col][powerUsed]) {
						PowerPair next = new PowerPair(col, row,
								maze.getRoom(row, col), powerUsed);
						next.parent = curr;
						powerVisited[row][col][powerUsed] = true;
						queue.add(next);
						updateDist(next);
						if (!visited[row][col]) {
							visited[row][col] = true;
							addtoSet(next);
						}
						if (row == endRow && col == endCol &&
								(finalPosition == null || shortestPathLen(startPair, next) < shortestPathLen(startPair, finalPosition)))
							finalPosition = next;
					}
				}
			}
		}

		if (finalPosition == null) { // If destination cannot be reached, finalPosition will be not be reassigned
			return null;
		}
		int result = 0;
		while (finalPosition != startPair) { // Calculation of distance
			result++;
			maze.getRoom(startPair.y, startPair.x).onPath = true;
			finalPosition = finalPosition.parent;
		}
		return result;
	}

	/*private Maze maze;
	private Map<Integer, Set<PowerPair>> map;
	private boolean solved;
	private boolean[][] visited;
	private List<Integer> resultList;
	public class Pair {
		private int x;
		private int y ;
		private Pair parent;
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
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
	public class PowerPair {
		private int x;
		private int y ;
		private PowerPair parent;
		private Room room;
		private int distance;
		private int numPowersUsed;
		public PowerPair(int x, int y, Room room, int numPowersUsed) {
			this.x = x;
			this.y = y;
			this.room = room;
			this.numPowersUsed = numPowersUsed;
		}
		public int getX() {
			return this.x;
		}
		public int getY() {
			return this.y;
		}
		public PowerPair getParent() {
			return this.parent;
		}
		public void setParent(PowerPair pair) {
			this.parent = pair;
		}
	}
	public MazeSolverWithPower() {
		// TODO: Initialize variables.
		this.maze = null;
		this.solved = false;
		this.map = new HashMap<>();
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.solved = false;
		this.maze = maze;
		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find shortest path.
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				this.visited[i][j] = false;
				this.maze.getRoom(i, j).onPath = false;
			}
		}

		this.map = null;
		this.solved = false;
		//this.superPower = Integer.MAX_VALUE;
		Pair finalPosition = null; // Final position to be reassigned with the final pair of coordinates
		int depth = 0;
		int shortestPathLen = 0; // To be reassigned as well

		List<Integer> list = new ArrayList<Integer>();
		Queue<Pair> currFrontier = new LinkedList<Pair>(); // Queue for BFS traversal
		Pair p = new Pair(startRow, startCol); // Starting pair, indicating the starting position
		this.visited[startRow][startCol] = true;
		this.maze.getRoom(startRow, startCol).onPath = true; // Starting position guaranteed to be on path
		currFrontier.add(p);
		p.setParent(null); // Starting node has no parent node

		while(!currFrontier.isEmpty()) { // BFS traversal

			Queue<Pair> newFrontier = new LinkedList<Pair>();
			list.add(currFrontier.size());
			for (Pair currPair: currFrontier) {
				if (currPair.getX() == endRow && currPair.getY() == endCol) { //Found
					this.solved = true;
					finalPosition = currPair;
					shortestPathLen = depth;
				}

				this.visited[currPair.getX()][currPair.getY()] = true;
				if (!this.maze.getRoom(currPair.getX(), currPair.getY()).hasNorthWall()) {
					if (!this.visited[currPair.getX() - 1][currPair.getY()]) {
						Pair pairNorth = new Pair(currPair.getX() - 1, currPair.getY());
						newFrontier.add(pairNorth);
						this.visited[currPair.getX() - 1][currPair.getY()] = true;
						pairNorth.setParent(currPair);
					}
				}
				if (!this.maze.getRoom(currPair.getX(), currPair.getY()).hasEastWall()) {
					if (!this.visited[currPair.getX()][currPair.getY() + 1]) {
						Pair pairEast = new Pair(currPair.getX(), currPair.getY() + 1);
						newFrontier.add(pairEast);
						this.visited[currPair.getX()][currPair.getY() + 1] = true;
						pairEast.setParent(currPair);
					}
				}
				if (!this.maze.getRoom(currPair.getX(), currPair.getY()).hasSouthWall()) {
					if (!this.visited[currPair.getX() + 1][currPair.getY()]) {
						Pair pairSouth = new Pair(currPair.getX() + 1, currPair.getY());
						newFrontier.add(pairSouth);
						this.visited[currPair.getX() + 1][currPair.getY()] = true;
						pairSouth.setParent(currPair);
					}
				}
				if (!this.maze.getRoom(currPair.getX(), currPair.getY()).hasWestWall()) {
					if (!this.visited[currPair.getX()][currPair.getY() - 1]) {
						Pair pairWest = new Pair(currPair.getX(), currPair.getY() - 1);
						newFrontier.add(pairWest);
						this.visited[currPair.getX()][currPair.getY() - 1] = true;
						pairWest.setParent(currPair);
					}
				}
			}
			currFrontier = newFrontier;
			depth++;
		}

		this.resultList = list;

		if (this.solved) {

			while(finalPosition.getParent() != null) {
				this.maze.getRoom(finalPosition.getX(), finalPosition.getY()).onPath = true;
				Pair parentPair = finalPosition.getParent();
				finalPosition= parentPair;
			}
			return shortestPathLen;
		}
		return null;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		// TODO: Find number of reachable rooms.
		if (this.map == null) {
			if (k > this.resultList.size() - 1) {
				return 0;
			}
			List<Integer> list = this.resultList;
			Integer result = list.get(k);
			return result;
		} else {
			if (this.map.get(k) == null) {
				return 0;
			} else {
				int result = this.map.get(k).size();
				return result;
			}
		}
	}
	public boolean canGo(int[] arr, int numOfSupoerpowers) {
		int nextX = arr[0];
		int nextY = arr[1];
		int numofUsedPow = arr[2];
		if ((nextX >= 0 && nextX < this.maze.getRows()) &&
				(nextY >= 0 && nextY < this.maze.getColumns()) && numofUsedPow <= numOfSupoerpowers) { // Change to >= for remainPower
			return true;
		}
		return false;
	}

	public void addtoMap(PowerPair p) {
		if (!this.map.containsKey(p.distance)) {
			//System.out.println(j);
			//j++;
			Set<PowerPair> hashset = new HashSet<PowerPair>();
			hashset.add(p);
			this.map.put(p.distance, hashset);
		} else { // There already exists a hashset in the key p.distance
			this.map.get(p.distance).add(p);
		}
	}

	public void updateDistNode(PowerPair p) {
		if (p.getParent() == null) {
			p.distance = 0;
		} else {
			Integer distParent = p.getParent().distance;
			p.distance = distParent + 1;
		}
	}

	private Integer shortestPath(PowerPair start, PowerPair end) {
		if (end == null) {
			return null;
		}
		int result = 0;
		while (end != start) {
			result++;
			end = end.parent;
		}
		return result;
	}

	private void createPath(PowerPair start, PowerPair end) {
		if (end == null) {
			return;
		}
		maze.getRoom(start.x, start.y).onPath = true;
		while (end != start) {
			maze.getRoom(start.x, start.y).onPath = true;
			end = end.parent;
		}
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		// TODO: Find shortest path with powers allowed.


		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				this.visited[i][j] = false;
				this.maze.getRoom(i, j).onPath = false;
			}
		}

		this.map.clear();


		Queue<PowerPair> queue = new LinkedList<>();
		boolean[][][] visited = new boolean[maze.getRows()][maze.getColumns()][superpowers + 1];
		boolean[][] step = new boolean[maze.getRows()][maze.getColumns()];
		PowerPair start = new PowerPair(startRow, startCol, maze.getRoom(startRow, startCol), 0);
		PowerPair end = null;
		visited[start.x][start.y][0] = true;
		step[start.x][start.y] = true;
		queue.add(start);
		updateDistNode(start);
		addtoMap(start);

		if (startRow == endRow && startCol == endCol)
			end = start;

		while (!queue.isEmpty()) {
			PowerPair curr = queue.poll();
			Room room = curr.room;
			int[][] possibleCoords = {{curr.x + 1, curr.y, room.hasEastWall() ? curr.numPowersUsed + 1 : curr.numPowersUsed},
					{curr.x - 1, curr.y, room.hasWestWall() ? curr.numPowersUsed + 1 : curr.numPowersUsed},
					{curr.x, curr.y + 1, room.hasSouthWall() ? curr.numPowersUsed + 1 : curr.numPowersUsed},
					{curr.x, curr.y - 1, room.hasNorthWall() ? curr.numPowersUsed + 1 : curr.numPowersUsed}};
			for (int[] possible : possibleCoords) {
				int row = possible[0];
				int col = possible[1];
				int powerUsed = possible[2];
				if (canGo(possible, superpowers)) {
					if (!visited[row][col][powerUsed]) {
						PowerPair next = new PowerPair(row, col,
								maze.getRoom(row, col), powerUsed);
						next.parent = curr;
						visited[row][col][powerUsed] = true;
						queue.add(next);
						updateDistNode(next);
						if (!step[row][col]) {
							step[row][col] = true;
							addtoMap(next);
						}
						if (row == endRow && col == endCol &&
								(end == null || shortestPath(start, next) < shortestPath(start, end)))
							end = next;
					}
				}
			}
		}
		createPath(start, end);
		return shortestPath(start, end);
	}*/

	/*@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		// TODO: Find shortest path with powers allowed.

		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()];

		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				this.visited[i][j] = false;
				this.maze.getRoom(i, j).onPath = false;
			}
		}

		this.map.clear();
		for (int i = 0; i < this.maze.getRows(); i++) {
			for (int j = 0; j < this.maze.getColumns(); j++) {
				this.maze.getRoom(i, j).onPath = false;
			}
		}

		Queue<PowerPair> currFrontier = new LinkedList<>();
		boolean[][][] powerVisited = new boolean[this.maze.getRows()][this.maze.getColumns()][superpowers + 1];
		//boolean[][] justVisited = new boolean[this.maze.getRows()][this.maze.getColumns()];
		PowerPair startPair = new PowerPair(startRow, startCol, this.maze.getRoom(startRow, startCol), 0); //Powers here represent number of powers used
		PowerPair finalPosition = null; // To be reassigned with the end point if there is
		visited[startRow][startCol] = true;
		powerVisited[startRow][startCol][0] = true;
		currFrontier.add(startPair);
		updateDistNode(startPair);
		addtoMap(startPair);

		if (startRow == endRow && startCol == endCol) {
			finalPosition = startPair;
		}

		while(!currFrontier.isEmpty()) {
			PowerPair currPowerPair = currFrontier.poll(); // Dequeue
			Room currRoom = currPowerPair.room;
			// North, South, East, West => coordinates, and computation of the remaining powers in each direction with wall/no wall
			int[][] directions = {{currPowerPair.getX() + DELTAS[0][0], currPowerPair.getY() + DELTAS[0][1], currPowerPair.room.hasNorthWall() ? currPowerPair.numPowers + 1 : currPowerPair.numPowers},
					{currPowerPair.getX() + DELTAS[1][0], currPowerPair.getY() + DELTAS[1][1], currPowerPair.room.hasEastWall() ? currPowerPair.numPowers + 1 : currPowerPair.numPowers},
					{currPowerPair.getX() + DELTAS[2][0], currPowerPair.getY() + DELTAS[2][1], currPowerPair.room.hasSouthWall() ? currPowerPair.numPowers + 1 : currPowerPair.numPowers},
					{currPowerPair.getX() + DELTAS[3][0], currPowerPair.getY() + DELTAS[3][1], currPowerPair.room.hasWestWall() ? currPowerPair.numPowers + 1 : currPowerPair.numPowers}};

			for (int[] direction: directions) {
				int row = direction[0];
				int col = direction[1];
				int numPowerUsed = direction[2];
				if (canGo(direction, superpowers)) {
					if (!powerVisited[row][col][numPowerUsed]) {
						powerVisited[row][col][numPowerUsed] = true;
						PowerPair nextPair = new PowerPair(row, col, this.maze.getRoom(row, col), numPowerUsed);
						currFrontier.add(nextPair);
						nextPair.setParent(currPowerPair);
						updateDistNode(nextPair);

						if (!this.visited[row][col]) {
							this.visited[row][col] = true;
							addtoMap(nextPair);
						}

						if (row == endRow && col == endCol && (finalPosition == null || shortestDist(startPair, nextPair) < shortestDist(startPair, finalPosition))) {
							finalPosition = nextPair;
						}
					}
				}
			}
		}
		createPath(startPair, finalPosition);
		return shortestPath(startPair, finalPosition);
	}*/



	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 4, 4, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
