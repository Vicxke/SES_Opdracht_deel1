module be.kuleuven.candycrush {
    requires javafx.controls;
    requires javafx.fxml;
    requires be.kuleuven.CheckNeighboursInGrid;

    requires org.controlsfx.controls;

    opens be.kuleuven.candycrush to javafx.fxml;
    exports be.kuleuven.candycrush;
}