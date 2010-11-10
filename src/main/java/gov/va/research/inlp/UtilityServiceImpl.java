package gov.va.research.inlp;

import gov.va.research.v3nlp.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UtilityServiceImpl {
	
	
	public List<LabelValue>  getSemanticGroupDescriptionMap() {

		List<LabelValue> resultList = new ArrayList<LabelValue>();
		Map<String, String> results= Util.getSemanticGroupDescriptionMap();
		for(String key:results.keySet()) {
			resultList.add(new LabelValue(results.get(key), key));
		}
		return resultList;
	}
}
