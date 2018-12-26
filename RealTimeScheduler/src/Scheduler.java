import java.util.ArrayList;

public abstract class Scheduler {
	protected int endTime;
	protected ArrayList<Task> allTasks = new ArrayList<Task>();

	abstract public boolean isSchedulable();

	abstract public ArrayList<Task> schedule();

	public void calculateEndTime() {
		int lcm = allTasks.get(0).period;
		boolean flag = true;

		//Working on:
		/*
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
		*/
		endTime = lcm;
	}
}
