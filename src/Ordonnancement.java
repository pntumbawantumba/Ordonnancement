import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Ordonnancement {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
		int nombreGraphe = 60;
		int groupeDe = 6;
		int nombreJob = 100;
		Platform platform = null;
		DagGrouped dagGrouped = new DagGrouped(nombreGraphe, groupeDe, nombreJob), dagGrouped1 = null;
		dagGrouped.generateAllDagsGrouped();
		try {
			dagGrouped1 = (DagGrouped)dagGrouped.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int indexGroup = 0;
		int indexGroupedBy = 0;
		
		/*for(indexGroup = 0; indexGroup < nombreGraphe/groupeDe; indexGroup++)
		{
			System.out.println("");
			System.out.println("********************Groupe("+(indexGroup+1)+")********************");
			System.out.println("");
			indexGroupedBy = 0;
			while(indexGroupedBy < groupeDe)
			{
				System.out.println("");
				System.out.println("========== Graphe"+(indexGroupedBy+1)+"("+dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].getGranularity()+")=================");
				int index = 0;
				while(index < dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].getJobNumber())
				{
					System.out.print(dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].jobBoard[index].getLabel()+" => ");
					System.out.print("ExecTime("+dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].jobBoard[index].getTimeExec()+") ");
					System.out.print("BLVal("+dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].jobBoard[index].getBottomLevelValue()+") ");
					if(!dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].jobBoard[index].getListNextJob().isEmpty())
					{
						System.out.print("NextJob(");
						int indexList = 0;
						while(indexList < dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].jobBoard[index].getListNextJob().size())
						{
							System.out.print(dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].jobBoard[index].getListNextJob().get(indexList).getLabel());
							System.out.print("["+dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].jobBoard[index].getListNextJob().get(indexList).getTimeExec()+"]");
							System.out.print("=>("+dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].jobBoard[index].getListNextJob().get(indexList).getTimeComm(index)+") ");
							indexList++;
						}
						System.out.print(")");
						indexList=0;
					}
					else
					{
						System.out.print("NextJob : Empty");
					}
					
					System.out.println("");
					index++;
				}
				indexGroupedBy++;
			}
		}
		
		*/  
		
		int octoMakespan = 0;
		XYSeries octoSeries = new XYSeries("Results on 8 cores");
		for(indexGroup = 0; indexGroup < nombreGraphe/groupeDe; indexGroup++)
		{
			
			float granularite = 0;
			indexGroupedBy = 0;
			while(indexGroupedBy < groupeDe)
			{
				platform = new Platform(8,dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy]);
				platform.runAlgoCTS();
				octoMakespan += dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].getMakespan();
				granularite += dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].getGranularity();
				indexGroupedBy++;
			}
			octoSeries.add((granularite/6), (float)(octoMakespan/6));
		}
		octoSeries.setDescription("Results on 8 cores (Makespan "+(float)(octoMakespan/6)+")");
		
		for(indexGroup = 0; indexGroup < nombreGraphe/groupeDe; indexGroup++)
		{
			indexGroupedBy = 0;
			while(indexGroupedBy < groupeDe)
			{
				
				dagGrouped.getAllDagsGrouped()[indexGroup][indexGroupedBy].resetJobState();
				indexGroupedBy++;
			}
		}
		
		
		XYSeries monoSeries = new XYSeries("Results on 1 core",true,false);
		int monoMakespan = 0;
		for(indexGroup = 0; indexGroup < nombreGraphe/groupeDe; indexGroup++)
		{
			
			float granularite = 0;
			indexGroupedBy = 0;
			while(indexGroupedBy < groupeDe)
			{
				platform = new Platform(1,dagGrouped1.getAllDagsGrouped()[indexGroup][indexGroupedBy]);
				platform.runAlgoCTS();
				monoMakespan += dagGrouped1.getAllDagsGrouped()[indexGroup][indexGroupedBy].getMakespan();
				granularite += dagGrouped1.getAllDagsGrouped()[indexGroup][indexGroupedBy].getGranularity();
				indexGroupedBy++;
			}
			monoSeries.add((granularite/6), (float)(monoMakespan/6));
		}
		octoSeries.setDescription("Results on 1 core (Makespan "+(float)(monoMakespan/6)+")");
		
		DecimalFormat myFormatter = new DecimalFormat("#.#");
		System.out.print("    Makespan sur un 8 core    |   ");
		System.out.println("Makespan sur un mono core");
		System.out.println("*********************************************************");
		System.out.println("");
		System.out.print("Granularité    | Makespan  ");
		System.out.println("  |  Granularité | Makespan ");
		System.out.println("-------------------------------------------------------------");
		for(int i=0; i<10; i++)
		{
			System.out.print(myFormatter.format(octoSeries.getX(i))+"            |   "+octoSeries.getY(i));
			System.out.println("   |         "+myFormatter.format(monoSeries.getX(i))+"       |   "+monoSeries.getY(i));
			System.out.println("-------------------------------------------------------------");
		}
		  XYDataset xyOctoDataset = new XYSeriesCollection(octoSeries);
		  XYDataset xyMonoDataset = new XYSeriesCollection(monoSeries);
		  
		  JFreeChart octoChart = ChartFactory.createXYAreaChart
		  ("", "Granularity", "Makespan",xyOctoDataset, PlotOrientation.VERTICAL, true,true, false);
		  
		  JFreeChart monoChart = ChartFactory.createXYAreaChart
		  ("", "Granularity", "Makespan",xyMonoDataset, PlotOrientation.VERTICAL, true,true, false);
		  
		  ChartPanel octoPanel =new ChartPanel(octoChart);
		  ChartPanel monoPanel =new ChartPanel(monoChart);
		  JFrame myFrame = new JFrame("Projet ordonnance");
		  myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          myFrame.setLocationRelativeTo(null);
		  myFrame.setLayout(new GridLayout(1, 2));
		  myFrame.add(octoPanel);
		  myFrame.add(monoPanel);
		  myFrame.setVisible(true);
		  myFrame.setSize(600,600);
		
	}

}
