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
			return highest.getDeadline();
	}
	
	//Exact test = sufficient + necessary.
	public boolean isSchedulable() {
			double ui = 0;
			for (int i = 0; i < allTasks.size(); i++) 
			{
				Task temp = allTasks.get(i);
				ui +=((double) temp.getExecution() /(double) temp.getPeriod());
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
			if(nextDeadline == t.getDeadline())
			{ 
				if(t.started(pos)&&(t.getRemainingE()!=0))
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
			if(taskID==t.getId())
			{
				t.deadline+=t.getPeriod();
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
				while((current.getRemainingE()!=0)&&(nextDeadline(i)==current.getDeadline()))
					{
						i++;
						array.add(current);
						refreshExecution(current.getId());
					}
						
				if(current.getRemainingE()==0) 
				{
					reset(current.getId());
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

	/*
	public void setUnschedulable(ArrayList<Task> array) 
	{	
		int pos=0;
		Task task= new Task(0,0,0);
		for(int i=0; i<array.size();i++)
		{
			Task t= array.get(i);
			if((t.getRemainingE()>0)&&(i+1>=t.getDeadline()))
			{
				pos=i;
				break;
			}
		}
		
		for (int j=pos; j<array.size();j++)
		{
			array.set(j, task);
		}
	}
	
	*/
	public static void main(String[] args) {
		Task a = new Task(3, 1, 1);
		Task b = new Task(2, 1, 2);
//		Task c = new Task(6, 1, 3);
//		Task d = new Task(20, 3, 4);
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(a);
		tasks.add(b);
	//	tasks.add(c);
	//	tasks.add(d);

		EDF schedule = new EDF(tasks);
		ArrayList<Task> result = schedule.schedule();
		
		if (schedule.isSchedulable()) 
		{
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
			//schedule.setUnschedulable(result);
			System.out.println("Schedule is not schedulable");
		}
	}

}
