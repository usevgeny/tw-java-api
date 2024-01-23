package io.task.api.app.utils;

public enum TaskParams {
    PARAM_EXPORT(" export"),
    // Specifies High, Medium, Low and no priority for a task.
    PARAM_PRIORITY("priority:%s"), // H|M|L or priority:

//Specifies the frequency of a recurrence of a task.
    PARAM_RECUR("recur:%s"), // <frequency>

//Specifies the date after which a task can be accomplished.
    PARAM_SCHEDULED("scheduled:%s"), // <ready-date>

//Specifies the expiration date of a task, after which it will be deleted.
    PARAM_UNTIL("until:%s"), // <expiration date of task>

//When  a  task  is given a wait date, it is hidden from most built-in reports, which exclude +WAITING.  When
//compatibilty, such tasks are shown as having status "waiting"), but this will change in a future release.
    PARAM_WAIT("wait:%s"), // <wait-date>
//Declares  this  task  to be dependent on id1 and id2.  This means that the tasks id1 and id2 should be com‐
//comma-separated list of ID numbers, UUID numbers and ID ranges.  When prefixing any element of this list by
//pleted before this task.  Consequently, this task will then show up on the 'blocked' report.  It accepts  a
//'-', the specified tasks are removed from the dependency list.
    PARAM_DEPENDS("depends:%d"), // <id1,id2 ...>
//For report purposes, specifies the date that a task was created.
    PARAM_ENTRY("entry:%s"), // <entry-date>

    PARAM_DESCRIPTION("desciption:%s"), // <entry-date>

    PARAM_PROJECT("project:%s"),
    PARAM_EFFORT("effort:%d"),
    PARAM_SCORE("score:%s"),
    PARAM_CONTEXT("context:%s"),
    // finish date
    PARAM_DUE("due:%s"),
    PARAM_START("start"),
    PARAM_DONE("done"),
    // waits until a specific date before showing in a backlog
    ADD_TASK("add"),
    EDIT_TASK("edit"),
    ALL_TASKS("all"),
    ACTIVE_TASKS("next");

    private final String value;

    public String getValue() {
        return value;
    }

    private TaskParams(String value) {
        this.value = value;
    }

}
