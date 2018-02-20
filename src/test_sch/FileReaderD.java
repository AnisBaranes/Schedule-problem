package test_sch;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FileReaderD {

	private String empDemandsFile, empDataFile;
	private String[] neddedEmployee, objectiveFunction;
	private Employee[] employeeArr;
	private String strObjectiveFunction;
	private int numOfEmp;

	public FileReaderD(String demandsFile, String dataFile) {

		this.empDemandsFile = demandsFile;
		this.empDataFile = dataFile;
		this.objectiveFunction = null;
		this.neddedEmployee = null;
		this.strObjectiveFunction = null;
		this.numOfEmp = 0;
	}

	/* employee info file */
	public void loadEmployeeDataFile() {
		String line = null;

		int i = 0, id = 0;

		try {
			FileReader fileReader = new FileReader(empDataFile);

			/* number of employees */
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			if ((line = bufferedReader.readLine()) != null) {
				numOfEmp = Integer.parseInt(line);
				employeeArr = new Employee[numOfEmp];
			}

			while ((line = bufferedReader.readLine()) != null) {
				String[] tokens = line.split(" ");
				id = Integer.parseInt(tokens[1]);
				employeeArr[i++] = new Employee(id, tokens[0]);
				 System.out.println(line);
			}    

			bufferedReader.close();
		}

		catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + empDataFile + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + empDataFile + "'");

		}

		// testPrint();
		Scanner reader = new Scanner(System.in);
		System.out.println("Please verify the number of employees- is the number is "+ numOfEmp + "? y/n");
		char answer = reader.next().charAt(0);
		if(answer == 'n'||answer == 'N')
		{
			System.out.println("Please update the employee file");
			loadEmployeeDataFile();
		}
		reader.close();
		
	}

	public void testPrint() {
		for (int i = 0; i < employeeArr.length; i++)
			System.out.println(employeeArr[i].getName() + " " + employeeArr[i].getId());
	}

	/* objective file */
	public void loadObjectiveFunction() {
		String line = null;

		try {
			FileReader fileReader = new FileReader(empDemandsFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			if ((line = bufferedReader.readLine()) != null)
				objectiveFunction = line.split("\\s+"); 
			
			if(objectiveFunction.length != numOfEmp*12)
				System.err.println("Error in 'rfile' file - wrong number of employee preferences");

			if ((line = bufferedReader.readLine()) != null) 
				neddedEmployee = line.split("\\s+");
			
			if(neddedEmployee.length != 12) 
				System.err.println("Error in 'rfile' file- number of needed employees should be 12\n");
			

			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + empDemandsFile + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + empDemandsFile + "'");

		}
		runLpSolve();

	}

	public void mulObjectiveFunc() {
		for (int i = 0; i < objectiveFunction.length; i++) {
			if (Integer.parseInt(objectiveFunction[i]) == 2)
				objectiveFunction[i] = String.valueOf(Integer.parseInt(objectiveFunction[i]) * 10);

			if (Integer.parseInt(objectiveFunction[i]) == 3)
				objectiveFunction[i] = String.valueOf(Integer.parseInt(objectiveFunction[i]) * 100);

			if (Integer.parseInt(objectiveFunction[i]) == 4)
				objectiveFunction[i] = String.valueOf(Integer.parseInt(objectiveFunction[i]) * 1000);

			if (Integer.parseInt(objectiveFunction[i]) == 5)
				objectiveFunction[i] = String.valueOf(Integer.parseInt(objectiveFunction[i]) * 1000000);
		}
		strObjectiveFunction = String.join(" ", objectiveFunction);
	}

	public void runLpSolve() {
		mulObjectiveFunc();
		Solver solver = new Solver(strObjectiveFunction, neddedEmployee, numOfEmp,employeeArr); 
		solver.solveLp();

	}

}
