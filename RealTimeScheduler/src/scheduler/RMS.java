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
			ui += (double) temp.getExecution() / temp.getPeriod();
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
				if (time % (temp.getPeriod()) == 0) {
					for (int j = 0; j < temp.getExecution(); j++) {
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

	public static void main(String[] args) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task(1,6,2));
		tasks.add(new Task(2,7,1));
		tasks.add(new Task(3, 8,1));
		tasks.add(new Task(4, 20,3));


		RMS schedule = new RMS(tasks);
		if (schedule.isSchedulable()) {
			schedule.schedule();

			ArrayList<Task> result = schedule.schedule();
			for (int i = 0; i < result.size(); i++) {
				Task temp = result.get(i);
				if (result.get(i) != null) {
					System.out.print(temp.getId() + " ");
				} else {
					System.out.print("x ");
				}

			}
			System.out.println();
			for (int j = 0; j < schedule.endTime; j++) {
				System.out.print(j + " ");
			}
		} else {
			System.out.println("Schedule is not schedulable");
		}
	}
}
