package scheduler;
import java.util.ArrayList;

public abstract class Scheduler {
	protected int endTime;
	protected ArrayList<Task> allTasks = new ArrayList<Task>();	
	protected String report;
	
	abstract public boolean isSchedulable();

	abstract public ArrayList<Task> schedule();
	
	public void setReport(String report) {
		this.report = report;
	}
	
	public int checkSchedule(ArrayList<Task> ready) {
		boolean flag = false;
		String report = "Successfully Scheduled";
		int errorAt = ready.size();
		
		for (int i = 0; i < allTasks.size(); i++) {
			Task temp = allTasks.get(i);
			int period = temp.period;
			int execution = temp.execution;
			int count = 0;

			for (int j = 0; j < ready.size(); j++) {
				Task test = ready.get(j);
				if (!(test == (null))) {
			
					if (test.id == temp.id) {
						count++;
					}
					
					if (j != 0 && (j + 1) % period == 0) {
					
						if (count < execution) {
							flag = true;
							report = "Tasks are not schedulable: Task " + temp.id + " is missing its deadline " + ( j + 1) + " with " + (execution - count) + " execution step(s) missing.";
							errorAt = j + 1 ;
							break;
						}
						count=0;
						
					}
				}
			}
			
			if (flag) {
				break;
			}
		}
		
		setReport(report);
		return errorAt;
	}

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

	public String getReport()
    {
        return report;
    }
}
