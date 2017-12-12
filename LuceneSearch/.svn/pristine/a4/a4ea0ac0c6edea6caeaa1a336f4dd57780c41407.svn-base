package se.unlogic.hierarchy.foregroundmodules.lucenesearch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class CallbackThreadPoolExecutor extends ThreadPoolExecutor {

	private final SearchModule searchModule;
	private int executingThreadCount;

	public CallbackThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, SearchModule searchModule) {

		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

		this.searchModule = searchModule;
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {

		synchronized(this){
			executingThreadCount++;
		}
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {

		synchronized(this){
			executingThreadCount--;
		}

		searchModule.checkQueueState(true);
	}


	public int getExecutingThreadCount() {

		return executingThreadCount;
	}
}
