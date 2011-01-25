package gov.va.vinci.v3nlp;

import lombok.Data;

@Data
public class Span {

	private int start;
	private int end;
	
	public Span(int s, int e) {
		this.start = s;
		this.end = e;
	}
}
