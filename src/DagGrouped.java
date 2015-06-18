import java.util.ArrayList;


public class DagGrouped implements Cloneable {
	
	private DAG[][] listGroupeDag = null;
	private int nbrJobByDag = 0;   // Nombre des jobs par graphe
	private int nbrGroupe = 0;     // Nombre des groupes de graphes
	private int groupedBy = 0;     //Nombre des graphes par groupe
	
	public DagGrouped(int nbrDAG, int groupedBy,int nbrJobByDag)
	{
		this.nbrGroupe = nbrDAG/groupedBy; 
		this.groupedBy = groupedBy; 
		this.nbrJobByDag = nbrJobByDag;
		listGroupeDag = new DAG[nbrGroupe][groupedBy];
	}
	
	@Override
	public DagGrouped clone() throws CloneNotSupportedException
	{   
		return (DagGrouped)super.clone();
	} 
	
	public void generateAllDagsGrouped()
	{
		
		int indexNbrGroupe = 0;
		int indexGroupedBy = 0;
		
		for(indexNbrGroupe = 0; indexNbrGroupe < this.nbrGroupe; indexNbrGroupe++)
		{
			indexGroupedBy = 0;
			while(indexGroupedBy < this.groupedBy)
			{
				DAG dag = new DAG(this.nbrJobByDag,indexNbrGroupe);
				dag.generateSpanningTree();
				dag.additionalRandomArc();
				dag.estimateGranularity();
				dag.computeBottomLevelValue();
				listGroupeDag[indexNbrGroupe][indexGroupedBy] = dag;
				indexGroupedBy++;
			}
		}
	}
	
	public DAG[][] getAllDagsGrouped()
	{
		return this.listGroupeDag;
	}
}
