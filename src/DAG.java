import java.math.RoundingMode;
import java.text.DecimalFormat;


public class DAG implements Cloneable{
	
	int jobNumber = 0;
	int nbrDagGroup = 0;
	Job [] jobBoard;
	Utility utility = null;
	float granularity = 0;
	private int makespan;
	
	public DAG(int jobNumber, int nbrDagGroup)
	{
		this.jobNumber = jobNumber;
		jobBoard = new Job[this.jobNumber];
		this.nbrDagGroup = nbrDagGroup;
		utility = new  Utility(10);
	}
	
	@Override
	public DAG clone() throws CloneNotSupportedException
	{   
		return (DAG)super.clone();
	} 
	
	public void setGranularity(float granularity)
	{
		this.granularity = granularity;
	}
	
    public void setJobNumber(int jobNumber)
    {
    	this.jobNumber = jobNumber;
    }
    
    public void setMakespan(int makespan)
	{
		this.makespan = makespan;
	}

	public int getMakespan()
	{
		return this.makespan; 
	}
	
    public int getJobNumber()
    {
    	return this.jobNumber;
    }
    
    public float getGranularity()
    {
    	return this.granularity;
    }
    
    
    public void estimateGranularity()
    {
        int somme =  utility.getIntervalleTime()[this.nbrDagGroup].getMinExecutionTime() +  utility.getIntervalleTime()[this.nbrDagGroup].getMaxExecutionTime();
        int divise = somme/2;
        this.granularity = (float)(divise)/100;
        setGranularity(this.granularity);
        
    	
    }
    
    public void addJobToDag(Job job, int index)
    {
    	this.jobBoard[index] = job;
    }
    
    public void resetJobState()
    {
    	for(int index=0; index < this.getJobNumber(); index++)
		{
    		this.jobBoard[index].setIsPulled(false);
		}
    }
    
    public void generateSpanningTree()
    {
    	int index = 0;
    	for(index=0; index < this.getJobNumber(); index++)
		{
			Job job = new Job("t"+(index+1),this.getJobNumber());
			job.setTimeExec
			(
                utility.getRandom
                (
                	utility.getIntervalleTime()[this.nbrDagGroup].getMinExecutionTime(),
                    utility.getIntervalleTime()[this.nbrDagGroup].getMaxExecutionTime()
                )
			);
			
			this.addJobToDag(job, index);
		}
    	
    	for( index = (this.jobNumber-2); index >= 0; index--)
    	{
    		int randomIndexJob = utility.getRandom(index+1, this.jobNumber-1);
    		this.jobBoard[randomIndexJob].setTimeComm
			(
				index,
				utility.getRandom
			    (
        		   utility.getIntervalleTime()[this.nbrDagGroup].minCommTime,
        		   utility.getIntervalleTime()[this.nbrDagGroup].maxCommTime
			    )
			);
    		this.jobBoard[index].addNextJob(this.jobBoard[randomIndexJob]);
    	}
    	
    }
    
    public void additionalRandomArc()
    {
    	int index = 1;
    	int nbrRandomArc = (utility.getRandom(this.jobNumber-1, (this.jobNumber*(this.jobNumber-1))/2));
    	nbrRandomArc = nbrRandomArc - (this.jobNumber-1);
    	while(index <= nbrRandomArc )
    	{
    		int indexCurrentJob = utility.getRandom(0, this.jobNumber-1);
    		int indexNextJob = utility.getRandom(0, this.jobNumber-1);
    	    if(indexCurrentJob > indexNextJob)
    		{
    			indexCurrentJob = indexCurrentJob + indexNextJob;
    			indexNextJob = indexCurrentJob - indexNextJob;
    			indexCurrentJob = indexCurrentJob - indexNextJob;
    		}
    		if
    		(
    		   indexCurrentJob < indexNextJob && 
    		   !this.jobBoard[indexCurrentJob].getListNextJob().contains(this.jobBoard[indexNextJob]) &&
    		   !this.jobBoard[indexCurrentJob].getListNextJob().equals(this.jobBoard[indexNextJob])
       		
    		)
    		{
    			this.jobBoard[indexNextJob].setTimeComm
    			(
    			    indexCurrentJob,
    				utility.getRandom
    			    (
            		   utility.getIntervalleTime()[this.nbrDagGroup].minCommTime,
            		   utility.getIntervalleTime()[this.nbrDagGroup].maxCommTime
    			    )
    			);
    			this.jobBoard[indexCurrentJob].addNextJob(this.jobBoard[indexNextJob]);
    			index++;
    		}
    	}
    	
    }
    
    public void computeBottomLevelValue()
    {
    	int index = this.jobNumber - 1;
    	while(index >= 0)
    	{
    		
    		// Si c'est la tache de sortie, on affecte son temps d'execution comme sa valeur de bottom levl
    		if(this.jobBoard[index].getListNextJob().isEmpty())
    		{
    			this.jobBoard[index].setBottomLevelValue(this.jobBoard[index].getTimeExec());
    			index--;
    		}
    		else // Si ce n'est pas la tache de sortie, on le calcule
    		{
    			int i = 0;
    			int bottomLeveleValue = 0;
    			while(i < this.jobBoard[index].getListNextJob().size())
    			{
    				int bottomLevel = 0;
    				bottomLevel += this.jobBoard[index].getTimeExec();
    				bottomLevel += this.jobBoard[index].getListNextJob().get(i).getTimeComm(index);
    				bottomLevel += this.jobBoard[index].getListNextJob().get(i).getBottomLevelValue();
    				bottomLeveleValue = Math.max(bottomLeveleValue, bottomLevel);
    				i++;
        		}
    			this.jobBoard[index].setBottomLevelValue(bottomLeveleValue);
        		index--;
    		}
    		
    		
    		
    	}
    	//this.granularity = (this.granularity/this.jobNumber) / 100 ;
    }
}
