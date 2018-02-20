package test_sch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
//import java.io.RandomAccessFile;
import java.util.Arrays;

import lpsolve.*;

public class Solver {

	private String strObjectiveFunction;
	private String[] neddedEmployee;
	private int numOfEmp; 
	private Employee[] employeeArr;
	private final int numberOfShifts = 12; 

	public Solver(String strObjFunc, String[] needEmp, int numberOfEmp,Employee[] employeeArr) {
		this.strObjectiveFunction = strObjFunc;
		this.neddedEmployee = needEmp;
		this.numOfEmp = numberOfEmp; 
		this.employeeArr = employeeArr;
	}

	public void solveLp() {

		int numberOfvariables = numOfEmp*numberOfShifts, counter = 0, neededEmpIndex = 0;
		int shiftArr[] = new int[numberOfvariables];

		for(int i = 0; i < numberOfvariables; i++) shiftArr[i] = 0;

		try {
			/*initialize with zero constraints*/
			LpSolve solver = LpSolve.makeLp(0, numberOfvariables); 


			/*add constraints*/

			/* Constraints - requested employees - OK  */ 
			for(int i = 0; i < numberOfvariables; i++)
			{
				shiftArr[i] = 1;
				counter++;

				if(counter == numOfEmp)
				{
					String str = Arrays.toString(shiftArr);
					str = str.substring(1, str.length()-1).replace(",", "");
					solver.strAddConstraint(str, LpSolve.EQ, Integer.parseInt(neddedEmployee[neededEmpIndex]));

					neededEmpIndex++;
					counter = 0; 
					for(int x = 0; x < numberOfvariables; x++) shiftArr[x] = 0;
				}
			}
			/*Each employee can't work more than six shifts per week- OK-TODO: test with 5*/

			for(int i = 0; i < numberOfvariables; i++) shiftArr[i] = 0;
			counter = 0; 
			int z = 0;

			for(int j = 1; j < numOfEmp+1; j++)
			{
				for(; z < numberOfvariables; z+=numOfEmp)
				{
					shiftArr[z] = 1;
					counter++;

					if(counter == numberOfShifts)
					{
						String str = Arrays.toString(shiftArr);
						str = str.substring(1, str.length()-1).replace(",", "");
						solver.strAddConstraint(str,LpSolve.LE, 6);

						counter = 0; 
						for(int x = 0; x < numberOfvariables; x++) shiftArr[x] = 0;					
					}
				}
				z = j;
			}

			/*employee can work only one shift per day - OK*/
			for(int i = 0; i < numberOfvariables; i++) shiftArr[i] = 0;
			counter = 0; 
			z = 0;

			for(int j = 1; j < numOfEmp+1; j++)
			{
				for(int i = 0; i < 5; i++) //5 days with shifts
				{
					for(; z < (numberOfvariables-(2*numOfEmp)); z+=numOfEmp)
					{
						shiftArr[z] = 1;
						counter++;

						if(counter == 2)
						{
							String str = Arrays.toString(shiftArr);
							str = str.substring(1, str.length()-1).replace(",", "");
							solver.strAddConstraint(str,LpSolve.LE, 1);

							counter = 0; 
							for(int x = 0; x < numberOfvariables; x++) shiftArr[x] = 0;					
						}
					}
				}
				z = j;
			}
			
			/*{0,1}*/
			for(int i = 0; i < numberOfvariables; i++) shiftArr[i] = 0;
			for(int i = 0; i < numberOfvariables; i++)
			{
				 shiftArr[i] = 1;
				 String str = Arrays.toString(shiftArr);
				 str = str.substring(1, str.length()-1).replace(",", "");
				 solver.strAddConstraint(str,LpSolve.LE, 1);
				 shiftArr[i] = 0;
			}
			/*end of constraints*/

			/*set objective function*/
			solver.strSetObjFn(strObjectiveFunction);	//after multiplication
			
			solver.setMinim();

			/*solve the problem*/
			solver.solve();

			//print solution
			/*System.out.println("Value of objective function: " + solver.getObjective());
			double[] var = solver.getPtrVariables();
			for (int i = 0; i < var.length; i++) {
				System.out.println("Value of var[" + i + "] = " + (int)var[i]);
			}*/
			printScheduleToFile(solver);
			
			/*delete the problem and free memory*/
			solver.deleteLp();


		} catch (LpSolveException e) {
			e.printStackTrace();
		}
	}
	
	public void printScheduleToFile(LpSolve solver) throws LpSolveException
	{
		File file = new File("result.txt");
		int cnt = 0, x = 0, maxSpace = 0; 
		double[] var = solver.getPtrVariables();
		String[] week = {"Sun-morning","Sun-evening", "Mon-morning","Mon-evening", "Tue-morning","Tue-evening",
				"Wed-morning","Wed-evening", "Thu-morning","Thu-evening", "Fri-morning", "Sat-evening", ""};
	
		for(int i = 0; i < employeeArr.length; i++)
			if(employeeArr[i].getName().length() > maxSpace)
				maxSpace = employeeArr[i].getName().length();
		String space="";
		for(int i = 0; i < maxSpace; i++) space += " ";
		
		
		for(int i = 0; i < employeeArr.length; i++)
			if(employeeArr[i].getName().length() < maxSpace)
			{
				int s = maxSpace-employeeArr[i].getName().length();
				for(int z = 0; z < s; z++) employeeArr[i].setName();
			}
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FileWriter writer = new FileWriter(file);
			writer.write("             ");
			for(int i = 0; i < employeeArr.length; i++)
				writer.write(employeeArr[i].getName()+" ");
			writer.write("\n");
			
			for (int i = 0; i < var.length; i++) {
				cnt++;
				if(i==0) writer.write(week[x]+ space +(int)+var[i]+ space);
				
				else writer.write((int)var[i]+ space);
				if(cnt == numOfEmp)
				{
					cnt = 0; 
					writer.write("\n"+week[++x]+ space);
				}
			}					
			writer.flush();
		    writer.close();
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		warningMsg(); 
	}
	
	public void warningMsg()
	{
		String[] week = {"Sun-morning","Sun-evening", "Mon-morning","Mon-evening", "Tue-morning","Tue-evening",
				"Wed-morning","Wed-evening", "Thu-morning","Thu-evening", "Fri-morning", "Sat-evening", ""};
		String[] days = {"Sunday", "Monday", "Tuesday","Wednesday", "Thursday","Friday", "Saturday", ""};
		int shift = 0, requestEmpShift = 0, lastIndex = 0; 	
		
		for(int i = 0; i < 10; i++)
		{
			shift++; 
			if(Integer.parseInt(neddedEmployee[i]) > numOfEmp) {
				String warning = "Warning- " + week[i] + ": the number of requested employees is greater than the number of employees\n";
				try {
					Files.write(Paths.get("result.txt"), warning.getBytes(), StandardOpenOption.APPEND);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			requestEmpShift += Integer.parseInt(neddedEmployee[i]);
			if(shift == 2) {
				if(requestEmpShift > numOfEmp) {
					String warning = "Warning- " + days[i] + ": the number of requested employees is greater than the number of employees\n";
					try {
						Files.write(Paths.get("result.txt"), warning.getBytes(), StandardOpenOption.APPEND);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				requestEmpShift = 0; 
				shift = 0; 
			}
			lastIndex = i; 
		}//
		requestEmpShift = 0; 
		shift = 0; 
		lastIndex++;
		for(int i = 0; i < 2; i++)
		{
			if(Integer.parseInt(neddedEmployee[lastIndex]) > numOfEmp) {
				String warning = "Warning- " + week[lastIndex] + ": the number of requested employees is greater than the number of employees\n";
				try {
					Files.write(Paths.get("result.txt"), warning.getBytes(), StandardOpenOption.APPEND);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			lastIndex++; 
		}
	}
}
