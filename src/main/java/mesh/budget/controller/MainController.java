package mesh.budget.controller;

import javafx.fxml.FXML;
import javafx.geometry.Side;

import java.time.Month;
import java.util.ArrayList;

import org.controlsfx.control.table.TableFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import mesh.budget.Utils;
import mesh.budget.model.AppState;
import mesh.budget.model.BankStatementRow;
import mesh.budget.model.Budget;
import mesh.budget.model.Categories;
import mesh.budget.model.Category;
import mesh.budget.model.CategoryAverage;
import mesh.budget.model.CategoryMonth;

public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	// Models
	private AppState appStateModel;
	private Categories categories;
	private Budget budget;

	public CategoryUIController catController;
	public AddMatchController addDescriptionMatchController;
	public AddMatchController addReferenceMatchController;

	public Scene catScene;
	public Stage catStage;
	private boolean catTableCreated = false;
	private boolean averageTableCreated = false;

	@FXML
	private Button labeutton1l1;
	@FXML
	private TableView<BankStatementRow> budgetTable;

	@FXML
	private TableView<CategoryMonth> catTable;

	@FXML
	private TableView<CategoryAverage> averageTable;

	@FXML
	private DialogPane dialogPane1;

	@FXML
	private Alert alert;

	@FXML
	private Tab charttab;

	@FXML
	private PieChart pieChart;

	private BarChart<String, Number> barChart;
	private StackedBarChart<String, Number> barChart2;

	@FXML
	private Button pieButton1;

	@FXML
	private TextArea alertsTextArea;

	@FXML
	private BorderPane chartPane;

	@FXML
	private BorderPane chart2Pane;

	@FXML
	private ListView<String> monthPicker;

	@FXML
	private Label catTableTotalLabel;

	@FXML
	private Label budgetTotalLabel;

	@FXML
	private Tab chartTab2;

	@FXML
	public void onAlertButton1Click(ActionEvent event) {

		BankStatementRow row = budgetTable.getSelectionModel().getSelectedItem();
		alert(row.toString());
	}

	@FXML
	public void onSelectChartTab(Event event) {
		logger.info("onSelectChartTab");
	}

	// onShowChart2
	@FXML
	public void onShowChart2(ActionEvent event) {
		logger.info("showing tab 2");

	}

	@FXML
	void onChart2Button1Click() {
		logger.info("onShowChart2");
			
			ArrayList<XYChart.Series<String, Number>>  seriesArray = budget.calcMonthTotalsPerCategory(categories);
			logger.debug("seriesArray size:" + seriesArray.size());
			showBarChart2(seriesArray,categories);	
	}

	private void showBarChart2(ArrayList<XYChart.Series<String, Number>> barchartdata, Categories categories) {
		logger.info("showBarChart2");

		// remove the old one
		chart2Pane.getChildren().remove(barChart2);

		// define x axis

		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("month");
		
		/*
		for (Month m : Month.values()) { 
			xAxis.getCategories().add(m.name());
		};
		*/

		// define y axis
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("amount");

		barChart2 = new StackedBarChart<String, Number>(xAxis, yAxis);

		// setup legend
		barChart2.legendSideProperty().set(Side.RIGHT);
		barChart2.setTitle("amount spent per category by month");

		// make it visible
		// chartPane.getChildren().add(barChart);
		chart2Pane.setCenter(barChart2);
		
	/*
		
		for (XYChart.Series<String, Number> series: barchartdata) {
			barChart2.getData().addLast(series);
			logger.debug("adding series " + series.getName());
			
		}
		
	*/	
		barChart2.getData().addAll(barchartdata);

		

	}

	@FXML
	public void onChartButton1Click(ActionEvent event) {

		logger.info("onChartButton1Click");

		ArrayList<XYChart.Series<String, Number>> barchartdata = new ArrayList<XYChart.Series<String, Number>>();

		ObservableList<String> selectedMonths = monthPicker.getSelectionModel().getSelectedItems();
		selectedMonths.forEach(month -> {
			Month theMonth = Month.valueOf(month);
			XYChart.Series<String, Number> series = budget.calcCategoryTotalsForMonth(categories, theMonth);
			series.setName(month);
			barchartdata.add(series);

		});

		Number grandtotal = Double.valueOf(0);

		// calculate the grand total
		for (int i = 0; i < barchartdata.size(); i++) {
			Number amount;
			// category amounts for a given momth
			XYChart.Series<String, Number> theSeries = barchartdata.get(i);
			for (int k = 0; k < theSeries.getData().size(); k++) {
				amount = theSeries.getData().get(k).getYValue();
				grandtotal = grandtotal.doubleValue() + amount.doubleValue();
			}
		}

		showBarChart(barchartdata, categories);
		// catTableTotalTextField.setText(new Double(categories.getTotal()).toString());
		catTableTotalLabel.setText(Utils.toCurrency(grandtotal));
		int numMonths = monthPicker.getSelectionModel().getSelectedItems().size();
		budgetTotalLabel.setText(Utils.toCurrency(numMonths * categories.getBudgetTotal()));
		showCatTable(barchartdata);
	}

	private void showCatTable(ArrayList<XYChart.Series<String, Number>> barchartdata) {
		logger.info("getting cat table data");
		ObservableList<CategoryMonth> catTableData = FXCollections.observableArrayList();
		barchartdata.forEach(series -> {

			String month = series.getName();

			series.getData().forEach(row -> {
				CategoryMonth catm = new CategoryMonth();

				catm.setMonth(month);
				catm.setCategoryName(row.getXValue());
				catm.setCategoryAmount(row.getYValue());

				catTableData.add(catm);
				logger.debug(catm.toString());

			});

		});

		catTable.setItems(catTableData);
		showAverageTable(catTableData);

	}

	private void showAverageTable(ObservableList<CategoryMonth> monthdata) {
		logger.info("getting average table data");
		ObservableList<CategoryAverage> averageTableData = FXCollections.observableArrayList();

		monthdata.forEach(catMon -> {
			// categories.getCategoryByName(c
			String catName = catMon.getCategoryName();
			double catBudget = categories.getCategoryByName(catName).getBudget();
			CategoryAverage catAvg = new CategoryAverage(catName, catBudget);

			if (averageTableData.indexOf(catAvg) < 0) {
				averageTableData.add(catAvg);
				logger.debug("adding" + catAvg.getCategoryName());
			}
			int index = averageTableData.indexOf(catAvg);
			CategoryAverage thisCat = averageTableData.get(index);
			thisCat.addValue(catMon.getCategoryAmount());

		});
		averageTable.setItems(averageTableData);

	}

	private void showBarChart(ArrayList<XYChart.Series<String, Number>> barchartdata, Categories categories) {

		// remove the old one
		chartPane.getChildren().remove(barChart);

		// define x axis

		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("category");
		categories.getCategories().forEach(cat -> {
			xAxis.getCategories().add(cat.getName());

		});

		// define y axis
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("amount");

		barChart = new BarChart<String, Number>(xAxis, yAxis);

		// setup legend
		barChart.legendSideProperty().set(Side.RIGHT);
		barChart.setTitle("amount spent per month by category");

		// make it visible
		// chartPane.getChildren().add(barChart);
		chartPane.setCenter(barChart);

		barchartdata.forEach(series -> {
			barChart.getData().addLast(series);
		});

	}

	private boolean tableCreated = false;

	// selectChart2
	@FXML
	public void selectChart2(ActionEvent event) {
		logger.info("showing Chart2");
	}

	@FXML
	public void Table1Context(ContextMenuEvent event) {
		// System.out.println("context");
		logger.info("context");
		alert(budgetTable.selectionModelProperty().getValue().toString());

	}

	@FXML
	public void loadExports(ActionEvent event) {

		logger.info("loading exports");
		alert("loading exports");
		budget.loadExports();
	}

	@FXML
	public void save(ActionEvent event) {
		budget.saveToFile(Utils.budgetFileName);
		alert("saved to file " + Utils.budgetFileName);
		categories.saveToFile(Utils.categoryFileName);
	}

	@FXML
	public void showCategoryManager(ActionEvent event) {
		logger.info("showing Category Manger");
		showCategoryManger();
	}

	@FXML
	public void runMatches(ActionEvent event) {
		logger.info("running matches");
		alert("running matches");
		budget.runMatches(categories);
		budgetTable.refresh();
	}

	@FXML
	public void HelloButtonClicked(ActionEvent event) {

		logger.info("hello button clicked");
		budgetTable.refresh();

	}

	@FXML
	public void dedupe(ActionEvent event) {
		int count = budget.dedupe();
		alert(count + " duplicates found");
	}

	@FXML
	public void initialize() {
		tableSetup();
		chartSetup();
		catTableSetup();
		averageTableSetup();
		appStateModel.bankStatementRowChangedProperty().addListener((observable, oldValue, newValue) -> {
			// Only if completed
			logger.info("bankStatementRow changed");
			budgetTable.refresh();
			appStateModel.setBankStatementRowChanged(false);
		});

		budget.getBudget().addListener(new ListChangeListener<BankStatementRow>() {
			@Override
			public void onChanged(Change<? extends BankStatementRow> c) {
				budgetTable.refresh();
				logger.debug("budget changed");
			}
		});

	}

	private void chartSetup() {

		monthPicker.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		for (Month month : Month.values()) {

			monthPicker.getItems().add(month.toString());
		}

	}

	private void catTableSetup() {
		if (!catTableCreated) {
			logger.info("catTableSetup");
			TableColumn<CategoryMonth, String> month = new TableColumn<CategoryMonth, String>("Month");
			TableColumn<CategoryMonth, String> category = new TableColumn<CategoryMonth, String>("category");
			TableColumn<CategoryMonth, Number> amount = new TableColumn<CategoryMonth, Number>("Amount");

			catTable.getColumns().addAll(month, category, amount);

			month.setCellValueFactory(new PropertyValueFactory<CategoryMonth, String>("Month"));
			category.setCellValueFactory(new PropertyValueFactory<CategoryMonth, String>("categoryName"));
			amount.setCellValueFactory(new PropertyValueFactory<CategoryMonth, Number>("categoryAmount"));

		}
		catTableCreated = true;

	}

	private void averageTableSetup() {
		if (!averageTableCreated) {
			logger.info("averageTableSetup");

			TableColumn<CategoryAverage, String> category = new TableColumn<CategoryAverage, String>("category");
			TableColumn<CategoryAverage, Number> average = new TableColumn<CategoryAverage, Number>("average");
			TableColumn<CategoryAverage, Number> budget = new TableColumn<CategoryAverage, Number>("budget");

			averageTable.getColumns().addAll(category, average, budget);

			category.setCellValueFactory(new PropertyValueFactory<CategoryAverage, String>("categoryName"));
			average.setCellValueFactory(new PropertyValueFactory<CategoryAverage, Number>("categoryAverage"));
			budget.setCellValueFactory(new PropertyValueFactory<CategoryAverage, Number>("categoryBudget"));

		}
		averageTableCreated = true;
		logger.info("averageTableSetup finished");

	}

	private void tableSetup() {
		if (!tableCreated) {

			TableColumn<BankStatementRow, String> account = new TableColumn<BankStatementRow, String>("account");
			TableColumn<BankStatementRow, String> month = new TableColumn<BankStatementRow, String>("month");

			TableColumn<BankStatementRow, String> dateProcessed = new TableColumn<BankStatementRow, String>(
					"dateProcessed");
			TableColumn<BankStatementRow, String> id = new TableColumn<BankStatementRow, String>("id");
			TableColumn<BankStatementRow, String> type = new TableColumn<BankStatementRow, String>("type");
			TableColumn<BankStatementRow, String> reference = new TableColumn<BankStatementRow, String>("reference");
			TableColumn<BankStatementRow, String> description = new TableColumn<BankStatementRow, String>(
					"description");
			TableColumn<BankStatementRow, String> amount = new TableColumn<BankStatementRow, String>("amount");
			TableColumn<BankStatementRow, String> category = new TableColumn<BankStatementRow, String>("category");

			budgetTable.getColumns().addAll(account, month, dateProcessed, id, type, reference, description, amount,
					category);

			account.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("account"));

			month.setCellValueFactory(
					new Callback<CellDataFeatures<BankStatementRow, String>, ObservableValue<String>>() {
						public ObservableValue<String> call(CellDataFeatures<BankStatementRow, String> r) {							
							return new SimpleStringProperty(r.getValue().getMonth().toString());
						}
					});

			dateProcessed.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("dateProcessed"));
			amount.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("amount"));
			description.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("description"));
			reference.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("reference"));

			description.setOnEditStart(event -> {
				String currentValue = event.getOldValue();

				BankStatementRow row = (BankStatementRow) event.getRowValue();
				addDescriptionMatchController.show(currentValue, row);
			});

			reference.setOnEditStart(event -> {
				String currentValue = event.getOldValue();

				BankStatementRow row = (BankStatementRow) event.getRowValue();
				addReferenceMatchController.show(currentValue, row);
			});

			id.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("id"));
			reference.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("reference"));
			type.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("type"));

			category.setCellValueFactory(new PropertyValueFactory<BankStatementRow, String>("category"));
			category.setCellFactory(column -> new TableCel_Edit());

		}
		;

		tableCreated = true;
		budgetTable.setItems(budget.getBudget());
		TableFilter.forTableView(budgetTable).apply();
		budgetTable.setEditable(true);

	}

	private class TableCel_Edit extends TableCell<BankStatementRow, String> {

		ChoiceBox<String> categoryBox = new ChoiceBox<>();

		public TableCel_Edit() {

			for (Category c : categories.getCategories()) {
				categoryBox.getItems().add(c.getName());
			}

			categoryBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {

				String value = categoryBox.getItems().get(newValue.intValue());
				processEdit(value);
			});

		}

		private void processEdit(String value) {
			commitEdit(value);
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();
			setText(getItem());
			setGraphic(null);
		}

		@Override
		public void commitEdit(String value) {
			super.commitEdit(value);
			TableRow<BankStatementRow> row = this.getTableRow();
			row.getItem().setCategory(value);
			alert("changing category to " + value);
			setGraphic(null);
			budgetTable.refresh();

		}

		@Override
		public void startEdit() {
			super.startEdit();
			String value = getItem();
			if (value != null) {
				setGraphic(categoryBox);
				setText(null);
			}
		}

		@Override
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if (item == null || empty) {
				setText(null);

			} else {
				setText(item);
			}
		}

	}

	private void showCategoryManger() {

		BankStatementRow r = (BankStatementRow) budgetTable.getSelectionModel().getSelectedItem();

		// catController.setSelectedRow(r);

		if (r == null) {
			logger.error("selected row is null");
		} else {
			logger.info("showing category manager for " + r.getDescription());
		}
		catController.show(r);

	}

	// Event Listener on TableView.onMouseClicked
	@FXML
	public void Table1Clicked(MouseEvent event) {
		logger.info("Table1Clicked");

		budgetTable.refresh();

	}

	public MainController(AppState appStateModel, Categories categories, Budget budget) {
		logger.info("constructing main controler=" + this.toString());

		this.appStateModel = appStateModel;
		this.categories = categories;

		this.budget = budget;

		// to do: recalc when categories edited
		budget.calcCategoryTotals(categories);

	}

	public void alert(String msg) {
		alertsTextArea.setText(msg);

	}
}
