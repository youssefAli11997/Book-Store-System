package controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.AccountManager;
import model.DataManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class customerController {
    public TextField TitleField;
    public TextField CategoryField;
    public TextField AvailableQuantityField;
    public TextField SellingPriceField;
    public TextField PublicationYearField;
    public TextField ISBNField;
    public TextField AuthorNameField;
    public TextField DefaultOrderField;
    public TextField ThresholdField;
    public TableView DataTable;
    public TextField QuantityField;
    public VBox Card;
    public Button CheckOutButton;
    @FXML
    private Label testLabel;

    ResultSet lastResultSet;

    @FXML
    private void initialize() throws SQLException {
        if(AccountManager.getManager().isThereActiveUser())
            testLabel.setText("Welcome .... " + AccountManager.getManager().getCurrentUser().getUserName());
        ResultSet resultSet = DataManager.getInstance().getAllBook();
        TitleField.setText("bad book");
        showDatainTableView(resultSet);
    }
    private void showDatainTableView(ResultSet resultSet) throws SQLException {
        lastResultSet = resultSet;
        DataTable.getItems().clear();
        DataTable.getColumns().clear();
        for(int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            TableColumn<List<String>, String> col = new TableColumn<>(resultSet.getMetaData().getColumnName(i+1));
          //  System.out.println(resultSet.getMetaData().getColumnName(i+1));
            final int colIndex = i ;
            col.setCellValueFactory(data -> {
                List<String> rowValues = data.getValue();
                String cellValue ;
                if (colIndex < rowValues.size()) {
                    cellValue = rowValues.get(colIndex);
                } else {
                    cellValue = "" ;
                }
                return new ReadOnlyStringWrapper(cellValue);
            });
            DataTable.getColumns().add(col);
        }
        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>();
            for(int i = 0; i < resultSet.getMetaData().getColumnCount(); i++)
                row.add(resultSet.getString(i+1));
            DataTable.getItems().add(row);
        }
        Platform.runLater(() ->
        {
            DataTable.requestFocus();
            DataTable.getSelectionModel().select(0);
            DataTable.scrollTo(0);
        });
    }

    public void search(final ActionEvent actionEvent) throws SQLException {
        ArrayList<String> searchParametes = new ArrayList<>();

        searchParametes.add(ISBNField.getText());
        searchParametes.add(TitleField.getText());
        searchParametes.add(CategoryField.getText());
        searchParametes.add(PublicationYearField.getText());
        searchParametes.add(SellingPriceField.getText());
        searchParametes.add(AvailableQuantityField.getText());
        searchParametes.add(ThresholdField.getText());
        searchParametes.add(DefaultOrderField.getText());
        searchParametes.add(AuthorNameField.getText());

        ResultSet resultSet = DataManager.getInstance().searchForBook(searchParametes);
        showDatainTableView(resultSet);
    }

    public void addToCart(final ActionEvent actionEvent) throws SQLException {
        int index = 0;
        ResultSet temp = lastResultSet;
        ArrayList<String> selectedRow = (ArrayList<String>) DataTable.getSelectionModel().getSelectedItem();
        CardController card = new CardController(this.Card, QuantityField.getText(), selectedRow);
        card.addToCard();
    }
}
