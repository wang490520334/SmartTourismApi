package iii.org.tw.suggestion;

public class EventData implements Comparable {
	public long StartTime;
	public long EndTime;
	public double X = 121.561545;
	public double Y = 25.033263;

	@Override
	public int compareTo(Object o) {
		if (this.StartTime > ((EventData) o).StartTime)
			return 1;
		else if (this.StartTime < ((EventData) o).StartTime)
			return -1;
		else
			return 0;
	}
}