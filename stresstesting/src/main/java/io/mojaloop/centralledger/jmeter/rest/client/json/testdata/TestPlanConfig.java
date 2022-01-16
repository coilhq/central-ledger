package io.mojaloop.centralledger.jmeter.rest.client.json.testdata;

import io.mojaloop.centralledger.jmeter.rest.client.json.ABaseJSONObject;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 */
@Getter
@Setter
public class TestPlanConfig extends ABaseJSONObject {
	public static final long serialVersionUID = 1L;

	private int maxAccounts;
	private int transfers;
	private int rejections;
	private boolean transferSingleHttpRequest;

	public static class JSONMapping {
		public static final String MAX_ACCOUNTS = "max-accounts";
		public static final String transfers = "transfers";
		public static final String rejections = "rejections";
		public static final String transfers_single_http_request = "transfers-single-http-request";
		public static final String TRANSFERS_CURRENCY = "transfers-currency";
	}

	/**
	 * Populates local variables with {@code jsonObject}.
	 *
	 * @param jsonObject The JSON Object.
	 */
	public TestPlanConfig(JSONObject jsonObject) {
		super(jsonObject);

		if (jsonObject.has(JSONMapping.MAX_ACCOUNTS)) this.setMaxAccounts(jsonObject.getInt(JSONMapping.MAX_ACCOUNTS));
	}

	@Override
	public JSONObject toJsonObject() throws JSONException {
		JSONObject returnVal = super.toJsonObject();

		returnVal.put(JSONMapping.MAX_ACCOUNTS, this.getMaxAccounts());

		return returnVal;
	}
}
