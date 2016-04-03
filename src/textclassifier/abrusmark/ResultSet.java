package textclassifier.abrusmark;

import java.util.ArrayList;

public class ResultSet {
	ArrayList<BugReport> ClassedReports = new ArrayList<BugReport>();
	String bug_stringified;
	
	public ResultSet() {
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

