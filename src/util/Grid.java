package util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import wamp.WampOperator;

public class Grid {

	public enum GridType {
		Free, Obstacle, RobotPart
	}

	private GridType[][] gridCells;
	private ArrayList<Part> Parts;
	private ArrayList<Point> Obstacles;
	private int length;
	private int width;

	public Grid(boolean generate) {
		if (generate) {
			if (false) {
				do {
					length = (int) (Math.random() * 20);
					width = (int) (Math.random() * 20);
				} while (length < 3 || width < 3);
				// grid fills
				gridCells = new GridType[length][width];
				for (GridType[] row : gridCells) {
					Arrays.fill(row, GridType.Free);
				}

				// Parts randomization
				Parts = new ArrayList<Part>();
				int bound1;
				do {
					bound1 = (int) (Math.random() * 5);
				} while (bound1 < 1);
				for (int i = 0; i < bound1; i++) {
					int partsX;
					int partsY;
					do {
						partsX = (int) (Math.random() * length);
						partsY = (int) (Math.random() * width);
					} while (!gridCells[partsX][partsY].equals(GridType.Free));
					if (gridCells[partsX][partsY].equals(GridType.Free)) {
						Parts.add(new Part(new Point(partsX, partsY)));
						gridCells[partsX][partsY] = GridType.RobotPart;
					}
				}

				// Obstacles randomization
				Obstacles = new ArrayList<Point>();
				int bound2;
				do {
					bound2 = (int) (Math.random() * 5);
				} while (bound2 < 1);
				for (int i = 0; i < bound2; i++) {
					int obsX;
					int obsY;
					do {
						obsX = (int) (Math.random() * length);
						obsY = (int) (Math.random() * width);
					} while (!gridCells[obsX][obsY].equals(GridType.Free));

					if (gridCells[obsX][obsY].equals(GridType.Free)) {
						Obstacles.add(new Point(obsX, obsY));
						gridCells[obsX][obsY] = GridType.Obstacle;
					}
				}
			} else {
				gridCells = new GridType[5][5];
				for (GridType[] row : gridCells) {
					Arrays.fill(row, GridType.Free);
				}
				length = 5;
				width = 5;
				Parts = new ArrayList<Part>();
				Parts.add(new Part(new Point(1, 1)));
				gridCells[1][1] = GridType.RobotPart;
				Parts.add(new Part(new Point(1, 4)));
				gridCells[1][4] = GridType.RobotPart;
				Parts.add(new Part(new Point(3, 4)));
				gridCells[3][4] = GridType.RobotPart;
				Obstacles = new ArrayList<Point>();
				Obstacles.add(new Point (1,2));
				gridCells[1][2] = GridType.Obstacle;
				Obstacles.add(new Point (4,1));
				gridCells[4][1] = GridType.Obstacle;
			}
		}
	}

	public GridType[][] getGridCells() {
		return gridCells;
	}

	public void setGridCells(GridType[][] gridCells) {
		this.gridCells = gridCells;
	}

	public ArrayList<Part> getParts() {
		return Parts;
	}

	public void setParts(ArrayList<Part> parts) {
		Parts = parts;
	}

	public ArrayList<Point> getObstacles() {
		return Obstacles;
	}

	public void setObstacles(ArrayList<Point> obstacles) {
		Obstacles = obstacles;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@SuppressWarnings("unchecked")
	public Grid fix(WampOperator Operator, int partX, int partY) {
		Grid fixed = new Grid(false);
		// Cloning the obstacles not needed -- reference may cause issue take
		// care !!
		ArrayList<Point> newObstacles = (ArrayList<Point>) Obstacles.clone();
		// Fixing the new part point
		ArrayList<Part> newParts = cloneParts();
		newParts.get(Operator.getPartIndex()).getLocation().x = partX;
		newParts.get(Operator.getPartIndex()).getLocation().y = partY;
		// Fixing the grid cells
		GridType[][] newGridCells = new GridType[length][width];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++) {
				if (i == Parts.get(Operator.getPartIndex()).getLocation().x
						&& j == Parts.get(Operator.getPartIndex())
								.getLocation().y) {
					newGridCells[i][j] = GridType.Free;
					if (i == partX && j == partY) {
						newGridCells[i][j] = GridType.RobotPart;
					}
				} else {
					if (i == partX && j == partY) {
						newGridCells[i][j] = GridType.RobotPart;
					} else {
						newGridCells[i][j] = gridCells[i][j];
					}

				}
			}

		}
		switch (Operator.getPartDirection()) {

		case UP: {
			for (Part p : newParts) {
				if (p.getLocation().x == partX - 1
						&& p.getLocation().y == partY) {
					newParts.get(Operator.getPartIndex()).setUp(p);
					p.setDown(newParts.get(Operator.getPartIndex()));

				}
			}
		}
			break;
		case DOWN:
			for (Part p : newParts) {
				if (p.getLocation().x == partX + 1
						&& p.getLocation().y == partY) {
					newParts.get(Operator.getPartIndex()).setDown(p);
					p.setUp(newParts.get(Operator.getPartIndex()));
				}
			}
			break;
		case LEFT:
			for (Part p : newParts) {
				if (p.getLocation().x == partX
						&& p.getLocation().y == partY - 1) {
					newParts.get(Operator.getPartIndex()).setLeft(p);
					p.setRight(newParts.get(Operator.getPartIndex()));
				}
			}
			break;
		case RIGHT:
			for (Part p : newParts) {
				if (p.getLocation().x == partX
						&& p.getLocation().y == partY + 1) {
					newParts.get(Operator.getPartIndex()).setRight(p);
					p.setLeft(newParts.get(Operator.getPartIndex()));
				}
			}
			break;
		}

		fixed.Parts = newParts;
		fixed.gridCells = newGridCells;
		fixed.Obstacles = newObstacles;
		fixed.length = length;
		fixed.width = width;
		return fixed;
	}

	public Grid fix2(ArrayList<Part> AdjacentParts, WampOperator Operator,
			int partX, int partY) {
		Grid fixed = new Grid(false);
		// Cloning the obstacles not needed -- reference may cause issue take
		// care !!
		ArrayList<Point> newObstacles = (ArrayList<Point>) Obstacles.clone();
		// Fixing the new part point
		ArrayList<Part> newParts = cloneParts();
		ArrayList<Part> newAdjacent = cloneParts(newParts, AdjacentParts);
		//System.out.println(newAdjacent.toString());
		for (Part np : newParts) {
			for (Part ap : newAdjacent) {

				if (np.CompareParts(ap)) {
					np.getLocation().x += partX;
					np.getLocation().y += partY;
				}

			}
		}

		// newParts.get(Operator.getPartIndex()).getLocation().x = partX;
		// newParts.get(Operator.getPartIndex()).getLocation().y = partY;

		// Fixing the grid Types
		GridType[][] newGridCells = new GridType[length][width];

		for (GridType[] row : newGridCells) {
			Arrays.fill(row, GridType.Free);
		}

		for (Point obst : Obstacles) {
			newGridCells[(int) obst.getX()][(int) obst.getY()] = GridType.Obstacle;
		}

		for (Part particular : newParts) {
			newGridCells[(int) particular.getLocation().getX()][(int) particular
					.getLocation().getY()] = GridType.RobotPart;
		}

		for (Part AP : newAdjacent) { // <-------------
			switch (Operator.getPartDirection()) { // 5abat fe meen

			case UP: {
				for (Part p : newParts) {
					if (p.getLocation().x == AP.getLocation().getX() - 1
							&& p.getLocation().y == AP.getLocation().getY()) {
						//System.out.println(">>>>here");
						AP.setUp(p);
						p.setDown(AP);

					}
				}
			}
				break;
			case DOWN:
				for (Part p : newParts) {
					if (p.getLocation().x == AP.getLocation().getX() + 1
							&& p.getLocation().y == AP.getLocation().getY()) {
						//System.out.println(">>>>here");
						AP.setDown(p);
						p.setUp(AP);

					}
				}
				break;
			case LEFT:
				for (Part p : newParts) {
					if (p.getLocation().x == AP.getLocation().getX()
							&& p.getLocation().y == AP.getLocation().getY() - 1) {
						//System.out.println(">>>>here");
						AP.setLeft(p);
						p.setRight(AP);
					}
				}
				break;
			case RIGHT:
				for (Part p : newParts) {

					if (p.getLocation().x == AP.getLocation().x
							&& p.getLocation().y == AP.getLocation().y + 1) {
						//System.out.println(">>>>here");
						AP.setRight(p);
						p.setLeft(AP);
					}
				}
				break;
			}
		}

		fixed.Parts = newParts;
		fixed.gridCells = newGridCells;
		fixed.Obstacles = newObstacles;
		fixed.length = length;
		fixed.width = width;
		return fixed;
	}

	public ArrayList<Part> cloneParts() {
		ArrayList<Part> newParts = new ArrayList<Part>();
		for (Part p : Parts) {
			//System.out.println(">"+p.getRight());
			Part newP;
			newParts.add(newP= new Part(new Point(p.getLocation().x,
					p.getLocation().y), p.getUp(), p.getDown(), p.getLeft(), p
					.getRight()));
			//System.out.println(">"+newP.getRight());
		}
		return newParts;
	}

	public ArrayList<Part> cloneParts(ArrayList<Part> newParts,
			ArrayList<Part> adjParts) {
		ArrayList<Part> out = new ArrayList<Part>();
		for (Part np : newParts) {
			for (Part ap : adjParts) {
				if (np.CompareParts(ap)) {
					out.add(np);
				}
			}
		}
		return out;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < gridCells.length; i++) {
			s += (gridCells[i][0]);
			for (int j = 1; j < gridCells[i].length; j++) {
				s += "\t" + (gridCells[i][j].toString().substring(0,4));
			}
			s += "\n";
		}
		return s;
	}

	// remember to initialize it
	public ArrayList<Part> GetBulksRec(Part p, ArrayList<Part> result) {
		result.add(p);
		if (p.getUp() != null) {
			boolean Found = true;
			for (Part AvailableTestPart : result) {
				if (p.getUp().CompareParts(AvailableTestPart)) {
					Found = false;
				}
				break;
			}
			if (Found) {
				return GetBulksRec(p.getUp(), result); // mara wa7da bas
			}
		}
		if (p.getDown() != null) {
			boolean Found = true;
			for (Part AvailableTestPart : result) {
				if (p.getDown().CompareParts(AvailableTestPart)) {
					Found = false;
					break;
				}

			}
			if (Found) {
				return GetBulksRec(p.getDown(), result);
			}
		}
		if (p.getLeft() != null) {
			boolean Found = true;
			for (Part AvailableTestPart : result) {
				if (p.getLeft().CompareParts(AvailableTestPart)) {
					Found = false;
					break;
				}

			}
			if (Found) {
				return GetBulksRec(p.getLeft(), result);
			}
		}

		if (p.getRight() != null) {
			boolean Found = true;
			for (Part AvailableTestPart : result) {
				if (p.getRight().CompareParts(AvailableTestPart)) {
					Found = false;
					break;

				}
			}
			if (Found) {
				return GetBulksRec(p.getRight(), result);
			}
		}

		return result;
	}

}
