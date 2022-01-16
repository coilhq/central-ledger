package io.mojaloop.centralledger.jmeter.rest.client.json.participant;

import io.mojaloop.centralledger.jmeter.rest.client.json.ABaseJSONObject;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 */
@Getter
@Setter
public class Participant extends ABaseJSONObject {
	public static final long serialVersionUID = 1L;

	private String name;
	private String currency;
	private String password;
	private String id;
	private String created;
	private int isActive;
	private List<String> links;
	private List<Account> accounts;

	public static class JSONMapping {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String CURRENCY = "currency";
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
	public Participant(JSONObject jsonObject) {
		super(jsonObject);

		if (jsonObject.has(JSONMapping.ID)) this.setId(jsonObject.getString(JSONMapping.ID));
		if (jsonObject.has(JSONMapping.NAME)) this.setId(jsonObject.getString(JSONMapping.NAME));
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
