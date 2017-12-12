package se.unlogic.standardutils.operation;

import java.util.Calendar;
import java.util.Date;

import se.unlogic.standardutils.threads.ThreadUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.time.TimeUtils;

public class DetailedProgressMeterMonitor extends ProgressMeterMonitor {

	public DetailedProgressMeterMonitor(ProgressMeter progressMeter, String logString, int sleepIntervalInMilliseconds, ProgressLogger monitorOutput) {
		super(progressMeter, logString, sleepIntervalInMilliseconds, monitorOutput);
	}

	@Override
	public void run() {

		long lastRun = System.currentTimeMillis();
		long lastPosition = progressMeter.getCurrentPosition();

		while (!abort) {

			ThreadUtils.sleep(sleepInterval);

			long now = System.currentTimeMillis();

			// assume linear progress to estimate remaining time
			long totalMillis = (long) (progressMeter.getTimeSpent() / (double) progressMeter.getProgress());
			int remainingSeconds = (int) ((totalMillis - progressMeter.getTimeSpent()) / 1000L);

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, remainingSeconds);

			String estimatedCompletion = TimeUtils.TIME_SECONDS_FORMATTER.format(new Date(calendar.getTimeInMillis()));

			String digits;

			if (progressMeter.getStart() == progressMeter.getFinish()) {
				digits = "";

			} else {

				digits = Integer.toString((int) Math.ceil(Math.log10(Math.max(progressMeter.getStart(), progressMeter.getFinish()))));
			}

			long speed = (MillisecondTimeUnits.SECOND * (progressMeter.getCurrentPosition() - lastPosition)) / (now - lastRun);

			monitorOutput.logProgress(logMessage + String.format("%2d%% complete. %" + digits + "d/s, completed %" + digits + "d, %" + digits + "d remaining. Estimated completion at %s", progressMeter.getPercentComplete(), speed, progressMeter.getCurrentPosition(), progressMeter.getFinish() - progressMeter.getCurrentPosition(), estimatedCompletion));

			lastPosition = progressMeter.getCurrentPosition();
			lastRun = now;
		}
	}

}
