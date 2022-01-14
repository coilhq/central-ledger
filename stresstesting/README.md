# Stress Testing for Central Ledger using JMeter

## Install

The `AbstractJavaSamplerClient` for Central Ledger needs to be added to the jMeter.

## Configure



## Run

In order to successfully run the stress test, the jMeter profile needs to be configured for your 
local environment. Please follow the steps below;
1. Update environment variables in `scripts/start_jMeter.sh`
2. We are now ready to configure jMeter, run the following command;
```shell
scripts/start_jMeter.sh
```

If you wish to execute jMeter in console mode only, execute the following command:
```shell
scripts/start_jMeter.sh console
```
The execution results may be found at `jMeterOut`.


The `` may be executed in order to initiate the stress test. 
