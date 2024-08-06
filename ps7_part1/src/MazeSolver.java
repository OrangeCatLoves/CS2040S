import java.util.*;
import java.util.List;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private boolean solved;
	private boolean[][] visited;
	//private int endRow, endCol;
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

	public MazeSolver() {
		// TODO: Initialize variables.
		this.maze = null;
		this.solved = false;
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.solved = false;
		this.maze = maze;
		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()];
		//this.resultList = null;
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
		//this.endRow = endRow;
		//this.endCol = endCol;
		this.solved = false;

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
		//tracePath(startRow, startCol, endRow, endCol, queue, list); Trace the path, turning the boolean flags to true
		//Integer count = highlightPath(startRow, startCol, this.resultList, this.maze); Highlights the shortest path
		//with resultStack
	}

	/*public void tracePath(int startRow, int startCol, int endRow, int endCol, Queue<Pair> queue, List<Integer> l) {
		this.visited[startRow][startCol] = true;
		//Room currRoom = this.maze.getRoom(startRow, startCol);
		Queue<Pair> currFrontier = queue; // Starting queue with first starting room being added to queue
		Queue<Pair> nextFrontier = queue; // Will be reassigned after the next frontier is generated

		while(!currFrontier.isEmpty()) { // To populate next frontier for BFS traversal for all of each individual rooms
			if (startRow == endRow && startCol == endCol) {
				this.resultList = l; // If never reach here means that there's no shortest path available
			}
			Queue<Pair> newFrontier = new LinkedList<Pair>();
			for (Pair pair : currFrontier) { // For every room in the queue...
				if (!this.maze.getRoom(pair.getX(), pair.getY()).hasEastWall()) {
					if (!this.visited[startRow][startCol + 1]) {
						newFrontier.add(new Pair(startRow, startCol + 1));
						//this.visited[startRow][startCol + 1] = true;
					}
				} else if (!this.maze.getRoom(pair.getX(), pair.getY()).hasSouthWall()) {
					if (!this.visited[startRow + 1][startCol]) {
						newFrontier.add(new Pair(startRow + 1, startCol));
						//this.visited[startRow + 1][startCol] = true;
					}
				} else if (!this.maze.getRoom(pair.getX(), pair.getY()).hasNorthWall()) {
					if (!this.visited[startRow - 1][startCol]) {
						newFrontier.add(new Pair(startRow - 1, startCol));
						//this.visited[startRow - 1][startCol] = true;
					}
				} else if (!this.maze.getRoom(pair.getX(), pair.getY()).hasWestWall()) {
					if (!this.visited[startRow][startCol - 1]) {
						newFrontier.add(new Pair(startRow, startCol - 1));
						//this.visited[startRow][startCol - 1] = true;
					}
				}
			}
			nextFrontier = newFrontier;
		}

		// BFS traversal for currFrontier
		for (int i = 0; i < nextFrontier.size(); i++) {
			Pair currRoom = nextFrontier.remove();
			if (!this.maze.getRoom(currRoom.getX(), currRoom.getY()).hasEastWall()) {
				if (!this.visited[startRow][startCol + 1]) {
					Room room = this.maze.getRoom(startRow, startCol + 1);
					List<Integer> newList = copyList(l);
					newList.add(EAST);
					Queue<Pair> newQueue = new LinkedList<>();
					newQueue.add(new Pair(startRow, startCol + 1));
					tracePath(startRow, startCol + 1, endRow, endCol, newQueue, newList);
				}
			}
			if (!this.maze.getRoom(currRoom.getX(), currRoom.getY()).hasSouthWall()) {
				if (!this.visited[startRow + 1][startCol]) {
					Room room = this.maze.getRoom(startRow + 1, startCol);
					List<Integer> newList = copyList(l);
					newList.add(SOUTH);
					Queue<Pair> newQueue = new LinkedList<>();
					newQueue.add(new Pair(startRow + 1, startCol));
					tracePath(startRow + 1, startCol, endRow, endCol, newQueue, newList);
				}
			}
			if (!this.maze.getRoom(currRoom.getX(), currRoom.getY()).hasNorthWall()) {
				if (!this.visited[startRow - 1][startCol]) {
					Room room = this.maze.getRoom(startRow - 1, startCol);
					List<Integer> newList = copyList(l);
					newList.add(NORTH);
					Queue<Pair> newQueue = new LinkedList<>();
					newQueue.add(new Pair(startRow - 1, startCol));
					tracePath(startRow - 1, startCol, endRow, endCol, newQueue, newList);
				}
			}
			if (!this.maze.getRoom(currRoom.getX(), currRoom.getY()).hasWestWall()) {
				if (!this.visited[startRow][startCol - 1]) {
					Room room = this.maze.getRoom(startRow, startCol - 1);
					List<Integer> newList = copyList(l);
					newList.add(WEST);
					Queue<Pair> newQueue = new LinkedList<>();
					newQueue.add(new Pair(startRow, startCol - 1));
					tracePath(startRow, startCol - 1, endRow, endCol, newQueue, newList);
				}
			}
		}
		return;
	}

	public static List<Integer> copyList(List<Integer> list) {
		List<Integer> newList = new ArrayList<>();
		List<Integer> tempList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			tempList.add(list.remove(i));
		}

		for (int j = 0; j < tempList.size(); j++) {
			int element = tempList.remove(j);
			newList.add(element);
			list.add(element);
		}
		return newList;
	}
	public static Queue<Room> copyQueue(Queue<Room> q) {
		Queue<Room> newQueue = new LinkedList<Room>();
		for (Room element: q) {
			newQueue.add(element);
		}
		return newQueue;
	}
	public Integer highlightPath(int startRow, int startCol, List<Integer> sequence, Maze maze) {
		if (sequence.isEmpty()) {
			this.maze.getRoom(startRow, startCol).onPath = true;
			return 0;
		} else if (sequence == null) {
			return null;
		}
		int len = sequence.size();
		int row = startRow;
		int col = startCol;
		for (int i = 0; i < len; i++) { // To highLight the rooms on actual shortest path
			int direction = sequence.remove(i);
			if (direction == EAST) {
				col++;
				this.maze.getRoom(row, col).onPath = true;
			} else if (direction == SOUTH) {
				row++;
				this.maze.getRoom(row, col).onPath = true;
			} else if (direction == NORTH) {
				row--;
				this.maze.getRoom(row, col).onPath = true;
			} else if (direction == WEST) {
				col--;
				this.maze.getRoom(row, col).onPath = true;
			}
		}
		return len;
	}*/
	@Override
	public Integer numReachable(int k) throws Exception {
		// TODO: Find number of reachable rooms.
		if (k > this.resultList.size() - 1) {
			return 0;
		}
		List<Integer> list = this.resultList;
		Integer result = list.get(k);
		return result;
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 2, 3));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
