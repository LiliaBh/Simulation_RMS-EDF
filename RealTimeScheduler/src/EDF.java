import java.util.ArrayList;
import java.util.Collections;

public class EDF extends Scheduler {

	public EDF(ArrayList<Task> tasks) {
		this.allTasks = tasks;
		calculateEndTime();
	}
	
	public EDF(ArrayList<Task> tasks, int endTime) {
		this.allTasks = tasks;
		this.endTime = endTime;
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

	public ArrayList<Task> schedule() {

		ArrayList<Task> tempList = new ArrayList<Task>();
		
		for (int t=0; t<endTime; t++)
		{
			for (int i=0; i< allTasks.size(); i++)
			{
				Task task=allTasks.get(i);
				if(t%task.period==0)
					{
						for(int j=0; j<task.execution; j++)
						{
							tempList.add(task);
						}
					}
			}
			
			if (!(tempList.isEmpty())) 
			{
				Collections.sort(tempList);
		//		tempList.add(tempList.remove(0));
			}	
		//	else 
		//	{
		//	 tempList.add(null);
		//	}
		}
		
		return tempList;
	}
	
	public static void main(String[] args) 
	{
		Task a = new Task(6, 2, 1);
		Task b = new Task(5, 1, 2);
		Task c = new Task(10, 1, 3);
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(a);
		tasks.add(b);
		tasks.add(c);
		EDF edf = new EDF(tasks);
		if (edf.isSchedulable()) {
			ArrayList<Task> result = edf.schedule();
			for (int i = 0; i < result.size(); i++) {
				Task temp = result.get(i);
				if (temp != null) {
					System.out.print(temp.id + " ");
				} else {
					System.out.print("x ");
				}

			}}
	}

}
