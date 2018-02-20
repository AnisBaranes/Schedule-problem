package test_sch;


public class Employee {
	
	private int emp_id;
	private String emp_name; 

	public Employee(int id, String name) {
		this.emp_id = id; 
		this.emp_name = name; 
	}
	public int getId()
	{
		return emp_id; 
	}
	public String getName()
	{
		return emp_name; 
	}
	public void setName()
	{
		 emp_name+=" "; 
	}
}