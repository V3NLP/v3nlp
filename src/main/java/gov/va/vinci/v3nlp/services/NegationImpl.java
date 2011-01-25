package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.AnnotationInterface;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.v3nlp.Span;
import gov.va.vinci.v3nlp.model.operations.Negation;
import gov.va.vinci.v3nlp.negex.GenNegEx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import opennlp.maxent.MaxentModel;
import opennlp.maxent.io.BinaryGISModelReader;
import opennlp.tools.sentdetect.SentenceDetectorME;

import org.springframework.core.io.Resource;

public class NegationImpl {
	@Setter
	@Getter
	Resource sentenceRulesFile;

	@Setter
	@Getter
	Resource negationRulesFile;

	SentenceDetectorME sentenceDetector;
	String defaultNegationRules = "";

	GenNegEx g = new GenNegEx();

	public void init() {
		MaxentModel model = null;
		try {
			model = new BinaryGISModelReader(sentenceRulesFile.getFile())
					.getModel();
			sentenceDetector = new SentenceDetectorME(model);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@SneakyThrows
	public ArrayList<String> getDefaultNegationConfiguration() {
		ArrayList<String> toReturn = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				negationRulesFile.getInputStream()));
		String str = null;
		while ((str = in.readLine()) != null) {
			toReturn.add(str);
		}
		return toReturn;
	}

	public Corpus process(Corpus c, Negation neg) {
		ArrayList negationRules = new ArrayList();
		for (String s : neg.getConfiguration().split("\r")) {
			if (s.startsWith("##") || s.trim().length() == 0) {
				continue;
			}
			negationRules.add(s); // Convert to ArrayList.
		}
		for (DocumentInterface d : c.getDocuments()) {
			// No Annotations, skip
			if (d.getAnnotations().getAll().size() < 1) {
				continue;
			}

			// Step 1 tokenize to find sentences.
			Span[] spans = convertToSpans(sentenceDetector.sentPosDetect(d
					.getContent()), d.getContent());

			// Step 2 iterate through annotations
			for (AnnotationInterface ann : d.getAnnotations().getAll()) {
				// Step 3 find sentence for this annotation.
				String sentence = null;
				for (Span s : spans) {
					if (ann.getBeginOffset() >= s.getStart()
							&& ann.getBeginOffset() <= s.getEnd()) {
						// Found the sentence this annotation starts in.
						sentence = d.getContent().substring(s.getStart(),
								s.getEnd());
					}
				}

				try {
					// Step 4 run negation algorithm on sentence / annotation
					String result = g.negCheck(sentence, ann.getContent(),
							negationRules, false);
					if (result.trim().endsWith("negated")) {
						Feature f = new Feature("type", "Negated");
						f.getMetaData().setCreatedDate(new Date());
						f.getMetaData().setPedigree("Negation");
						((Annotation)ann).getFeatures().add(f);
						
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return c;
	}

	/**
	 * Open NLP 1.1 only returns sentence endings. This code is from OpenNLP 1.5
	 * that converts those into more useful spans for each sentence.
	 * 
	 * @param starts
	 *            integer array of sentence starts
	 * @param s
	 *            the text that the sentence starts belong to.
	 * @return an array of spans for each sentence.
	 */
	public Span[] convertToSpans(int[] starts, String s) {
		boolean leftover = starts[starts.length - 1] != s.length();
		Span[] spans = new Span[leftover ? starts.length + 1 : starts.length];
		for (int si = 0; si < starts.length; si++) {
			int start, end;
			if (si == 0) {
				start = 0;

				while (si < starts.length
						&& Character.isWhitespace(s.charAt(start)))
					start++;
			} else {
				start = starts[si - 1];
			}
			end = starts[si];
			while (end > 0 && Character.isWhitespace(s.charAt(end - 1))) {
				end--;
			}
			spans[si] = new Span(start, end);
		}
		if (leftover) {
			spans[spans.length - 1] = new Span(starts[starts.length - 1], s
					.length());
		}
		return spans;
	}
}
