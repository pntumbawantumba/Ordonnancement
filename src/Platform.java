import java.util.ArrayList;


public class Platform {
	
	private ArrayList<Job> freeJobsList;
	private ArrayList<CPU> octaCPU;
	private Job priorityJob = null;
	private int makespan = 0;
	private DAG dag;
	
	public Platform(int numberCPU, DAG dag)
	{
		octaCPU = new ArrayList<CPU>();
		for(int i = 0; i < numberCPU; i++)
		{
			CPU cpu = new CPU(i+1);
			octaCPU.add(cpu);
		}
		
		freeJobsList = new ArrayList<Job>();
		this.dag = dag;
	}
	
	
	public void addFreeJobToList(Job job)
	{
		this.freeJobsList.add(job);
	}
	
	public void setPriorityJob(Job priorityJob)
	{
		this.priorityJob = priorityJob;
	}
	
	public Job getPriorityJob()
	{
		return this.priorityJob;
	}
	
	public void findPriorityJob()
	{
		int maxBLVal = 0;
		Job priorityJob = null;
		int index = 0;
		int indexJob = 0;
		while(index < this.freeJobsList.size())
		{
			if(this.freeJobsList.get(index).getBottomLevelValue() >= maxBLVal)
			{
				priorityJob = this.freeJobsList.get(index);
				maxBLVal = this.freeJobsList.get(index).getBottomLevelValue();
				indexJob = index;
			}
			index++;
		}
		
		if(priorityJob != null)
		{
			freeJobsList.remove(indexJob);
			this.setPriorityJob(priorityJob);
		}
		
		
	}
	
	public ArrayList<Job> getFreeJobsList()
	{
		return this.freeJobsList;
    }
	
	public void getAvailableCPU(Job jobToExecute)
	{
		int index = 0;
		int indexCPU = 0;	
		int indexCPUFound = 0;	
		int availableValue = 0;
		Job executedJob = null;
		int commTime = 0;
		boolean foundJob = false;
		String value = "";
		while(index < this.octaCPU.size())
		{
			//int indexExecutedJob = 0;
			int i = 0;
			while(i < this.octaCPU.get(index).getExecutedJobsList().size())
			{
				executedJob = this.octaCPU.get(index).getExecutedJobsList().get(i);
				if(executedJob != null && executedJob.getListNextJob().contains(jobToExecute))
				{
					if(i < this.octaCPU.get(index).getExecutedJobsList().size()-1)
					{
						availableValue = this.octaCPU.get(index).getAvailability() - this.octaCPU.get(index).getExecutedJobsList().get(i).getTimeExec();
						commTime = jobToExecute.getTimeComm(executedJob.getLabel());
						indexCPU = index;
						indexCPUFound = index;
						foundJob = true;
					}
					else
					{
						if(availableValue < this.octaCPU.get(index).getAvailability())
						{
							commTime = jobToExecute.getTimeComm(executedJob.getLabel());
							availableValue = this.octaCPU.get(index).getAvailability();
							indexCPU = index;
							indexCPUFound = index;
							foundJob = true;
						}
					}
				}
				i++;
			}
			index++;
		}	
		
		index = 0;
		if(foundJob == true)
		{
			
			while(index <  this.octaCPU.size())
			{
				if(index != indexCPU)
				{
					
					int val = this.octaCPU.get(index).getAvailability();
					if(availableValue > (val + (availableValue - val) + commTime))
					{
						availableValue = availableValue + commTime;
						indexCPU = index;
					}
				}
				index++;
			}
			
		}
		else
		{
			Utility utility = new Utility(0);
			int randIndex = utility.getRandom(0, this.octaCPU.size()-1);
			while(index <  this.octaCPU.size())
			{
				if(this.octaCPU.get(randIndex).getAvailability() > this.octaCPU.get(index).getAvailability())
				{
					availableValue = this.octaCPU.get(index).getAvailability();
					indexCPU = index;
				}
				else
				{
					availableValue = this.octaCPU.get(randIndex).getAvailability();
					indexCPU = randIndex;
				}
				index++;
			}
		}
		executeJobOnCPU(indexCPU, jobToExecute, availableValue);
	}
	
	public void pullFreeJob()
	{
		int index = 0;
		int indexFreeJob = 0;
		
		while(index < dag.jobNumber)
		{
			if(dag.jobBoard[index].isPulled() == true)
			{
				index++;
			}
			
			boolean isFree = false;
			int i =0;
			while(i < dag.jobNumber)
			{
				if(dag.jobBoard[i].isPulled() == false)
				{
					if(dag.jobBoard[i].getListNextJob().contains(dag.jobBoard[index]))
					{
						isFree = false;
						//System.out.println("contient");
					}
					else
					{
						isFree = true;
						indexFreeJob = index + 1;
						//System.out.println("Ne contien pas");
					}
				}
				
				i++;
			}
			
			
			if(indexFreeJob > 0 && isFree == true)
			{
			
				this.addFreeJobToList(dag.jobBoard[indexFreeJob-1]);
				dag.jobBoard[indexFreeJob-1].setIsPulled(true);
				indexFreeJob = 0;
			}
			index++;
		}
		
	}
	
	public void executeJobOnCPU(int index, Job job, int newAvailability)
	{
		this.octaCPU.get(index).executeJob(job);
		this.octaCPU.get(index).setAvailability(newAvailability+job.getTimeExec());
	}


	public void runAlgoCTS() {
		// TODO Auto-generated method stub
		for(int i=0; i < this.dag.jobNumber; i++)
		{
			pullFreeJob();
			findPriorityJob();
			getAvailableCPU(this.getPriorityJob());
		}
		int index = 0;
		while(index < this.octaCPU.size())
		{
			dag.setMakespan(Math.max(this.octaCPU.get(index).getAvailability(), dag.getMakespan()));
			index++;
		}
	}
}
