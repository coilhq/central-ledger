package io.mojaloop.centralledger.jmeter.runner;

import io.mojaloop.centralledger.jmeter.rest.client.json.ABaseJSONObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TestDataCarrier {
	private ABaseJSONObject request;
	private ABaseJSONObject response;
	private ActionType actionType;

	public enum ActionType {
		create_account,
		transfer_prepare,
		transfer_fulfil,
		transfer_reject,
	}
}
