/*****
 License
 --------------
 Copyright © 2017 Bill & Melinda Gates Foundation
 The Mojaloop files are made available by the Bill & Melinda Gates Foundation under the Apache License, Version 2.0 (the "License") and you may not use these files except in compliance with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, the Mojaloop files are distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 Contributors
 --------------
 This is the official list of the Mojaloop project contributors for this file.
 Names of the original copyright holders (individuals or organizations)
 should be listed with a '*' in the first column. People who have
 contributed from an organization can be listed under the organization
 that actually holds the copyright for their contributions (see the
 Gates Foundation organization for an example). Those individuals should have
 their names indented and be marked with a '-'. Email address can be added
 optionally within square brackets <email>.
 * Gates Foundation
 - Name Surname <name.surname@gatesfoundation.com>

 * Jason Bruwer <jason.bruwer@coil.com>

 --------------
 ******/
'use strict'

const ErrorHandler = require('@mojaloop/central-services-error-handling')
const Logger = require('@mojaloop/central-services-logger')
const createClient = require('tigerbeetle-node').createClient
const Config = require('../lib/config')

let tbCachedClient

const getTBClient = async () => {
  try {
    if (!Config.TIGERBEETLE.enabled) return null;

    if (tbCachedClient == null) {
      Logger.info('TB-Client-Enabled. Connecting to R-01 '+Config.TIGERBEETLE.replicaPort01)

      tbCachedClient = await createClient({
        cluster_id: Config.TIGERBEETLE.cluster,
        replica_addresses:
          [
            Config.TIGERBEETLE.replicaPort01,
            Config.TIGERBEETLE.replicaPort02,
            Config.TIGERBEETLE.replicaPort03
          ]
      })
    }
    return tbCachedClient;
  } catch (err) {
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const tbCreateAccount = async (id) => {
  try {
    const client = await getTBClient()
    Logger.info('TB-Client '+client)
    if (client == null) return {}

    //Participant A
    const account = {
      id: id, // u128 (137n)
      user_data: 0n, // u128, opaque third-party identifier to link this account (many-to-one) to an external entity:
      reserved: Buffer.alloc(48, 0), // [48]u8
      unit: 1,   // u16, unit of value
      code: 718, // u16, a chart of accounts code describing the type of account (e.g. clearing, settlement)
      flags: 0,  // u32
      debits_reserved: 0n,  // u64
      debits_accepted: 0n,  // u64
      credits_reserved: 0n, // u64
      credits_accepted: 0n, // u64
      timestamp: 0n, // u64, Reserved: This will be set by the server.
    }
    const errors = await client.createAccounts([account])
    Logger.error('CreateAccErrors '+errors)
    return errors
  } catch (err) {
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

module.exports = {
  tbCreateAccount
}

