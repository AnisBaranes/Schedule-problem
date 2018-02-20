# Schedule-problem
## solving schedule problem using linear integer programming

Mixed-Integer Programming (MIP) Problems
A mixed-integer programming (MIP) problem is one where some of the decision variables are constrained to be integer values (i.e. whole numbers such as -1, 0, 1, 2, etc.) at the optimal solution.  The use of integer variables greatly expands the scope of useful optimization problems that you can define and solve.

### Scheduling model
Given N employees and 12 shifts: 2 shifts per day (morning, evening) from Sunday to Thursday and one shift on Friday and Saturday. 
##### X_ij  – employee i,shift j 
Each employee submits his shift's preferences to the supervisor, if he doesn't want to work in some shift he need to give the reason. 
The supervisor prioritized the employee requests from 1 to 5 (lowest to top). This is the first line in the input file. The second line is the number of needed employees in each shift.

![model](https://user-images.githubusercontent.com/34624638/36420018-8cdf4488-162b-11e8-8000-dee92017aae0.png)

### Installation in java programs
lpsolveJni is a directory I created which contain the unzip lp_solve dynamic libraries from the archives lp_solve_5.5_dev. (zip or tar.gz) and lp_solve_5.5_exe.

Copy the lpsolveJni to a standard library directory for your target platform. On Windows, a typical place would be \Windows or \Windows\System32. On Linux, a typical place would be the directory /usr/local/lib.

Copy the archive file lpsolve55j.jar from the Java wrapper distribution to a directory that is included in the CLASSPATH of your java program.
Control panel -> System and Security -> System-> Advanced system setting ->Environment variable -> find the PATH environment variable and select it. Click Edit.
It should look like this: “C:\Windows\System32\lpsolveJni\lib”

In Eclipse, click on Project -> Properties -> Java Build Path ->Libraries-> Add External Jar add the lpsolve55j.jar. 

Add an import statement for the package lpsolve. * at the beginning of your source file. 

#### Input files

##### ‘rfile.txt’:
First line - employee demands rated from 1 to 5 when 1 is the least important demand and 5 is the most important one. 
Second line - number of needed employees in each shift.

##### ‘employeeFile’:
First line contains the number of employee.
The following lines include the employees' names and serial number. 

#### Output file
Weekly schedule 

### Running example 
Input files: 
![1](https://user-images.githubusercontent.com/34624638/36420440-01675baa-162d-11e8-8ec7-6ba1a2ef6747.png)
![2](https://user-images.githubusercontent.com/34624638/36420452-11bd695e-162d-11e8-8070-d5850928aaf9.png)

Output file:

![3](https://user-images.githubusercontent.com/34624638/36420467-1c655a7e-162d-11e8-9897-322971efa3cb.png)






