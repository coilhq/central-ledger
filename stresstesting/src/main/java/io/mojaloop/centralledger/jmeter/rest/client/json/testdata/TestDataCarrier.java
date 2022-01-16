package io.mojaloop.centralledger.jmeter.rest.client.json.testdata;

import io.mojaloop.centralledger.jmeter.rest.client.json.ABaseJSONObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

@Data
@Getter
@Setter
public class TestDataCarrier extends ABaseJSONObject {
	private ABaseJSONObject request;
	private ABaseJSONObject response;
	private ActionType actionType;

	public enum ActionType {
		create_participant,
		transfer_prepare,
		transfer_fulfil,
		transfer_reject,
	}


	public static class JSONMapping {
		public static final String REQUEST = "request";
		public static final String RESPONSE = "response";
		public static final String ACTION_TYPE = "actionType";
	}

	/**
	 * Populates local variables with {@code jsonObject}.
	 *
	 * @param jsonObject The JSON Object.
	 */
	public TestDataCarrier(JSONObject jsonObject) {
		super(jsonObject);

		if (jsonObject.has(JSONMapping.ACTION_TYPE)) {
			this.setActionType(ActionType.valueOf(jsonObject.getString(JSONMapping.ACTION_TYPE)));

			switch (this.getActionType()) {

			}
		}
	}

	@Override
	public JSONObject toJsonObject() throws JSONException {
		JSONObject returnVal = super.toJsonObject();

		if (this.getActionType() == null) returnVal.put(JSONMapping.ACTION_TYPE, JSONObject.NULL);
		else returnVal.put(JSONMapping.ACTION_TYPE, this.getActionType().name());


		return returnVal;
	}
}
