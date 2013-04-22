package glum.task;

public enum TaskState
{
	// Initial state
	Inactive,

	// Default normal state
	Active,

	// Transitional states
	TransAbort,
	TransComplete,
	TransSuspend,

	// Terminal states
	Aborted,
	Completed,
	Suspended
}
