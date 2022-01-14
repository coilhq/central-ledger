/*
 * Koekiebox CONFIDENTIAL
 *
 * [2012] - [2017] Koekiebox (Pty) Ltd
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property
 * of Koekiebox and its suppliers, if any. The intellectual and
 * technical concepts contained herein are proprietary to Koekiebox
 * and its suppliers and may be covered by South African and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly
 * forbidden unless prior written permission is obtained from Koekiebox.
 */

package io.mojaloop.centralledger.jmeter.rest.client.json.account;

import io.mojaloop.centralledger.jmeter.rest.client.ABaseRESTClient;
import io.mojaloop.centralledger.jmeter.rest.client.participant.Account;

/**
 *
 */
public class DFSPClient extends ABaseRESTClient {

	public DFSPClient(String endpointBaseUrl) {
		super(endpointBaseUrl);
	}

	/**
	 * @see Account
	 */
	public Account createAccount(Account toCreate) {
		return new Account(this.postJson(toCreate, "/accounts"));
	}
}
