package scheduler;

public class Task implements Comparable<Task>{
	private int period;
    private int execution;
    private int remainingE;
    private int entry;
    private int id;

	public Task(int id, int period, int execution){
		this.id = id;
		this.period = period;
		this.execution = execution;
		this.remainingE = execution;
	}

	public int compareTo(Task task) {

		if (this.period >= task.period) {
		    return 1;
		} else {
			return -1;
		}
  	}

    public int getPeriod() {
        return period;
    }

    public int getExecution() {
        return execution;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", period=" + period +
                ", execution=" + execution +
                '}';
    }
}
