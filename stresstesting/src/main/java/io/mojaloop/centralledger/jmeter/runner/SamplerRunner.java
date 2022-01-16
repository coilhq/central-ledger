package io.mojaloop.centralledger.jmeter.runner;

import io.mojaloop.centralledger.jmeter.rest.client.DFSPClient;
import io.mojaloop.centralledger.jmeter.rest.client.json.participant.Participant;
import io.mojaloop.centralledger.jmeter.rest.client.json.testdata.TestDataCarrier;
import lombok.RequiredArgsConstructor;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;

import java.net.HttpURLConnection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Utility class used to run each of the test data types.
 *
 * @author jasonbruwer on 18/04/2020.
 * @since 1.0
 */
@RequiredArgsConstructor
public class SamplerRunner {
	private static final int QUEUE_MAX = 5000000;

	private static Map<String, Queue<Long>> VALID_PREPARES = new Hashtable<>();

	private final Logger logger;
	private final DFSPClient dfspClient;

	public void execute(
		TestDataCarrier testData,
		SampleResult result,
		int testDataIndex
	) {
		String contentToSend = null, responseData = null;
		try {
			switch (testData.getActionType()) {
				case create_participant:
					result.setRequestHeaders(
							this.createHeaderVal(
									"",
									"participant",
									testDataIndex));

					contentToSend = testData.toString();
					result.setSampleLabel(String.format("%s:%s", result.getSampleLabel(), "TODO"));

					result.sampleStart();
					Participant createdParticipant = this.dfspClient.createParticipant((Participant) testData.getRequest());
					//TODO REST request comes here...
					result.sampleEnd();
					testData.setResponse(createdParticipant);

					responseData = testData.toString();
					result.setResponseData(responseData, "UTF-8");

					result.setResponseMessage(String.format("SUCCESS (ID[%d])", 1));
					this.addValidPrepareToQueue("formToCreate.getFormType()", 1L);
				break;
				default:
					throw new IllegalStateException(
							String.format("Test data type '%s' not yet supported.",
									"testData.getTestDataType()"));
			}

			result.setSuccessful(Boolean.TRUE);
			result.setResponseCode(Integer.toString(HttpURLConnection.HTTP_OK));
			result.setResponseCodeOK();
		} catch (Exception except) {
			result.sampleEnd();
			this.logger.error(except.getMessage(), except);
			//testData.setResponseResult(except.getMessage());
			result.setResponseData(except.getMessage(), "UTF-8");
			throw except;
		} finally {
			//if (testData.getResponseResult() == null) testData.setResponseResult("--UNKNOWN--");

			long bodySize = contentToSend == null ? 0L : (long)contentToSend.getBytes().length;

			result.setBodySize(bodySize);
			result.setSamplerData(contentToSend);
		}
	}

	private String createHeaderVal(
			String urlPostfix,
			String user,
			int dataRowIndex
	) {
		return String.format("URL: %s\nUser: %s\nLTS Row Index: %d", urlPostfix, user, dataRowIndex);
	}

	private void addValidPrepareToQueue(String formType, Long transactionId) {
		Queue<Long> queueWithFormType = VALID_PREPARES.getOrDefault(formType, new ConcurrentLinkedQueue<>());
		if (queueWithFormType.size() > QUEUE_MAX) return;

		queueWithFormType.add(transactionId);
		VALID_PREPARES.put(formType, queueWithFormType);
	}

	public static void clearQueues() {
		VALID_PREPARES.clear();
	}
}
