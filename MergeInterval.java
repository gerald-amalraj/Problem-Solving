package com.careercup.intervalPartioning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MergeInterval {

	public static List retrieveCombinations(String[] set, int n) {		
		
		LinkedList<String> combinations = new LinkedList<String>();
		for (int i = 0; i < (1 << n); i++) {
			String str = "";
			for (int j = 0; j < n; j++) {

				if ((i & (1 << j)) > 0)
					str += set[j] + "-";
			}

			if (str.length() > 0)
				str = str.substring(0, str.length() - 1);

			if (str.contains("-")) {
				combinations.add(str);
			}
		}
		return combinations;
	}

	public static List<Interval> retrieveFreeTime(LinkedList<Interval> intervals) {

		if (intervals.size() == 0 || intervals.size() == 1)
			return intervals;
		else {
				intervals.addFirst(new Interval(0, 0));
			if (intervals.getLast().getEnd() != 24)
				intervals.addLast(new Interval(24, 0));
		}

		Interval first = intervals.get(0);
		double start = first.getStart();

		ArrayList<Interval> output = new ArrayList<Interval>();
		for (int i = 1; i < intervals.size(); i++) {
			Interval current = intervals.get(i);

			if (start < current.getStart()) {
				output.add(new Interval(start, current.getStart()));
				start = current.getEnd();
			} else {
				start = current.getEnd();
			}
		}

		return output;
	}

	public static LinkedList<Interval> mergeBusyTime(LinkedList<Interval> intervals) {

		if (intervals.size() == 0 || intervals.size() == 1)
			return intervals;

		Collections.sort(intervals, new IntervalComparator());

		Interval first = intervals.get(0);
		double start = first.getStart();
		double end = first.getEnd();

		LinkedList<Interval> result = new LinkedList<Interval>();

		for (int i = 1; i < intervals.size(); i++) {
			Interval current = intervals.get(i);
			if (current.getStart() < end) {
				end = Math.max(current.getEnd(), end);
			} else {
				result.add(new Interval(start, end));
				start = current.getStart();
				end = current.getEnd();
			}
		}

		result.add(new Interval(start, end));
		return result;
	}

	public static void main(String[] args) throws java.lang.Exception {

		HashMap<String, List<Interval>> inputData = new HashMap<String, List<Interval>>();
		inputData.put("Alice", Arrays.asList(new Interval(13.5, 14), new Interval(15.75, 17)));
		inputData.put("Bob", Arrays.asList(new Interval(9, 12), new Interval(13, 14), new Interval(14, 16)));
		inputData.put("Eve", Arrays.asList(new Interval(9, 11), new Interval(12.5, 13.5), new Interval(14, 15),	new Interval(16, 18)));
		inputData.put("Mallory", Arrays.asList(new Interval(0, 9), new Interval(12, 24)));

		String set[] = { "Alice", "Bob", "Eve", "Mallory" };
		int n = set.length;
		
		for (String getCombinations : (List<String>) retrieveCombinations(set, n)) {
			String[] splitByHyphen = getCombinations.split("-");
			LinkedList<Interval> addIntervals = new LinkedList<Interval>();
			for (String str : splitByHyphen) {
				for (Interval intrval : inputData.get(str)) {
					addIntervals.add(intrval);
				}
			}

			LinkedList<Interval> x = mergeBusyTime(addIntervals);

			System.out.println(getCombinations + " Busy Time=============================");
			for (Interval i : x) {
				System.out.println(i.getStart() + " " + i.getEnd());
			}
			System.out.println(getCombinations + " Free Time=============================");

			List<Interval> opt = retrieveFreeTime(x);

			for (Interval i : opt) {
				System.out.println(i.getStart() + " " + i.getEnd());
			}

			System.out.println("****************************************************");
		}

	}

}

class Interval {
	private double start;
	private double end;

	Interval() {
		start = 0;
		end = 0;
	}

	Interval(double s, double e) {
		start = s;
		end = e;
	}

	public double getStart() {
		return start;
	}

	public double getEnd() {
		return end;
	}
}

class IntervalComparator implements Comparator<Interval> {
	public int compare(Interval i1, Interval i2) {
		return Double.compare(i1.getStart(), i2.getStart());
	}
}
