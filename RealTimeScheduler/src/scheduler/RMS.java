package scheduler;
import java.util.ArrayList;

import java.util.Collections;

public class RMS extends Scheduler {
	ArrayList<Task> toSchedule = new ArrayList<Task>();

	public RMS(ArrayList<Task> tasks) {
		this.allTasks = tasks;
		calculateEndTime();
	}
	
	public RMS(ArrayList<Task> tasks, int endTime) {
		this.allTasks = tasks;
		this.endTime = endTime;
	}

	public boolean isSchedulable() {
		double ui = 0;
		double n = allTasks.size();
		for (int i = 0; i < allTasks.size(); i++) {
			Task temp = allTasks.get(i);
			ui += (double) temp.execution / temp.period;
		}
		System.out.println(ui);
		System.out.println(n * (Math.pow(2, 1 / n) - 1));
		if (ui < (n * (Math.pow(2, 1 / n) - 1))) {
			return true;
		}
		return false;
	}

	public ArrayList<Task> schedule() {
		ArrayList<Task> ready = new ArrayList<Task>();
		for (int time = 0; time < endTime; time++) {
			for (int i = 0; i < allTasks.size(); i++) {
				Task temp = allTasks.get(i);
				if (time % (temp.period) == 0) {
					for (int j = 0; j < temp.execution; j++) {
						toSchedule.add(temp);
					}
				}
			}
			if (!(toSchedule.isEmpty())) {
				Collections.sort(toSchedule);
				ready.add(toSchedule.remove(0));
			} else {
				
				ready.add(null);
			}
		}
		return ready;
	}
}