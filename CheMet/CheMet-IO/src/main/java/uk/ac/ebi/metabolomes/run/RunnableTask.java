/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.run;

import com.explodingpixels.macwidgets.SourceListItem;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;


/**
 * RunnableTask.java
 *
 *
 * @author johnmay
 * @date Apr 28, 2011
 */
public abstract class RunnableTask
implements Runnable {

    private JobParameters parameters;
    private TaskStatus status;


    public RunnableTask(JobParameters p) {
        this.status = TaskStatus.QUEUED;
        this.parameters = p;
    }


    /**
     * Accessor to the job parameters
     * @return
     */
    public JobParameters getJobParameters() {
        return parameters;
    }


    public void addParameter(String parameter,
                             Object value) {
        parameters.put(parameter, value);
    }


    public abstract void prerun();


    @Override
    public abstract void run();


    public abstract void postrun();


    public TaskStatus getStatus() {
        return this.status;
    }


    public boolean isCompleted() {
        return getStatus() == TaskStatus.COMPLETED;
    }


    public void setErrorStatus() {
        this.status = TaskStatus.ERROR;
    }


    public void setCompletedStatus() {
        this.status = TaskStatus.COMPLETED;
    }


    public void setRunningStatus() {
        this.status = TaskStatus.RUNNING;
    }


    /**
     * Accessor for a runnable thread object
     * @return A thread of this runnable object
     */
    public Thread getRunnableThread() {
        return new Thread(this);
    }


    public abstract String getTaskDescription();


    public abstract String getTaskCommand();


    @Override
    public String toString() {
        return getTaskDescription() + ": " + getStatus();
    }


}

