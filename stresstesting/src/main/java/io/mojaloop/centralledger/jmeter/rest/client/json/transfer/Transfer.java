package io.mojaloop.centralledger.jmeter.rest.client.json.transfer;

import io.mojaloop.centralledger.jmeter.rest.client.json.ABaseJSONObject;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 */
@Getter
@Setter
public class Transfer extends ABaseJSONObject {
	public static final long serialVersionUID = 1L;

	private String transferId;
	private boolean fulfil;
	private String payerFsp;
	private String payeeFsp;
	private Amount amount;
	private String ilpPacket;//AYIBgQAAAAAAAASwNGxldmVsb25lLmRmc3AxLm1lci45T2RTOF81MDdqUUZERmZlakgyOVc4bXFmNEpLMHlGTFGCAUBQU0svMS4wCk5vbmNlOiB1SXlweUYzY3pYSXBFdzVVc05TYWh3CkVuY3J5cHRpb246IG5vbmUKUGF5bWVudC1JZDogMTMyMzZhM2ItOGZhOC00MTYzLTg0NDctNGMzZWQzZGE5OGE3CgpDb250ZW50LUxlbmd0aDogMTM1CkNvbnRlbnQtVHlwZTogYXBwbGljYXRpb24vanNvbgpTZW5kZXItSWRlbnRpZmllcjogOTI4MDYzOTEKCiJ7XCJmZWVcIjowLFwidHJhbnNmZXJDb2RlXCI6XCJpbnZvaWNlXCIsXCJkZWJpdE5hbWVcIjpcImFsaWNlIGNvb3BlclwiLFwiY3JlZGl0TmFtZVwiOlwibWVyIGNoYW50XCIsXCJkZWJpdElkZW50aWZpZXJcIjpcIjkyODA2MzkxXCJ9IgA
	private String condition;//GRzLaTP7DJ9t4P-a_BA0WA9wzzlsugf00-Tn6kESAfM
	private Date expiration;//expiration: new Date((new Date()).getTime() + (24 * 60 * 60 * 1000)), // tomorrow
	private List<Extension> extensionList;

	public static class JSONMapping {
		public static final String TRANSFER_ID = "transferId";
		public static final String FULFIL = "fulfil";
		public static final String PAYER_FSP = "payerFsp";
		public static final String PAYEE_FSP = "payeeFsp";
		public static final String AMOUNT = "amount";
		public static final String ILP_PACKET = "ilpPacket";
		public static final String CONDITION = "condition";
		public static final String EXPIRATION = "expiration";
		public static final String extension_list = "extensionList";
	}

	/**
	 * Populates local variables with {@code jsonObjectParam}.
	 *
	 * @param jsonObject The JSON Object.
	 */
	public Transfer(JSONObject jsonObject) {
		super(jsonObject);

		if (jsonObject.has(JSONMapping.TRANSFER_ID)) this.setTransferId(jsonObject.getString(JSONMapping.TRANSFER_ID));
		if (jsonObject.has(JSONMapping.FULFIL)) this.setFulfil(jsonObject.getBoolean(JSONMapping.FULFIL));
		if (jsonObject.has(JSONMapping.PAYER_FSP)) this.setPayerFsp(jsonObject.getString(JSONMapping.PAYER_FSP));
		if (jsonObject.has(JSONMapping.PAYEE_FSP)) this.setPayeeFsp(jsonObject.getString(JSONMapping.PAYEE_FSP));
	}

	@Override
	public JSONObject toJsonObject() throws JSONException {
		JSONObject returnVal = super.toJsonObject();

		if (this.getTransferId() == null) returnVal.put(JSONMapping.TRANSFER_ID, JSONObject.NULL);
		else returnVal.put(JSONMapping.TRANSFER_ID, this.getTransferId());

		returnVal.put(JSONMapping.FULFIL, this.isFulfil());

		if (this.getTransferId() == null) returnVal.put(JSONMapping.TRANSFER_ID, JSONObject.NULL);
		else returnVal.put(JSONMapping.TRANSFER_ID, this.getTransferId());

		return returnVal;
	}
}
