/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.io.external;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import uk.ac.ebi.core.AbstractAnnotatedEntity;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Identifier;
import uk.ac.ebi.interfaces.TaskOptions;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;
import uk.ac.ebi.metabolomes.run.TaskStatus;
import uk.ac.ebi.observation.parameters.TaskOption;

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
    private Date time;
    public Set<AnnotatedEntity> entities = new HashSet();

    public RunnableTask(TaskOptions options, Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
        this.parameters = parameters;
        this.status = status;
    }

    public RunnableTask(TaskOptions options) {
        super(options.getIdentifier(), options.getName(), options.getDescription());
        this.options = options;
    }

    public TaskOptions getOptions() {
        return options;
    }

    /**
     * Returns the current elapsed time for task. If the task is completed then the completion time is return
     *
     */
    public Date getElapesedTime() {
        return isFinished() ? time : new Date(System.currentTimeMillis() - options.getInitialisationDate().getTime());
    }

    /**
     * Adds an entity to the task. All added entities will be pushed for update (if required)
     * at task termination
     * @param entity
     * @return
     */
    public boolean add(AnnotatedEntity entity) {
        return entities.add(entity);
    }

    /**
     * Adds a collection  entities to the task. All added entities will be pushed for update (if required)
     * at task termination
     * @param entity
     * @return
     */
    public boolean addAll(Collection<AnnotatedEntity> entities) {
        return entities.addAll(entities);
    }

    /**
     * Returns a set of all entities added to the task
     */
    public Set<AnnotatedEntity> getEntities() {
        return entities;
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
        return this.status == TaskStatus.COMPLETED;
    }

    public boolean isRunning() {
        return this.status == TaskStatus.RUNNING;
    }

    /**
     * Similar to {@see isCompleted()} but will return true if the task finished in error
     */
    public boolean isFinished() {
        return this.status == TaskStatus.COMPLETED || this.status == TaskStatus.ERROR;
    }

    public void setErrorStatus() {
        time = getElapesedTime();
        this.status = TaskStatus.ERROR;
    }

    public void setCompletedStatus() {
        time = getElapesedTime();
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

    /**
     * Returns the command that will be executed on the command line. If no command is provided then
     * an empty string is returned
     */
    public String getCommand() {
        StringBuilder sb = new StringBuilder(250);
        sb.append(getOptions().getProgram().getAbsoluteFile()).append(" ");
        for (TaskOption option : (Collection<TaskOption>) getOptions().getOptionMap().values()) {
            sb.append(option.getFlagValuePair()).append(" ");
        }

        return sb.toString();
    }
}
