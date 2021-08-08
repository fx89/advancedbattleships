package com.advancedbattleships.utilityservices;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Dynamically allocates tasks at dynamic intervals
 */
@Service
public class TaskManagerService {

	private Map<String, TimerTask> scheduledTasks = new ConcurrentHashMap<>();

	private Timer timer = new Timer();

	public synchronized void startRecurrentTask(String taskName, int intervalSecs, Runnable runnableJob) {
		TimerTask timerTask = new ScheduledTask(runnableJob);
		scheduledTasks.put(taskName, timerTask);
		timer.scheduleAtFixedRate(timerTask, intervalSecs * 1000, intervalSecs * 1000);
	}

	public synchronized void cancelTask(String taskName) {
		TimerTask task = scheduledTasks.get(taskName);

		if (task != null) {
			task.cancel();
		}
	}

	private static final class ScheduledTask extends TimerTask {
		private Runnable runnableJob;

		public ScheduledTask(Runnable runnableJob) {
			this.runnableJob = runnableJob;
		}

		@Override
		public void run() {
			runnableJob.run();
		}

	}
}
