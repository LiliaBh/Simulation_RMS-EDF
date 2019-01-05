package scheduler;
import java.util.ArrayList;

public abstract class Scheduler {
	protected int endTime;
	protected ArrayList<Task> allTasks = new ArrayList<Task>();	

	abstract public boolean isSchedulable();

	abstract public ArrayList<Task> schedule();

	//A method to update if schedule unschedulable.
	//abstract public void setUnschedulable(ArrayList<Task> array);

	public void calculateEndTime() {
		int lcm = allTasks.get(0).period;
		boolean flag = true;

		while (flag == true) {
			for (int i = 0; i < allTasks.size(); i++) {
				Task temp = allTasks.get(i);
				
				if (lcm % temp.period != 0) {
					flag = true;
					break;
				}
				flag = false;
			}
			
			if (flag == true) {
				lcm = lcm + 1;
			}
		}
		endTime = lcm;
	}
	public int getEndTime() {
		return endTime;
	}

	public String generateReport()
    {
        return "Simulation successful.";
    }
}
