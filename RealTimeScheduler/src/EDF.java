import java.util.ArrayList;
import java.util.Collections;

public class EDF extends Scheduler {
	
	ArrayList<Task> array = new ArrayList<Task>();

	public EDF(ArrayList<Task> tasks) {
		this.allTasks = tasks;
		calculateEndTime();
	}
	
	//Closest deadline.
	public int nextDeadline(int pos)
	{
		if(pos==0)
		{
			Collections.sort(allTasks);
			Task highest= allTasks.get(0);
			return highest.deadline;
		}
		
		Task temp1= allTasks.get(0);
		//Search for smallest deadline.
		for(int j=0; j<allTasks.size(); j++)
		{	
			Task temp2= allTasks.get(j);
			if((temp1.deadline > temp2.deadline)&&(temp1.remainingE!=0))
			{
				//temp2 is smaller and wasn't executed yet.
				temp1=temp2;
			}
		}
		return temp1.deadline;
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
	
	//Computed through closest deadline.
	public Task nextTask(int nextDeadline)
	{
		for(int i=0; i<allTasks.size(); i++)
		{
			Task t= allTasks.get(i);
			if((nextDeadline % t.deadline)==0)
			{ 
				return t;
			}
		}
		return null;
	}
	
	//Method to reset the value of remainingE to given execution steps for the next iteration.
	public void reset()
	{
		for(int i=0; i<allTasks.size(); i++)
		{
			Task t= allTasks.get(i);
			t.remainingE=t.execution;
		}
	}
	
	public ArrayList<Task> schedule() 
	{	
		for(int i=0; i<endTime; i++)
		{
			int nd= nextDeadline(i);
			Task current= nextTask(nd);
			if (current!=null) 
			{
				for(int j=0; j<current.execution; j++)
					{
						array.add(current);
						current.remainingE--;
					}
				
				//Next deadline for the just executed task.
				current.deadline+=current.period;
				
				//Time for prior task execution is taken into account.
				i+=(current.execution-1);
				reset();
			}
			else
			{
				array.add(null);
			}
		}
		return array;
	}

	public static void main(String[] args) {
		Task a = new Task(3,1,1);
		Task b = new Task(4,1,2);
		Task c = new Task(6,1,3);
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(a);
		tasks.add(b);
		tasks.add(c);
		
		EDF schedule = new EDF(tasks);
		if (schedule.isSchedulable()) 
		{
			ArrayList<Task> result = schedule.schedule();
			for (int i = 0; i < result.size(); i++) 
			{
				Task temp = result.get(i);
				if (temp != null) 
				{
					System.out.print(temp.id + " ");
				} 
				else 
				{
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
