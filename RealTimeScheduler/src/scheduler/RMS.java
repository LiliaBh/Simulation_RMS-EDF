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

		if (!(isSchedulable())) {
			int errorAt = checkSchedule(ready);
			for (int i = ready.size() - 1 ; i >= errorAt; i--) {
				ready.remove(i);
			}
			
		} else {
			String report = "Successfully Scheduled";
			setReport(report);
		}

		return ready;
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
							report = "Task " + temp.id + " is missing its deadline " + ( j + 1) + ": " + (execution - count) + " execution step(s) missing.";
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

}