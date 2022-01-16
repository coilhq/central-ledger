package io.mojaloop.centralledger.jmeter;

import io.mojaloop.centralledger.jmeter.rest.client.DFSPClient;
import io.mojaloop.centralledger.jmeter.runner.SamplerRunner;
import io.mojaloop.centralledger.jmeter.rest.client.json.testdata.TestDataCarrier;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class StressTestMappingSampler extends AbstractJavaSamplerClient {
	/**
	 * Arguments accepted by the sampler.
	 */
	private static class Arg {
		private static final String _0_INPUT_FILE = "inputFile";
		private static final String _1_URL = "url";
	}

	private Logger logger = this.getNewLogger();

	private String inputFile = null;
	private String url = "http://localhost";

	private DFSPClient dfspClient = null;

	private int counter;
	private int commandCount;

	private List<TestDataCarrier> allTestData;

	private Map<String, Object> validPrepare = new ConcurrentHashMap<>();

	@Override
	public void setupTest(JavaSamplerContext contextParam) {
		super.setupTest(contextParam);
		this.logger.info("Initiating test data.");
		this.counter = 0;

		//Set Params...
		this.inputFile = contextParam.getParameter(Arg._0_INPUT_FILE);
		File file = null;
		try (FileInputStream fis = new FileInputStream(file)) {
			this.allTestData = null;
			this.commandCount = this.allTestData.size();
			this.logger.info(
					String.format("Test file '%s' read a total of '%d' test scenarios.",
							this.inputFile, this.commandCount));
		} catch (IOException eParam) {
			this.logger.error("Unable to load test data. "+eParam.getMessage(), eParam);
			return;
		}

		this.url = contextParam.getParameter(Arg._1_URL, this.url);
		this.dfspClient = new DFSPClient(this.url);

		//Populate the form containers...
		this.logger.info("Initiation of test data for [{}] COMPLETE.", this.url);
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument(Arg._0_INPUT_FILE, System.getProperty("user.home"));
		defaultParameters.addArgument(Arg._1_URL,this.url);
		return defaultParameters;
	}

	@Override
	public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
		SampleResult returnVal = new SampleResult();
		TestDataCarrier testData = this.allTestData.get(this.counter);
		returnVal.setSentBytes(testData.toString().getBytes().length);

		String testDataType = "";

		try {
			returnVal.setSampleLabel(String.format("[%s]:[%s]",
				this.url,
				testDataType
			));
			returnVal.setURL(new URL(this.url));
			returnVal.setDataType(SampleResult.TEXT);
			returnVal.setContentType("application/json");

			//The execution utility...
			SamplerRunner sr = new SamplerRunner(this.logger, this.dfspClient);

			Integer testDataIndex = Integer.valueOf(this.counter + 2);

			sr.execute(testData, returnVal, testDataIndex);
		} catch (Exception exception) {
			this.logger.error("Request was not successfully processed. "+ exception.getMessage(),exception);
			returnVal.setResponseMessage("ERROR-EXCEPTION ("+
					"testData.getTestDataType()"+","+
					"testData.getTestUser().getUsername()"+"): "+ exception.getMessage());
			returnVal.setSuccessful(Boolean.FALSE);
			returnVal.setResponseCode("500");
		} finally {
			this.counter++;
			if (this.counter >= this.commandCount) {
				this.counter = 0;
			}
		}

		return returnVal;
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		super.teardownTest(context);

		SamplerRunner.clearQueues();
	}
}
