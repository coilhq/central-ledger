package io.mojaloop.centralledger.jmeter.rest.client.json.participant;

import io.mojaloop.centralledger.jmeter.rest.client.json.ABaseJSONObject;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 */
@Getter
@Setter
public class Account extends ABaseJSONObject {
	public static final long serialVersionUID = 1L;

	private Long id;
	private String ledgerAccountType;
	private String currency;
	private int isActive;
	private String createdDate;
	private String createdBy;

	public static class JSONMapping {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String PASSWORD = "password";
		public static final String BALANCE = "balance";
		public static final String IS_DISABLED = "is_disabled";
		public static final String LEDGER = "ledger";
		public static final String CREATED = "created";
	}

	/**
	 * Populates local variables with {@code jsonObjectParam}.
	 *
	 * @param jsonObject The JSON Object.
	 */
	public Account(JSONObject jsonObject) {
		super(jsonObject);

		if (jsonObject.has(JSONMapping.ID)) this.setId(jsonObject.getLong(JSONMapping.ID));
	}

	/**
	 *
	 * @return
	 * @throws JSONException
	 */
	@Override
	public JSONObject toJsonObject() throws JSONException {
		JSONObject returnVal = super.toJsonObject();

		//ID...
		if (this.getId() == null) returnVal.put(JSONMapping.ID, JSONObject.NULL);
		else returnVal.put(JSONMapping.ID, this.getId());

		return returnVal;
	}
}
