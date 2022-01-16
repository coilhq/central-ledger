package io.mojaloop.centralledger.jmeter.rest.client.json;

import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 *
 * @see JSONObject
 */
public abstract class ABaseJSONObject implements Serializable {

	public static final long serialVersionUID = 1L;

	@Getter
	protected JSONObject JSONObject;

	/**
	 * The JSON mapping for the {@code ABaseFluidJSONObject} object.
	 */
	public static class JSONMapping {

	}

	/**
	 * Default constructor.
	 */
	public ABaseJSONObject() {
		super();
	}

	/**
	 * Populates local variables Id and Service Ticket with {@code jsonObjectParam}.
	 *
	 * @param jsonObjectParam The JSON Object.
	 */
	public ABaseJSONObject(JSONObject jsonObjectParam) {
		this();

		this.JSONObject = jsonObjectParam;
		if (this.JSONObject == null) return;
	}

	public JSONObject toJsonObject() throws JSONException {
		JSONObject returnVal = new JSONObject();

		return returnVal;
	}

	/**
	 * Return the Text representation of {@code this} object.
	 *
	 * @return JSON body of {@code this} object.
	 */
	@Override
	public String toString() {
		JSONObject jsonObject = this.toJsonObject();
		return (jsonObject == null) ? null : jsonObject.toString();
	}
}
