// Rushy Panchal
// BackgroundQueueProcessor.java

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
* The {@link BackgroundQueueProcessor} processes data in the background,
* by running a timed thread. Each {@link BackgroundQueueProcessor} is limited
* to using a single data type for the purposes of managing the queue.
* @author Rushy Panchal
* @version 0.1.0
*/
public abstract class BackgroundQueueProcessor<Data, Output> extends ConcurrentLinkedQueue<Data> implements Queue<Data> {
	private final long interval; // interval to run the timed task
	private final ConcurrentLinkedQueue<Output> output; // queue for output
	private Timer scheduler; // scheduler for processing data with timed task

	/**
	* Creates a new {@link BackgroundQueueProcessor} with a given amount
	* of time to process all of the data on the queue.
	* @param interval time process data in (seconds). An interval of 5s,
	* for example, processes all of the data available every 5 seconds.
	*/
	public BackgroundQueueProcessor(double interval) {
		super();

		this.output = new ConcurrentLinkedQueue<Output>();

		this.interval = (long) (interval * 1000);
		this.scheduler = null;
		}

	/**
	* Creates a new {@link BackgroundQueueProcessor} with the default interval
	* of 5 seconds.
	* @see #BackgroundQueueProcessor(double)
	*/
	public BackgroundQueueProcessor() {
		this(5);
		}

	/**
	* Starts the {@link BackgroundQueueProcessor} by enabling the processing
	* to occur. Once started, it is important to call {@link #stop(boolean)},
	* {@link #stop()}, or {@link #abort()} before exiting your program.
	* Otherwise, this thread will not exit.
	*/
	public void start() {
		// schedule the recurring task to process data
		this.scheduler = new Timer();
		this.scheduler.schedule(new TimerTask() {
			@Override
			public void run() {
				while (! isEmpty()) {
					boolean success = process();
					if (! success) break; // no more data left
					}
				}
			}, 0, this.interval);
		}

	/**
	* Stops the {@link BackgroundQueueProcessor} and clear the current queue,
	* optionally processing it before clearing it.
	* @param finish whether or not to process the data before clearing the queue
	*/
	public void stop(boolean finish) {
		if (this.scheduler == null) return; // processing not yet started

		if (finish) {
			while (! this.isEmpty()) {
				boolean success = process();
				if (! success) break; // no more data left
				}
			}
		this.clear();
		this.scheduler.cancel();
		this.scheduler = null;
		}

	/**
	* Stops the {@link BackgroundQueueProcessor} and process the current queue
	* before clearing it and cleaning up the threads.
	* Same as calling {@link BackgroundQueueProcessor#stop(boolean)} with true.
	*/
	public void stop() {
		stop(true);
		}

	/**
	* Stops the {@link BackgroundQueueProcessor} and clear the current queue,
	* without processing the data on it.
	* Same as calling {@link BackgroundQueueProcessor#stop(boolean)} with false.
	*/
	public void abort() {
		stop(false);
		}

	/**
	* Gets all of the current output as an iterable of {@link Output}s. This is
	* another {@link ConcurrentLinkedQueue}.
	* Note: not guaranteed to contain all of the output so far because it is only
	* <a href="https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html#iterator--">weakly-consistent</a>.
	* @return iterator over the current output
	*/
	public Iterator<Output> outputStream() {
		return this.output.iterator();
		}

	/**
	* Gets a reference to the {@link ConcurrentLinkedQueue} for output from
	* the queued data.
	* @return {@link ConcurrentLinkedQueue} of output
	*/
	public ConcurrentLinkedQueue<Output> output() {
		return this.output;
		}

	/**
	* Processes a single item from the queue and returns the appropriate
	* output.
	* Note: this should be overridden during instantiation.
	* @param datum the item to process
	* @return output from the processing
	*/
	public abstract Output process(Data datum);

	/**
	* Processes a single item on the queue by popping off of the queue and
	* running the given method on it.
	* @return whether or not the process was successful
	*/
	private boolean process() {
		Data element = this.poll();
		if (element == null) return false;

		Output retval = process(element);
		this.output.add(retval);
		return true;
		}
	}
