package test_sch;

public class Main {

	public static void main(String[] args) {
		
		 FileReaderD read_data = new FileReaderD("rfile.txt","employeeFile.txt");
		
		 read_data.loadEmployeeDataFile();
		 read_data.loadObjectiveFunction();
	}
	
}
