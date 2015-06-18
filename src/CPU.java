import java.util.ArrayList;


public class CPU {
	
	private int numero;
	private ArrayList<Job> executedJobsList;
	private int availability = 0;
	
	public CPU(int numero)
	{
		this.numero = numero;
		executedJobsList = new ArrayList<Job>();
	}
	
	public int getAvailability()
	{
		return this.availability;
	}
	
	public void setAvailability(int availability)
	{
		this.availability = availability;
		//System.out.println("PROC"+numero+" Nouvelle disponibilte est à "+this.availability);
	}
	
	public void executeJob(Job job)
	{
		executedJobsList.add(job);
		//System.out.println("CPU"+this.numero+": J'execute +"+job.getLabel()+" ExecTime("+job.getTimeExec()+")");
	}
	
	public ArrayList<Job> getExecutedJobsList()
	{
		return this.executedJobsList;
	}
}
