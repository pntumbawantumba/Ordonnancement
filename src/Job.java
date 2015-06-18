import java.util.ArrayList;

public class Job implements Cloneable {
	
	private ArrayList<Job> listNextJob = null;
	private int[] timeCommTab = null;
	private int timeExec;
	private String label;
	private static final int INFINITY = Integer.MAX_VALUE;
	private int bottomLevelValue;
	private boolean pulled = false;
	
	@Override
	public Job clone() throws CloneNotSupportedException
	{   
		return (Job)super.clone();
	} 
	 
	public Job(String label, int nbrMaxJob)
	{
		this.timeExec = INFINITY;
		this.label = label;
		this.listNextJob = new ArrayList<Job>() ;
		timeCommTab = new int[nbrMaxJob];
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public void setBottomLevelValue(int bottomLevelValue)
	{
		this.bottomLevelValue = bottomLevelValue;
	}
	
	public void setTimeExec(int timeExec)
	{
		this.timeExec = timeExec;
	}
	
	public void setTimeComm(int index, int timeComm)
	{
		timeCommTab[index] = timeComm;
	}
	
	public void setIsPulled(boolean value)
	{
		this.pulled = value;
	}
	
	public boolean isPulled()
    {
    	return this.pulled;
    }
	
	public String getLabel()
	{
		return this.label;
	}
	
	public int getBottomLevelValue()
	{
		return this.bottomLevelValue;
	}
	
	public int getTimeExec()
	{
		return this.timeExec;
	}
	
	public int getTimeComm(String jobName)
	{
		String subStr = jobName.substring(1);
		
		int index = Integer.valueOf(subStr)-1;
		return this.timeCommTab[index];
	}
	
	public int getTimeComm(int index)
	{
		return this.timeCommTab[index];
	}
	
	public ArrayList<Job> getListNextJob()
	{
		return this.listNextJob;
	}
	
	public void addNextJob(Job nextJob)
	{
		this.listNextJob.add(nextJob);
	}

}
