package io.mojaloop.centralledger.jmeter.rest.client;

import io.mojaloop.centralledger.jmeter.rest.client.json.participant.Accounts;
import io.mojaloop.centralledger.jmeter.rest.client.json.participant.Participant;
import io.mojaloop.centralledger.jmeter.rest.client.json.transfer.Transfer;
/**
 *
 */
public class DFSPClient extends ABaseRESTClient {

	public DFSPClient(String endpointBaseUrl) {
		super(endpointBaseUrl);
	}

	/**
	 *
	 */
	public Accounts getHubAccounts(String participantName) {
		return new Accounts(this.getJson(
				String.format("/participants/%s/accounts", participantName)));
	}

	/**
	 * @see Participant
	 */
	public Participant createParticipant(Participant toCreate) {
		return new Participant(this.postJson(toCreate, "/participants"));
	}

	/**
	 * @see Participant
	 */
	public Participant getParticipant(String name) {
		return new Participant(this.getJson(String.format("/participants/%s", name)));
	}

	public Transfer jMeterTransferPrepare(Transfer transfer) {
		return new Transfer(this.postJson(transfer, "/jmeter/transfers/prepare"));
	}
}
