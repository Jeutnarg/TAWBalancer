package org.taw.tc;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class Balancer {
	
	public static void main(String[] args)
	{
		int totalEs = 6;
		
		Model model = new Model("TAW Balance");
		int[][] pS = new int[][]
		{
			{7,7,5,7,7,5},//
			{4,4,3,4,4,3},
			{1,9,7,1,9,7},
			{6,8,2,6,8,2},
			{5,5,5,5,5,5},
			{9,2,2,9,2,2}
		};
		
		IntVar[] eSVal = model.intVarArray("PSVal",  totalEs, 0, 10, false);
		IntVar[] eSSlot = model.intVarArray("PInd", totalEs, 0, totalEs-1, false);
		model.allDifferent(eSSlot).post();
		for(int i = 0; i < totalEs; i++)
		{
			model.element(eSVal[i],  pS[i], eSSlot[i]).post();
		}
		
		IntVar[] eSValDiffs = new IntVar[totalEs/2];
		for(int i = 0; i < (totalEs/2); i++)
		{
			eSValDiffs[i] = (eSVal[i].sub(eSVal[i+(totalEs/2)])).abs().intVar();
		}
		int[] coeffs = new int[] {1,1,1};
		IntVar skillDisparity = model.intVar("SDisp", 0, 10 * totalEs/2);
		model.scalar(eSValDiffs,  coeffs,  "=",  skillDisparity).post();
		
		model.setObjective(Model.MINIMIZE, skillDisparity);
		Solution solution = model.getSolver().findSolution();
		if(solution != null)
			System.out.println(solution.toString());
		
	}
	
}
