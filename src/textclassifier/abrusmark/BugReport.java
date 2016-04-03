package textclassifier.abrusmark;

public class BugReport{
	int bug_id;
	String bug_summary;
	String bug_developer;
	String bug_product;
	String bug_component;
	
	String bug_stringified;
	
	public BugReport(int id, String summary, String developer, String product, String component) {
		bug_id = id;
		bug_summary = summary;
		bug_developer = developer;
		bug_product = product;
		bug_component = component;
	}
	@Override
	public String toString() {
		bug_stringified = this.bug_product + "," + this.bug_component + ","
	+ this.bug_summary + "," + this.bug_developer + "\n";
		return bug_stringified;
	}
	
	public int getBug_id() {
		return bug_id;
	}

	public void setBug_id(int bug_id) {
		this.bug_id = bug_id;
	}

	public String getBug_summary() {
		return bug_summary;
	}

	public void setBug_summary(String bug_summary) {
		this.bug_summary = bug_summary;
	}

	public String getBug_developer() {
		return bug_developer;
	}

	public void setBug_developer(String bug_developer) {
		this.bug_developer = bug_developer;
	}

	public String getBug_product() {
		return bug_product;
	}

	public void setBug_product(String bug_product) {
		this.bug_product = bug_product;
	}

	public String getBug_component() {
		return bug_component;
	}

	public void setBug_component(String bug_component) {
		this.bug_component = bug_component;
	}
}
