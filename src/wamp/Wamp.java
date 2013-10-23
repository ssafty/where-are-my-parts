package wamp;

import java.util.ArrayList;

import util.Grid;
import util.Operator;
import util.SearchProblem;
import util.SearchTreeNode;
import wamp.WampOperator.Direction;

import algorithm.SearchAlgorithm;

public class Wamp extends SearchAlgorithm {

	public Grid genGrid() {
		Grid grid = new Grid(true);
		return grid;
	}

	public String[] search(Grid grid, String strategy, boolean visualize) {
		WampState initialState = new WampState(grid, 0, 0);
		Operator[] operators = new Operator[grid.getParts().size() * 4];
		for (int i = 0; i < grid.getParts().size(); i++) {
			operators[i] = new WampOperator(i, Direction.UP);
			operators[i] = new WampOperator(i, Direction.DOWN);
			operators[i] = new WampOperator(i, Direction.LEFT);
			operators[i] = new WampOperator(i, Direction.RIGHT);
		}
		WampSearchProblem problem = new WampSearchProblem(operators, null,
				initialState);
		int strategyNumber = 0;
		if (strategy.equals("BF")) {
			strategyNumber = 0;
		} else {
			if (strategy.equals("DF")) {
				strategyNumber = 1;
			} else {
				if (strategy.equals("ID")) {
					strategyNumber = 2;
				} else {
					if (strategy.equals("GR1")) {
						strategyNumber = 3;
					} else {
						if (strategy.equals("GR2")) {
							strategyNumber = 4;
						} else {
							if (strategy.equals("AS1")) {
								strategyNumber = 5;
							} else {
								if (strategy.equals("AS2")) {
									strategyNumber = 6;
								} else {
									strategyNumber = 0;
								}
							}
						}
					}
				}
			}
		}

		search(problem, strategyNumber, visualize);
		return null;
	}

	public SearchTreeNode search(WampSearchProblem problem, int strategy,
			boolean visualize) {
		nodes = new ArrayList<SearchTreeNode>();
		nodes.add(new SearchTreeNode(problem.getInitialState(), null, null, 0,
				0));
		while (!nodes.isEmpty()) {
			SearchTreeNode node = nodes.get(0);
			if (problem.goalTest(node.getState())) {
				return node;
			} else {
				switch (strategy) {
				case 0:
					BFS(node, problem);
					break;
				case 1:
					DFS(node, problem);
					break;
				case 2:
					IDS(node, problem);
					break;
				case 3:
					GRS0(node, problem);
					break;
				case 4:
					GRS1(node, problem);
					break;
				case 5:
					AS0(node, problem);
					break;
				case 6:
					AS1(node, problem);
					break;
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		Wamp wamp = new Wamp();
		Grid grid = wamp.genGrid();
		wamp.search(grid, "", true);
	}

	@Override
	public void BFS(SearchTreeNode node, SearchProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DFS(SearchTreeNode node, SearchProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void IDS(SearchTreeNode node, SearchProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GRS0(SearchTreeNode node, SearchProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GRS1(SearchTreeNode node, SearchProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AS0(SearchTreeNode node, SearchProblem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AS1(SearchTreeNode node, SearchProblem problem) {
		// TODO Auto-generated method stub
		
	}

}