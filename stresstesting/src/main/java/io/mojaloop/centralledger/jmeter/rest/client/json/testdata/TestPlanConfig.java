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
	private int transferLookups;
	private int accountLookups;
	private boolean transferSingleHttpRequest;
	private String transfersCurrency;

	public static class JSONMapping {
		public static final String MAX_ACCOUNTS = "max-accounts";
		public static final String TRANSFERS = "transfers";
		public static final String REJECTIONS = "rejections";
		public static final String TRANSFERS_SINGLE_HTTP_REQUEST = "transfers-single-http-request";
		public static final String TRANSFERS_CURRENCY = "transfers-currency";
		public static final String TRANSFER_LOOKUPS = "transfer-lookups";
		public static final String ACCOUNT_LOOKUPS = "account-lookups";
	}

	/**
	 * Populates local variables with {@code jsonObject}.
	 *
	 * @param jsonObject The JSON Object.
	 */
	public TestPlanConfig(JSONObject jsonObject) {
		super(jsonObject);

		if (jsonObject.has(JSONMapping.MAX_ACCOUNTS)) this.setMaxAccounts(jsonObject.getInt(JSONMapping.MAX_ACCOUNTS));
		if (jsonObject.has(JSONMapping.TRANSFERS_CURRENCY)) this.setTransfersCurrency(jsonObject.getString(JSONMapping.TRANSFERS_CURRENCY));
	}

	@Override
	public JSONObject toJsonObject() throws JSONException {
		JSONObject returnVal = super.toJsonObject();

		returnVal.put(JSONMapping.MAX_ACCOUNTS, this.getMaxAccounts());
		returnVal.put(JSONMapping.TRANSFERS_CURRENCY, this.getTransfersCurrency());
		returnVal.put(JSONMapping.TRANSFER_LOOKUPS, this.getTransferLookups());
		returnVal.put(JSONMapping.ACCOUNT_LOOKUPS, this.getAccountLookups());

		return returnVal;
	}
}
