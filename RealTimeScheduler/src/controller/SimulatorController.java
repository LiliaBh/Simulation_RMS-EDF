package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


import controller.helpers.ModalWindow;
import exception.MalformedConfigFileException;
import exception.NegativeNumberException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import scheduler.Scheduler;
import scheduler.SchedulerFactory;
import scheduler.Task;
import utils.ConfigFile;
import utils.ConfigFilesReader;

public class SimulatorController implements Initializable {

	private static final int MAX_TASKS = 10;
	private static final int KEYS_GRID_PANE_COUNT = 5;

	private static final Color[] COLORS = { Color.LIGHTCORAL, Color.LIGHTBLUE, Color.LIGHTGREEN, Color.LIGHTSALMON,
			Color.MEDIUMPURPLE, Color.LIGHTPINK, Color.LIGHTSEAGREEN, Color.LIGHTSTEELBLUE, Color.MEDIUMPURPLE,
			Color.LIGHTCYAN, };


	// Buttons
	public Button startButton;
	public Button resetButton;
	public Button addTaskButton;
	public Button deleteTaskButton;
	public Menu menu;

	// Fields
	public ChoiceBox<String> schedulerChoiceBox;
	public TextField simulationRuntimeField;
	public TextField periodField;
	public TextField executionField;

	// Table
	public TableView tableView;
	public TableColumn idColumn;
	public TableColumn periodColumn;
	public TableColumn executionColumn;
	private ObservableList<Task> tableData;

	// Chart
	public Pane chartPane;
	public Group chartGroup;
	public GridPane legendGridPane;
	private int xAxisLength;
	private int yAxisLength;
	private double xScale;
	private double yScale;
	private HashMap<Integer, Color> colorMapping;
	private ArrayList<Label> legendLabels;

	private ArrayList<Task> tasks;
	private int currentTaskId;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		schedulerChoiceBox.setItems(FXCollections.observableArrayList(SchedulerFactory.RMS, SchedulerFactory.EDF));

		schedulerChoiceBox.setValue(SchedulerFactory.RMS);

		tasks = new ArrayList<>();
		legendLabels = new ArrayList<>();
		tableData = FXCollections.observableArrayList(tasks);
		currentTaskId = 1;

		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		periodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));
		executionColumn.setCellValueFactory(new PropertyValueFactory<>("execution"));
		tableView.setItems(tableData);
		refreshTable();
        initMenu();
	}

	public void handleAddTask() {
		if (tasks.size() > 10) {
			ModalWindow.displayError("The number of tasks must be between 1 and 10.");
			return;
		}

		int period = getIntegerValueFromField(periodField);
		int execution = getIntegerValueFromField(executionField);

		if (!(period < 0 || execution < 0)) {
			tasks.add(new Task(period, execution, currentTaskId));
			currentTaskId++;
			refreshTable();
		}

		periodField.clear();
		executionField.clear();
	}

	public void handleStartSimulation() {
		resetChart();
		SchedulerFactory factory = SchedulerFactory.getInstance();
		String schedulerName = schedulerChoiceBox.getValue();
		Scheduler scheduler;

		if (tasks.size() > MAX_TASKS || tasks.size() < 1) {
			ModalWindow.displayError("The number of tasks must be between 1 and 10.");
			return;
		}

		ArrayList<Task> toSchedule = new ArrayList<>();
		
		for ( Task t: tasks) {
			toSchedule.add(new Task(t.getPeriod(), t.getExecution(), t.getId()));
		}
		
		if (simulationRuntimeField.getText().trim().equals("")) {
			scheduler = factory.getScheduler(schedulerName, toSchedule);

		} else {
			int executionTime = getIntegerValueFromField(simulationRuntimeField);
			if (executionTime > 1) {
				scheduler = factory.getScheduler(schedulerName, toSchedule, executionTime);

			} else {

				return;
			}
		}

		ArrayList<Task> schedule = scheduler.schedule();

		drawChart(schedule, schedule.size());
		ModalWindow.displayReport(schedulerName + " Scheduler", scheduler.getReport());
	}

	public void deleteTask() {
		ObservableList<Task> selectedTasks = tableView.getSelectionModel().getSelectedItems();

		for (Task selectedTask : selectedTasks) {
			removeTask(selectedTask.getId());
		}

		refreshTable();
	}

    public void reset() {
        resetChart();
        simulationRuntimeField.clear();
        executionField.clear();
        periodField.clear();
        tasks = new ArrayList<>();
        currentTaskId = 1;
        refreshTable();
    }

	private void refreshTable() {
		tableData = FXCollections.observableArrayList(tasks);
		tableView.setItems(tableData);
	}

	private int getIntegerValueFromField(TextField field) {
		String value = field.textProperty().getValue().trim();

		if (value.equals("")) {
			return -1;
		}

		try {
			int intValue = Integer.parseInt(value);

			if (intValue < 0) {
				throw new NegativeNumberException();
			}

			return intValue;

		} catch (NumberFormatException | NegativeNumberException e) {
			ModalWindow.displayError("Invalid input: " + value + ".");
			return -1;
		}
	}

	private void removeTask(int id) {
		tasks.removeIf(t -> (t.getId() == id));
	}

	private void drawChart(ArrayList<Task> schedule, int endTime) {
		initChart(endTime);
		drawCartesianPlane();
		initColorMapping();
		drawExecution(schedule);
		drawChartKeys();
	}

	private void initChart(int executionTime) {
		xAxisLength = executionTime;
		yAxisLength = MAX_TASKS;
	}

	private void drawCartesianPlane() {
		double xAxisStart = 0;
		double yAxisStart = 0;
		double xAxisEnd = chartPane.getPrefWidth() - 50;
		double yAxisEnd = chartPane.getPrefHeight() - 50;

		xScale = xAxisEnd / xAxisLength;
		yScale = yAxisEnd / yAxisLength;

		chartGroup.setScaleY(-1);

		Color linesColor = new Color(1, 1, 1, 1);

		Line yAxisLine = new Line();
		yAxisLine.setStartX(xAxisStart);
		yAxisLine.setStartY(yAxisStart);
		yAxisLine.setEndX(0);
		yAxisLine.setEndY(yAxisEnd);
		yAxisLine.setStroke(Color.LIGHTGRAY);
		yAxisLine.setStrokeWidth(2);
		yAxisLine.setFill(linesColor);

		Line xAxisLine = new Line();
		xAxisLine.setStartX(xAxisStart);
		xAxisLine.setStartY(yAxisStart);
		xAxisLine.setEndX(xAxisEnd);
		xAxisLine.setEndY(0);
		xAxisLine.setStroke(Color.LIGHTGRAY);
		xAxisLine.setStrokeWidth(2);
		xAxisLine.setFill(linesColor);

		chartGroup.getChildren().addAll(yAxisLine, xAxisLine);

		for (int yIndex = 0; yIndex < yAxisLength; yIndex++) {
			Line horizontalLine = new Line();
			horizontalLine.setStartX(0);
			horizontalLine.setStartY(yIndex * yScale);
			horizontalLine.setEndX(xAxisEnd);
			horizontalLine.setEndY(yIndex * yScale);
			horizontalLine.setStroke(Color.LIGHTGRAY);
			horizontalLine.setFill(linesColor);
			chartGroup.getChildren().add(horizontalLine);
		}

		for (int xIndex = 0; xIndex < xAxisLength; xIndex++) {
			Line verticalLine = new Line();
			verticalLine.setStartX(xIndex * xScale);
			verticalLine.setStartY(0);
			verticalLine.setEndX(xIndex * xScale);
			verticalLine.setEndY(yAxisLength);
			verticalLine.setStroke(Color.LIGHTGRAY);
			verticalLine.setFill(linesColor);
			chartGroup.getChildren().add(verticalLine);
			Text scaleIndex = new Text (xIndex * xScale, 5, xIndex + "");
			scaleIndex.setFill(Color.BLACK);
			scaleIndex.getTransforms().add(new Rotate(180, xIndex * xScale, -5));
			scaleIndex.getTransforms().add(new Scale(-1, 1, xIndex * xScale, -5));
			chartGroup.getChildren().add(scaleIndex);
		}
	}

	private void drawExecution(ArrayList<Task> schedule) {
		for (int step = 0; step < schedule.size(); step++) {
			Task task = schedule.get(step);
			
			if (task != null) {
				int taskId = task.getId();
				drawStep(step, taskId, colorMapping.get(taskId));
			} else {
				drawIdleTime(step);
			}
		}
	}

	private void drawStep(int step, int taskId, Color color) {
		Line line = new Line();

		line.setStrokeWidth(5);
		line.setStroke(color);
		line.setStartX(step * xScale + 5);
		line.setStartY(taskId * yScale);
		line.setEndX((step + 1) * xScale - 3);
		line.setEndY(taskId * yScale);
		chartGroup.getChildren().add(line);
	}

	private void drawIdleTime(int step) {
		Line line = new Line();

		line.setStrokeWidth(5);
		line.setStroke(Color.DARKGRAY);
		line.setStartX(step * xScale + 5);
		line.setStartY(0);
		line.setEndX((step + 1) * xScale - 3);
		line.setEndY(0);
		chartGroup.getChildren().add(line);
	}

	private void initColorMapping() {
		colorMapping = new HashMap<>();

		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			colorMapping.put(task.getId(), COLORS[i]);
		}
	}

	private void drawChartKeys() {
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);

			if (task == null) {
				continue;
			}

			Color taskColor = colorMapping.get(task.getId());
			Label taskLabel = new Label("Task " + task.getId());
			taskLabel.setTextFill(taskColor);
			taskLabel.setFont(new Font(17));

			int gridRow = i % (KEYS_GRID_PANE_COUNT + 1);
			int gridCol = i / KEYS_GRID_PANE_COUNT;

			legendLabels.add(taskLabel);
			legendGridPane.add(taskLabel, gridRow, gridCol);
		}
	}

	private void resetChart() {
		for (Label label : legendLabels) {
			legendGridPane.getChildren().remove(label);
		}

		legendLabels = new ArrayList<>();
		chartGroup.getChildren().clear();
	}

	private void initMenu()
    {
        ObservableList<MenuItem> items = menu.getItems();
        ArrayList<String> fileNames = ConfigFilesReader.getAvailableFileNames();

        for (String fileName : fileNames) {
            MenuItem menuItem = new MenuItem(fileName);
            items.add(menuItem);

            EventHandler<ActionEvent> event = e -> {
                try {
                    ConfigFile configFile = ConfigFilesReader.readConfigFile(fileName);
                    populateFormFieldsFromConfigFile(configFile);
                } catch (MalformedConfigFileException el) {
                    ModalWindow.displayError("The configuration file " + fileName + " is malformed.\n Please contact the support at support@rms-edf-simulation.de for more information.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            };

            menuItem.setOnAction(event);
        }
    }

    private void populateFormFieldsFromConfigFile(ConfigFile configFile) {
	    schedulerChoiceBox.setValue(configFile.getScheduler());
	    simulationRuntimeField.setText(configFile.getExecutionTime());
	    tasks = configFile.getTasks();
        currentTaskId = tasks.size();
        refreshTable();
    }
}
