package io.mojaloop.centralledger.jmeter.rest.client.json.participant;

import io.mojaloop.centralledger.jmeter.rest.client.json.ABaseJSONObject;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
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
	private Date created;
	private boolean isActive;
	private List<String> links;
	private List<Account> accounts;

	public static class JSONMapping {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String CURRENCY = "currency";
		public static final String PASSWORD = "password";
		public static final String BALANCE = "balance";
		public static final String IS_ACTIVE = "isActive";
		public static final String LINKS = "links";
		public static final String SELF = "self";
		public static final String ACCOUNTS = "accounts";
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
		if (jsonObject.has(JSONMapping.CREATED)) {
			this.setCreated(this.dateFrom(jsonObject, JSONMapping.CREATED));
		}
		if (jsonObject.has(JSONMapping.IS_ACTIVE)) this.setActive(jsonObject.getInt(JSONMapping.IS_ACTIVE) > 0);

		if (jsonObject.has(JSONMapping.LINKS)) {
			JSONObject links = jsonObject.getJSONObject(JSONMapping.LINKS);
			List<String> listList = new ArrayList<>();
			if (links.has(JSONMapping.SELF)) {
				String selfLink = links.getString(JSONMapping.SELF);
				listList.add(selfLink);
			}
			this.setLinks(listList);
		}

		if (jsonObject.has(JSONMapping.ACCOUNTS)) {
			JSONArray accounts = jsonObject.getJSONArray(JSONMapping.ACCOUNTS);
			List<Account> listList = new ArrayList<>();
			for (int index = 0; index < accounts.length(); index++) {
				listList.add(new Account(accounts.getJSONObject(index)));
			}
			this.setAccounts(listList);
		}
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
