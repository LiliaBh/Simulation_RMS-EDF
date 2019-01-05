package scheduler;

public class Task implements Comparable<Task>{
	int period;
	int execution;
	int id;
	int remainingE;
	int deadline;
	int count;
	
	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getExecution() {
		return execution;
	}

	public void setExecution(int execution) {
		this.execution = execution;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRemainingE() {
		return remainingE;
	}

	public void setRemainingE(int remainingE) {
		this.remainingE = remainingE;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Task(int period,int execution,int id){
		this.id = id;
		this.period = period;
		this.execution = execution;
		this.deadline= period;
		this.remainingE= execution;
		this.count=0;
	}

	 public int compareTo(Task task) {
         if(this.deadline>= task.deadline){
        	 return 1;
         }else{
        	 return -1;
         }
     }
	  
	//A method to check if a task is executable.
	 public boolean started(int position)
		{
			if(position>=(this.count*this.period))
			{
				return true;
			}
			return false;
		}
	 
	//Increase count value after complete execution.
	 public void increaseCount()
	 {
		 this.count++;
	 }
	 
	//Reset the value of remainingE after task execution.
	 public void resetExecution()
	 {
		this.remainingE=this.execution;
	 }
	 
	    @Override
	    public String toString() {
	        return "Task{" +
	                "id=" + id +
	                ", period=" + period +
	                ", execution=" + execution +
	                '}';
	    }
}
