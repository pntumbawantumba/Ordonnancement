
public class IntervalleTime {
	
	private int minExecTime = 10;
	private int maxExecTime = 30;
	public static final int minCommTime = 80;
	public static final int maxCommTime = 120;
	
	public IntervalleTime(int groupIndex)
	{
		this.minExecTime = this.minExecTime + (20*groupIndex);
		this.maxExecTime = this.maxExecTime + (20*groupIndex);
	}
	
	public int getMinExecutionTime()
	{
		return this.minExecTime;
	}
	
	public int getMaxExecutionTime()
	{
		return this.maxExecTime;
	}
}
