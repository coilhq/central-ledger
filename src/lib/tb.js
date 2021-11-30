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
const util = require('util')

let tbCachedClient

const getTBClient = async () => {
  try {
    if (!Config.TIGERBEETLE.enabled) return null;

    if (tbCachedClient == null) {
      Logger.info('TB-Client-Enabled. Connecting to R-01 '+Config.TIGERBEETLE.replicaEndpoint01)
      Logger.info('TB-Client-Enabled. Connecting to R-02 '+Config.TIGERBEETLE.replicaEndpoint02)
      Logger.info('TB-Client-Enabled. Connecting to R-03 '+Config.TIGERBEETLE.replicaEndpoint03)

      tbCachedClient = await createClient({
        cluster_id: Config.TIGERBEETLE.cluster,
        replica_addresses:
          [
            Config.TIGERBEETLE.replicaEndpoint01,
            Config.TIGERBEETLE.replicaEndpoint02,
            Config.TIGERBEETLE.replicaEndpoint03
          ]
      })
    }
    return tbCachedClient;
  } catch (err) {
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const tbCreateAccount = async (id) => {//TODO add currency and account type...
  try {
    const client = await getTBClient()
    Logger.info('TB-Client '+client)
    if (client == null) return {}

    Logger.info('Storing Account '+id)

    const userData = BigInt(id)
    const currency = 840;//USD
    const accountType = 718;
    const tbId = userData//TODO needs to be (user_data+currency+accountType)

    //Participant A
    const account = {
      id: tbId, // u128 (137n)
      user_data: userData, // u128, opaque third-party identifier to link this account (many-to-one) to an external entity:
      reserved: Buffer.alloc(48, 0), // [48]u8
      unit: currency,   // u16, unit of value
      code: accountType, // u16, a chart of accounts code describing the type of account (e.g. clearing, settlement)
      flags: 0,  // u32
      debits_reserved: 0n,  // u64
      debits_accepted: 0n,  // u64
      credits_reserved: 0n, // u64
      credits_accepted: 0n, // u64
      timestamp: 0n, // u64, Reserved: This will be set by the server.
    }
    const errors = await client.createAccounts([account])
    if (errors.length > 0) {
      for (let i = 0; i < errors.length; i++) {
        Logger.error('CreateAccErrors -> '+errors[i].code)
      }
      const fspiopError = ErrorHandler.Factory.createFSPIOPError(ErrorHandler.Enums.FSPIOPErrorCodes.MODIFIED_REQUEST,
        'TB-Account entry failed for '+id+ ' : '+ util.inspect(errors));
      throw fspiopError
    }
    //Logger.error('AccCreate-> NoErrors: See! '+util.inspect(errors))
    return errors
  } catch (err) {
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const tbLookupAccount = async (id) => {
  try {
    const client = await getTBClient()
    if (client == null) return {}

    Logger.info('Fetching Account '+id)

    const accounts = await client.lookupAccounts(BigInt(id))
    Logger.error('AccLookup: '+util.inspect(accounts))
    if (accounts.length > 0) return accounts[0]
    return {}
  } catch (err) {
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const tbPrepareTransfer = async (
  transferRecord,
  payerTransferParticipantRecord,
  payeeTransferParticipantRecord,
  participants,
  participantCurrencyIds
) => {
  try {
    const client = await getTBClient()
    if (client == null) return {}

    Logger.info('1.1 Creating Transfer    '+util.inspect(transferRecord))
    Logger.info('1.2 Creating Payer       '+util.inspect(payerTransferParticipantRecord))
    Logger.info('1.3 Creating Payee       '+util.inspect(payeeTransferParticipantRecord))
    Logger.info('1.4 Participants         '+util.inspect(participants))
    Logger.info('1.5 Participant Currency '+util.inspect(participantCurrencyIds))

    Logger.info('(tbPrepareTransfer) Making use of id '+uuidToBigInt(transferRecord.transferId))

    //const amountCool = BigInt(transfer.amount)
    //Logger.info('Cool '+amountCool)

    //TODO we need to find all participants by user_data

    const transfer = {
      id: uuidToBigInt(transferRecord.transferId), // u128
      debit_account_id: locateAccountFrom(payerTransferParticipantRecord, participants),  // u128
      credit_account_id: locateAccountFrom(payeeTransferParticipantRecord, participants), // u128
      user_data: 0n, // u128, opaque third-party identifier to link this transfer (many-to-one) to an external entity
      reserved: Buffer.alloc(32, 0), // two-phase condition can go in here
      timeout: 0n, // u64, in nano-seconds.
      code: 1,  // u32, a chart of accounts code describing the reason for the transfer (e.g. deposit, settlement)
      flags: 0, // u32
      amount: 100n, // u64
      timestamp: 0n, //u64, Reserved: This will be set by the server.
    }

    const errors = await client.createTransfers([transfer])
    Logger.error('Transfer-Created: '+util.inspect(errors))
    if (errors.length > 0) {
      for (let i = 0; i < errors.length; i++) {
        //TODO Logger.error('CreateTransferErrors -> '+errors[i].code)
      }
    }
    return errors
  } catch (err) {
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const tbFulfilTransfer = async (transferRecord) => {
  try {
    const client = await getTBClient()
    if (client == null) return {}

    //Logger.info('Creating Transfer '+util.inspect(transferRecord))
    //Logger.info('Making use of id '+uuidToBigInt(transferRecord.transferId))

    const transfer = {
      id: uuidToBigInt(transferRecord.transferId), // u128
      debit_account_id: 1n,  // u128
      credit_account_id: 2n, // u128
      user_data: 0n, // u128, opaque third-party identifier to link this transfer (many-to-one) to an external entity
      reserved: Buffer.alloc(32, 0), // two-phase condition can go in here
      timeout: 0n, // u64, in nano-seconds.
      code: 1,  // u32, a chart of accounts code describing the reason for the transfer (e.g. deposit, settlement)
      flags: 0, // u32
      amount: BigInt(transferRecord.amount), // u64
      timestamp: 0n, //u64, Reserved: This will be set by the server.
    }

    const errors = await client.createTransfers([transfer])
    Logger.error('Transfer-Created: '+util.inspect(errors))
    if (errors.length > 0) {
      for (let i = 0; i < errors.length; i++) {
        //TODO Logger.error('CreateTransferErrors -> '+errors[i].code)
      }
    }
    return errors
  } catch (err) {
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const tbDestroy = async () => {
  try {
    const client = await getTBClient()
    if (client == null) return {}
    Logger.info('Destroying TB client')
    client.destroy()
    tbCachedClient = undefined
  } catch (err) {
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const locateAccountFrom = (
  account,
  participants
) => {
  const partCurrencyId = account.participantCurrencyId;
  for (let i = 0; i < participants.length; i++) {
    const itemAtIndex = participants[i]
    if (itemAtIndex.participantCurrencyId == partCurrencyId) {
      Logger.info('Found Account! LocateAccount: '+util.inspect(itemAtIndex))
      return BigInt(itemAtIndex.participantId)
    }
  }

  const fspiopError = ErrorHandler.Factory.createFSPIOPError(ErrorHandler.Enums.FSPIOPErrorCodes.MODIFIED_REQUEST,
    'TB-Participant-NotFound '+partCurrencyId+ ' : '+ util.inspect(account) + ' : ' + util.inspect(participants));
  throw fspiopError
}

const uuidToBigInt = (uuid) => {
  //const buffer = Buffer.from(uuidNoDashes, 'hex')
  //Logger.info('UUID-Buff-1: ' + BigInt("0x"+uuid.replace(/-/g, '')))
  //Logger.info('UUID-Buff-2: ' + buffer.readBigInt64BE())
  //Logger.info('UUID-Buff: ' + BigInt(buffer))

  //const uuidBin = hex2bin(uuidNoDashes)
  //Logger.info('UUID: '+uuidBin)
  return BigInt("0x" + uuid.replace(/-/g, ''))
}

module.exports = {
  tbCreateAccount,
  tbLookupAccount,
  tbPrepareTransfer,
  tbFulfilTransfer,
  tbDestroy
}

