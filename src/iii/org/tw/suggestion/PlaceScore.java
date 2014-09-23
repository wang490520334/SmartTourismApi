package iii.org.tw.suggestion;

public class PlaceScore implements Comparable {
	public int index;
	public float totalscore;

	@Override
	public int compareTo(Object arg0) {
		if (this.totalscore < ((PlaceScore) arg0).totalscore)
			return 1;
		else if (this.totalscore > ((PlaceScore) arg0).totalscore)
			return -1;
		else
			return 0;
	}
}