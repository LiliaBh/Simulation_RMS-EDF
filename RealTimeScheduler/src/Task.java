
public class Task implements Comparable<Task>{
	int period;
	int execution;
	int id;
	int remainingE;
	int deadline;
	int count;
	
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

}
