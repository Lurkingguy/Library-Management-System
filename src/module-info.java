module Final_Project_Group7 {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
