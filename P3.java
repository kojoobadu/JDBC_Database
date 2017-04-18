import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class P3
{
	public static void main(String[] args) throws Exception {
		// Load and register a JDBC driver
		try {
			// Load the driver (registers itself)
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception E) {
			System.err.println("Unable to load driver.");
			E.printStackTrace();
		}
		try {
			// Connect to the database
			Connection conn1;
			String dbUrl = "jdbc:mysql://mysql.cs.iastate.edu:3306/db363kojob";
			String user = "dbu363kojob";
			String password = "i6s2dguj";
			conn1 = DriverManager.getConnection(dbUrl, user, password);
			System.out.println("*** Connected to the database ***");

			// Create Statement and ResultSet variables to use throughout the project
			Statement statement = conn1.createStatement();
			Statement statement2 = conn1.createStatement();
			Statement statement3 = conn1.createStatement();
			Statement statement4 = conn1.createStatement();
			ResultSet rs;
			ResultSet courses = null; 
			ResultSet cHours = null;
			ResultSet gpa = null;

			// get salaries of all instructors
			rs = statement.executeQuery("select * from Student f");
			

			int newCreditHours = 0;
			String studentID = "";
			float newGPA = 0;
			int i = 0;
			int numberOfCourses = 0;
			int oldCreditHours = 0;
			float GPA = 0;
			String courseGrade = "";
			ArrayList<Float> courseGrades = new ArrayList<Float>();
			float thisGrade = 0;
			float classesGPA = 0;
			

			while (rs.next()) {
				//get value of salary from each tuple
				studentID = rs.getString("StudentID");			
				System.out.println("Student "+ ++i + " is : " + studentID);
				
				gpa = statement4.executeQuery("select GPA from Student where StudentID = " + studentID);
				gpa.last();
				GPA = gpa.getFloat("GPA");
				System.out.print("GPA : " + GPA + "\n");
				
				cHours = statement2.executeQuery("select CreditHours from Student where StudentID = "+ studentID );
				cHours.last();
				oldCreditHours = cHours.getInt("CreditHours");
				System.out.println("The credit hours is " + oldCreditHours);
				
				courses = statement3.executeQuery("select * from Enrollment where StudentID = " + studentID);
				while ( courses.next()){
					courseGrade = courses.getString("Grade");
					thisGrade = GradePoint(courseGrade) * 3;
					courseGrades.add(thisGrade);
					System.out.println("The grade point for course is : " + thisGrade);
				}
				for ( int k = 0; k < courseGrades.size(); k++){
					classesGPA += courseGrades.get(k);
				}
				courses.last();
				numberOfCourses = courses.getRow();
				System.out.println("The number of courses are " + numberOfCourses);
				newCreditHours = oldCreditHours + (numberOfCourses * 3);
				newGPA = ((GPA * oldCreditHours) + classesGPA) / newCreditHours;
			//	newGPA = Math.round(newGPA * 100) / 100;
				System.out.println("The new credit hours is "+ newCreditHours);
				System.out.println("The new GPA is : " + newGPA);
				courseGrades.clear();
				System.out.print("\n\n");
			}
			
			
			
		
			// Close all statements and connections
			statement.close();
			rs.close();
			conn1.close();
			statement2.close();
			statement3.close();
			statement4.close();
			courses.close();
			cHours.close();
			gpa.close();

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}
	
	public String Classification(int creditHours){
		if( creditHours > 0 && creditHours < 30){
			return "Freshman";
		}
		if( creditHours > 29 && creditHours < 60){
			return "Sophomore";
		}
		if ( creditHours > 59 && creditHours < 90){
			return "Junior";
		}
		return "Senior";
	}

	public static float GradePoint(String grade){
		grade = grade.trim();
		if(grade.equals("A")){
			return 4;
		}
		else if (grade.equals("A-")){
			return (float) 3.66;
		}
		else if (grade.equals("B+")){
			return (float) 3.33;
		}
		else if (grade.equals("B")){
			return (float) 3;
		}
		else if (grade.equals("B-")){
			return (float) 2.66;
		}
		else if (grade.equals("C+")){
			return (float) 2.33;
		}
		else if (grade.equals("C")){
			return (float) 2;
		}
		else if (grade.equals("C-")){
			return (float) 1.66;
		}
		else if (grade.equals("D+")){
			return (float) 1.33;
		}
		else if (grade.equals("D")){
			return 1;
		}
		return 0;
	}
	

}