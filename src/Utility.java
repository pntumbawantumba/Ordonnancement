import java.util.ArrayList;


public class Utility{

	
	private IntervalleTime[] boardIntervalleTime = null;
	private int nbrDagGroup;
	
	public Utility(int nbrDagGroup)
	{
		boardIntervalleTime = new IntervalleTime[nbrDagGroup];
		this.nbrDagGroup = nbrDagGroup;
		
		for(int index = 0; index < nbrDagGroup; index++)
		{
			//System.out.println("Bonjour index = "+index+"  nbrDagGroup ="+nbrDagGroup);
			IntervalleTime intervalleTimeObject = new IntervalleTime(index);
			//System.out.println("Bonjour index = "+index+"  nbrDagGroup ="+nbrDagGroup);
			boardIntervalleTime[index] = intervalleTimeObject;
			//System.out.println("Bonjour index = "+index+"  nbrDagGroup ="+nbrDagGroup);
		}
		
	}
	
	public IntervalleTime[] getIntervalleTime()
	{
		return this.boardIntervalleTime;
	}
	
	public int getRandom(int min, int max)
	{
		return (min + (int)(Math.random() * ((max - min) + 1)));
	}
}
