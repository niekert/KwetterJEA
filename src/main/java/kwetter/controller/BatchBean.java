package kwetter.controller;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Niek on 16-3-14.
 */
@Named
@SessionScoped
public class BatchBean implements Serializable {

    private JobOperator jobOperator;
    private long execID = -1;

    public void startBatchJob(){
        System.out.println("Starting batch import");

        jobOperator = BatchRuntime.getJobOperator();
        execID = jobOperator.start("tweetBatch", null);
    }

    /* Show the current status of the job */
    public String getJobStatus() {
        if (execID == -1) {
            return "Not started";
        }

        return jobOperator.getJobExecution(execID).getBatchStatus().toString();
    }

    public boolean isCompleted() {
        return (getJobStatus().compareTo("COMPLETED") == 0);
    }

}
