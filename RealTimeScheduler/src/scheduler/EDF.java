package scheduler;
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

			if (ui <=1) 
			{ 
				return true; 
			}
		return false;
	}
	
	public ArrayList<Task> schedule()
	{
		if (isSchedulable()) {
			return generateWorkingSchedule();
		} else {
			return generateNonWorkingSchedule();
		}
	}
	//For unschedulable tasks:
	
	private ArrayList<Task> generateNonWorkingSchedule() 
	{	
			ArrayList<Task> array = new ArrayList<Task>();
			boolean error=false;
			for(int i=0; ((i<endTime)&& !error); i++)
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
							if((current.getRemainingE()!=0)&& (current.getDeadline()==i))
							{
								error=true;
								break;
							}
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
			int dummy = checkSchedule(array);
			return array;
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
	
	private ArrayList<Task> generateWorkingSchedule() 
	{	
		String report= "Successfully Scheduled";
		ArrayList<Task> array = new ArrayList<Task>();
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
		setReport(report);
		return array;
	}

		
	public Task lastTask(ArrayList<Task> result)
	{
		Task t= result.get(result.size()-1);
		return t;
	}

}
