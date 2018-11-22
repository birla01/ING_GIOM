package com.sepatransfer.cronjobs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class ReportGenerateJob implements Job{

	public static void main(String[] args) {
		
		JobDetail job1 = JobBuilder.newJob(ReportGenerateJob.class)
                .withIdentity("job1", "group1").build();

        Trigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity("cronTrigger1", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/15 * * ? * *"))
                .build();
         
        Scheduler scheduler1;
		try {
			scheduler1 = new StdSchedulerFactory().getScheduler();
			scheduler1.start();
	        scheduler1.scheduleJob(job1, trigger1);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void runJob(){
		String basePath = System.getProperty("user.dir");
		String painFolderPath = basePath + "/pain";
		String ErrorFolderPath = basePath + "/error";
		String xmlFolderPath = basePath + "/xml";
		String exceptionFolderPath = basePath + "/exception";
		int successCount = runFilesScan(painFolderPath);
		int errorCount = runFilesScan(ErrorFolderPath);
		int xmlCount = runFilesScan(xmlFolderPath);
		int exceptoinCount = runFilesScan(exceptionFolderPath);
		printReport(successCount, errorCount, xmlCount, exceptoinCount);
	}

	private static void printReport(int successCount, int errorCount, int xmlCount, int exceptoinCount){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		System.out.println("\nDear XYZ, \nPlease find PLI to PAIN conversion stastics for " + formatter.format(new Date()) );
		System.out.println("=========================================================================");
		System.out.printf("%-20S %-20s%n", "Type", "Count");
		System.out.printf("%-20S %-20s%n", "------", "-------");
		System.out.printf("%-20S %-20s%n", "Success", successCount);
		System.out.printf("%-20S %-20s%n", "Error", errorCount);
		System.out.printf("%-20S %-20s%n", "XML", xmlCount);
		System.out.printf("%-20S %-20s%n", "Exception", exceptoinCount);
		System.out.println("\nPlease find attached detailed report for Error files failures.");
		
	}
	
	private static int runFilesScan(String path){
		int count = 0;
		try{
			File textErrDir = new File(path);
			  File[] directoryListing = textErrDir.listFiles();
			  if (directoryListing != null) {
			    for (File child : directoryListing) {
			    	if(child.isFile()){
			    		count++;
			    	}
			    }
			  } else {
				  System.out.println("No files found in Text Error folder.");
			  }			
		}
		catch(Exception e){
			System.out.println("Error occured while scanning folder.");
			e.printStackTrace();
		}
		
		return count;
	}


	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		runJob();
	}
	
}
