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

 * Coil
 - Jason Bruwer <jason.bruwer@coil.com>
 --------------
 ******/
'use strict'

const Transaction = require('../../domain/transactions')
const Transfer = require('../../domain/transfer')
const ErrorHandler = require('@mojaloop/central-services-error-handling')
const Logger = require('@mojaloop/central-services-logger')
const Enum = require('@mojaloop/central-services-shared').Enum
const TransferEventAction = Enum.Events.Event.Action

const getIlpTransactionById = async function (request) {
  try {
    const entity = await Transaction.getById(request.params.id)
    if (entity && entity.length > 0) {
      return await Transaction.getTransactionObject(entity[0].value)
    }
    throw ErrorHandler.Factory.createFSPIOPError(ErrorHandler.Enums.FSPIOPErrorCodes.ID_NOT_FOUND, 'The requested resource could not be found.')
  } catch (err) {
    console.error(err)
    Logger.isErrorEnabled && Logger.error(err)
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const getTransferById = async function (request) {
  try {
    //http://localhost:3001/jmeter/participants/payeeFsp47384172/transfers/1a097f1f-e6aa-48ec-bcab-0dfce9cd25cf
    const entity = await Transfer.getTransferParticipant(request.params.name, request.params.id)
    if (entity && entity.length > 0) {
      return entity
    }
    throw ErrorHandler.Factory.createFSPIOPError(ErrorHandler.Enums.FSPIOPErrorCodes.ID_NOT_FOUND, 'The requested resource could not be found.')
  } catch (err) {
    console.error(err)
    Logger.isErrorEnabled && Logger.error(err)
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const prepareTransfer = async function (request) {
  try {
    const body = request.payload
    await Transfer.prepare(body, null, true)
    if (body.fulfil) {
      fulfilTransfer(request)
    }
  } catch (err) {
    Logger.isErrorEnabled && Logger.error(err)
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

const fulfilTransfer = async function (request) {
  try {
    const body = request.payload
    var eventAction = TransferEventAction.COMMIT
    if (body.reject) {
      eventAction = TransferEventAction.REJECT
    }
    const transferId = body.transferId;
    await Transfer.handlePayeeResponse(transferId, body, eventAction, null)
  } catch (err) {
    Logger.isErrorEnabled && Logger.error(err)
    throw ErrorHandler.Factory.reformatFSPIOPError(err)
  }
}

module.exports = {
  getIlpTransactionById,
  getTransferById,
  prepareTransfer,
  fulfilTransfer
}
