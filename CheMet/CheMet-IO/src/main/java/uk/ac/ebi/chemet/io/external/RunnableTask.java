/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.io.external;

import java.util.List;
import uk.ac.ebi.core.AbstractAnnotatedEntity;
import uk.ac.ebi.interfaces.Identifier;
import uk.ac.ebi.interfaces.TaskOptions;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;
import uk.ac.ebi.metabolomes.run.TaskStatus;

/**
 * RunnableTask.java
 *
 *
 * @author johnmay
 * @date Apr 28, 2011
 */
public abstract class RunnableTask
        extends AbstractAnnotatedEntity
        implements Runnable {

    private JobParameters parameters;
    private TaskOptions options;
    private TaskStatus status = TaskStatus.QUEUED;
    public static final String BASE_TYPE = "Task";

    public RunnableTask(TaskOptions options, Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
        this.parameters = parameters;
        this.status = status;
    }

    
    public RunnableTask(TaskOptions options){
        super(options.getIdentifier(), options.getName(), options.getDescription());
        this.options = options;
    }


    public TaskOptions getOptions(){
        return options;
    }

    /**
     * Use other constructor
     * @param p
     * @deprecated
     */
    @Deprecated
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

    public String getTaskDescription() {
        return "";
    }

    public String getTaskCommand() {
        return "";
    }

    @Override
    public String toString() {
        return getTaskDescription() + ": " + getStatus();
    }

    @Override
    public String getBaseType() {
        return BASE_TYPE;
    }
}
