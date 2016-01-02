package pa.iscde.metrix.internal;

import pa.iscde.metrix.extensibility.DefaultMetrics;

class DefaultMetricsTranscation {
	
	protected String getMetricName(DefaultMetrics m) {
		switch (m) {
		case NUMBER_OF_METHODS:
			return "Number of Methods";
		case NUMBER_OF_CHARACTERS:
			return "Number of Characters";
		case NUMBER_OF_COMMENTS:
			return "Number of Comments";
		case NUMBER_OF_CONSTRUCTORS:
			return "Number of Constructors";
		case NUMBER_OF_FIELDS:
			return "Number of Fields";
		case NUMBER_OF_LINES:
			return "Number of Lines";
		case NUMBER_OF_PACKAGES:
			return "Number of Packages";

		default:
			return null;
		}
	}

}
