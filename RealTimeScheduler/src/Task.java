
public class Task implements Comparable<Task>{
	int period;
	int execution;
	int remainingE;
	int entry;
	int id;
	public Task(int period,int execution,int id){
		this.id = id;
		this.period = period;
		this.execution = execution;
		this.remainingE = execution;
	}

	  public int compareTo(Task task) {
         if(this.period>= task.period){
        	 return 1;
         }else{
        	 return -1;
         }
  }

}
