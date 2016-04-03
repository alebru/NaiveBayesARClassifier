package textclassifier.abrusmark;

import java.util.ArrayList;

public class Goldstandard {
	ArrayList<BugReport> ClassedReports;
	String bug_stringified;
	
	public Goldstandard() {
		ClassedReports = new ArrayList<BugReport>();
	}
	
	
	@Override
	public String toString() {
		for (BugReport bugReport : ClassedReports) {
			bug_stringified += bugReport.toString();
		}
		return bug_stringified;
	}
	
	public void AddReport(BugReport bug) {
		try {
			ClassedReports.add(bug);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}