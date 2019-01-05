package scheduler;

import java.util.ArrayList;

public class SchedulerFactory {
    public static final String RMS = "RMS";
    public static final String EDF = "EDF";

    private static SchedulerFactory instance;

    public static SchedulerFactory getInstance()
    {
        if (instance == null) {
            instance = new SchedulerFactory();
        }

        return instance;
    }

    private SchedulerFactory()
    {

    }

    public Scheduler getScheduler(String Scheduler, ArrayList<Task> tasks)
    {
        Scheduler scheduler;

        switch (Scheduler) {
            case RMS:
                scheduler = new RMS(tasks);
                break;
            // TODO: add EDF case
            default:
                throw new UnsupportedOperationException();
        }

        return scheduler;
    }

    public Scheduler getScheduler(String Scheduler, ArrayList<Task> tasks, int executionTime)
    {
        Scheduler scheduler;

        switch (Scheduler) {
            case RMS:
                scheduler = new RMS(tasks, executionTime);
                break;
            // TODO: add EDF case
            default:
            throw new UnsupportedOperationException();
        }

        return scheduler;
    }
}
