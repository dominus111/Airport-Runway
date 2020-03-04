//module RunwayRedeclarationTeam19.main {
module runwayredeclaration {
    requires javafx.controls;
    requires javafx.fxml;

    opens runway.View to javafx.graphics;
    opens runway.Controller to javafx.fxml;
}