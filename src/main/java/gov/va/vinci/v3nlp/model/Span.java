package gov.va.vinci.v3nlp.model;

public class Span {

	private int start;
	private int end;
	
	public Span(int s, int e) {
		this.start = s;
		this.end = e;
	}

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
