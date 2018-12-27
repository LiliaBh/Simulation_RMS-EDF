import java.util.ArrayList;
import java.util.Collections;

public class EDF extends Scheduler {
	
	ArrayList<Task> toSchedule = new ArrayList<Task>();

	public EDF(ArrayList<Task> tasks) {
		this.allTasks = tasks;
		calculateEndTime();
	}
	
	
	//Exact test = sufficient + necessary.
	public boolean isSchedulable() {
			double ui = 0;
			for (int i = 0; i < allTasks.size(); i++) 
			{
				Task temp = allTasks.get(i);
				ui +=((double) temp.execution /(double) temp.period);
			}
			
			System.out.println(ui);
			if (ui <=1) 
			{ 
				return true; 
			}
		return false;
	}
	
	public ArrayList<Task> schedule() 
	{
		for (int time = 0; time < endTime; time++) 
		{
			for (int i = 0; i < allTasks.size(); i++) 
			{
				Task temp = allTasks.get(i);
				if (time % (temp.period) == 0) 
				{
					for (int j = 0; j < temp.execution; j++) 
					{
						toSchedule.add(temp);
					}
				}
			}
		}
		return toSchedule;
	}

	public static void main(String[] args) {
		Task a = new Task(3,1,1);
		Task b = new Task(5,2,2);
		Task c = new Task(6,1,3);
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(a);
		tasks.add(b);
		tasks.add(c);
		EDF schedule = new EDF(tasks);
		if (schedule.isSchedulable()) 
		{
			schedule.schedule();
			ArrayList<Task> result = schedule.schedule();
			for (int i = 0; i < result.size(); i++) {
				Task temp = result.get(i);
				if (temp != null) {
					System.out.print(temp.id + " ");
				} else {
					System.out.print("x ");
				}

			}
			
			System.out.println();
			for (int j = 0; j < result.size(); j++) 
			{
				System.out.print(j + " ");
			}
		} 
		else 
		{
			System.out.println("Schedule is not schedulable");
		}
	}

}
