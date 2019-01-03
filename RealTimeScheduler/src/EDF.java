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
			Collections.sort(allTasks);
			Task highest= allTasks.get(0);
			
			int i=0;
			while((!highest.started(pos))&&(i<allTasks.size()))
			{
				highest=allTasks.get(i);
				i++;
			}
			return highest.deadline;
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
	public Task nextTask(int nextDeadline, int pos)
	{
		for(int i=0; i<allTasks.size(); i++)
		{
			Task t= allTasks.get(i);
			//Find task using the deadline.
			if(nextDeadline == t.deadline)
			{ 
				if(t.started(pos)&&(t.remainingE!=0))
				{
					return t;
				}
			}
		}
		return null;
	}
	
	//Reset the value of remainingE after task execution.
	//Increase count value.
	public void reset(int taskID)
	{
		for(int i=0; i<allTasks.size(); i++)
		{		
			Task t=allTasks.get(i);
			if(t.id==taskID)
			{
				t.resetExecution();
				t.increaseCount();
				refreshDeadline(taskID);
				allTasks.set(i,t);
			}
		}
	}
	
	//Decrement execution number during task execution.
	public void refreshExecution(int taskID)
	{
		for(int i=0; i<allTasks.size(); i++)
		{
			Task t= allTasks.get(i);
			if(taskID==t.id)
			{
				t.remainingE--;
			}
			allTasks.set(i, t);
		}
	}
	
	//Calculate new deadline.
	public void refreshDeadline(int taskID)
	{
		for(int i=0; i<allTasks.size(); i++)
		{
			Task t= allTasks.get(i);
			if(taskID==t.id)
			{
				t.deadline+=t.period;
			}
			allTasks.set(i, t);
		}
	}
	
	public ArrayList<Task> schedule() 
	{	
		for(int i=0; i<endTime; i++)
		{
			int nd= nextDeadline(i);
			Task current= nextTask(nd,i);
			if (current!=null) 
			{
				while((current.remainingE!=0)&&(nextDeadline(i)==current.deadline))
					{
						i++;
						array.add(current);
						refreshExecution(current.id);
					}
						
				if(current.remainingE==0) 
				{
					reset(current.id);
				}
				
				//Time for prior task execution is taken into account.
				i=i-1;
			}
			else
			{
				array.add(null);
			}
		}
		return array;
	}

	public static void main(String[] args) {
		Task a = new Task(2,1,1);
		Task b = new Task(4,1,2);
		Task c = new Task(6,2,3);
		
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
