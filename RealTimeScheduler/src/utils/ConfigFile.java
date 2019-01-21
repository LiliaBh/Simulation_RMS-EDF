package utils;

import exception.MalformedConfigFileException;
import scheduler.SchedulerFactory;
import scheduler.Task;

import java.util.ArrayList;

public class ConfigFile {
    private String scheduler;

    private String executionTime;
    private ArrayList<Task> tasks;

    public ConfigFile()
    {
        tasks = new ArrayList<>();
    }

    public void addTask(String stringPeriod, String stringExecution, String stringId) throws MalformedConfigFileException {
        try {

            int period = Integer.parseInt(stringPeriod);
            int execution = Integer.parseInt(stringExecution);
            int id = Integer.parseInt(stringId);
            tasks.add(new Task(period, execution, id));
        } catch (NumberFormatException e) {
            throw new MalformedConfigFileException();
        }
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) throws MalformedConfigFileException {
        if (scheduler == null || scheduler.equals("") ||
            (!scheduler.equals(SchedulerFactory.RMS) && !scheduler.equals(SchedulerFactory.EDF))) {
            throw new MalformedConfigFileException();
        }
        this.scheduler = scheduler;
    }

    public String getExecutionTime() {
        if (executionTime == null) {
            return "";
        }
        return executionTime;
    }

    public void setExecutionTime(String executionTime) throws MalformedConfigFileException {
        if (executionTime.equals("")) {
            return;
        }

        try {
            Integer.parseInt(executionTime);
            this.executionTime = executionTime;
        } catch (NumberFormatException e) {
            throw new MalformedConfigFileException();
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
