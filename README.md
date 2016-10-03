# interop-ilp ledger
This project provides an interop API implementation of ILP Ledger Service.

Below is the RAML for reference. However, the responses are generated based on specifications mentioned here: https://github.com/LevelOneProject/Docs/issues/205

This is currently hosted as a service in the URL that looks like this:  http://<awshost:port>/ilp/backend/v1/console/ , the host details have been communicated via e-mails and slack

The following RAML file provides methods for the following:

* Find an account by ID 
* Add an account by ID
* List all accounts
* Find a local transfer by id
* Request a transfer
* Excute a transfer
* Retrieve all accounts of all connectors on a ledger

https://github.com/LevelOneProject/interop-ilp-ledger/blob/master/src/main/api/ilp-ledger-adapter.raml

```
#%RAML 1.0
title: ILP Ledger Adapter
version: v1
baseUri: http://localhost:8081/ilp/ledger/{version}

types:
  TransferState:
    enum: [ proposed, prepared, executed, cancelled ]

  Timeline:
    type: object
    properties:
      proposed_at?: string
      prepared_at?: string
      executed_at?: string

  PutAccountRequest:
    type: object
    properties:
      name: string
      balance: string

  PutAccountResponse:
    type: object
    properties:
      name: string
      balance: string
      id: string
      minimum_allowed_balance: string

  GetAccountsResponse:
    type: array
    items: object

  Funds:
    type: object
    properties:
      account: string
      amount: string
      authorized?: boolean

  TransferRequest:
    type: object
    properties:
      id: string
      ledger: string
      execution_condition: string
      expires_at: string
      debits:
        type: Funds[]
        minItems: 1
      credits:
        type: Funds[]
        minItems: 1

  ProposeTransferRequest:
    type: TransferRequest

  ProposedTransferResponse:
    type: ProposeTransferRequest
    properties:
      state:
        enum: [ proposed ]

  PrepareTransferRequest:
    type: TransferRequest

  PreparedTransferResponse:
    type: PrepareTransferRequest
    properties:
      state:
        enum: [ prepared ]

  ServerMetadataResponse:
    description: Information about the ILP Ledger Adapter
    type: object
    properties:
      currency_code: string
      currency_symbol: string
      precision: integer
      scale: integer
      urls:
        description: (Optional) Paths to other methods exposed by this ledger. Each field name is short name for a method and the value is the path to that method.
        type: object
        required: false

  TransferResponse:
    type: TransferRequest
    properties:
      timeline:
        type: Timeline
        required: false

  TransferResponses:
    type: array
    items: TransferResponse
    minItems: 1

  ConnectorDescriptor:
    type: object
    properties:
      id: string
      name: string
      connector: string

  TransferStateResponse:
    type: object
    properties:
      message:
        type: object
        properties:
          id: string
          state: string
      type: string
      signer: string
      public_key: string
      signature: string

  #Error Definitions
  Error:
    type: object
    properties:
      id: string
      message: string

  ValidationError:
    type: Error
    properties:
      validationErrors:
        type: array
        items: ValidationItem
        minItems: 0

  ValidationItem: #Each item in the array of validation Errors
    type: object
    properties:
      message: string
      code: integer
      dataPath: string
      schemaPath: string
      subErrors: string
      stack: string
      params:
        type: object
        required: false
        properties:
          pattern: string
          key: string

  #400 Errors
  InvalidBodyError:
    description: The submitted JSON entity does not match the required schema.
    type: ValidationError

  InvalidUriParameterError:
    description: At least one provided URI or UUID parameter was invalid.
    type: ValidationError

  #403 Forbidden Errors
  UnauthorizedError:
    description: You do not have permissions to access or modify this resource in the requested way.
    type: Error

  #404 Not Found Errors
  NotFoundError:
    description: The requested resource could not be found.
    type: Error

  #422 Unprocessable Entity Errors
  AlreadyExistsError:
    description: The specified entity already exists and may not be modified.
    type: Error

  InsufficientFundsError:
    description: The source account does not have sufficient funds to satisfy the request.
    type: Error
    properties:
      owner: string

  UnmetConditionError:
    description: The submitted fulfillment does not meet the specified condition.
    type: Error
    properties:
      condition?: string
      fulfillment?: string

  UnprocessableEntityError:
    description: The provided entity is syntactically correct, but there is a generic semantic problem with it.
    type: Error

/:
  displayName: GET /v1
  get:
    description: Receive information about the ILP Ledger Adapter

    responses:
      200:
        body:
          application/json:
            type: ServerMetadataResponse

/accounts:
  displayName: GET /v1/accounts
  get:
    description: Retrieve all accounts

    responses:
      200:
        body:
          application/json:
            type: GetAccountsResponse

      403:  #UnauthorizedError
        description: HTTP/1.1 403 Forbidden - UnauthorizedError
        body:
          application/json:
            type: UnauthorizedError

  /{id}:
    displayName: POST /v1/accounts/:id
    uriParameters:
      id: string
    get:
      description: lookup an account

      responses:
          200:
            body:
              application/json:
                type: object
                properties:
                  id: string
                  name: string
                  #UnauthenticatedResponse
                  ledger:
                    type: string
                    required: false
                  #AuthenticatedResponse
                  balance:
                    type: string
                    required: false
                  is_disabled:
                    type: boolean
                    required: false

          400:  #InvalidUriParameterError
            body:
              application/json:
                description: HTTP/1.1 400 Bad Request - InvalidUriParameterError
                type: InvalidUriParameterError

          404:  #NotFoundError
            body:
              application/json:
                description: HTTP/1.1 404 Not Found - NotFoundError
                type: NotFoundError
    put:
      description: Create an account
      body:
        application/json:
          type:  PutAccountRequest

      responses:
        200:
          body:
            application/json:
              type: PutAccountResponse

        400:  #InvalidUriParameterError, InvalidBodyError
          body:
            application/json:
              description: HTTP/1.1 400 Bad Request - InvalidUriParameterError, InvalidBodyError
              type: InvalidUriParameterError | InvalidBodyError

        403:  #UnauthorizedError
          description: HTTP/1.1 403 Forbidden - UnauthorizedError
          body:
            application/json:
              type: UnauthorizedError

/transfers/{id}:
  displayName: POST /v1/transfers/:id
  uriParameters:
    id: string
  get:
    description: Transfer - Get local transfer object

    responses:
      200:
        body:
          application/json:
            type: TransferResponse

      400:  #InvalidUriParameterError
        body:
          application/json:
            description: HTTP/1.1 400 Bad Request - InvalidUriParameterError
            type: InvalidUriParameterError

      404:  #NotFoundError
        body:
          application/json:
            description: HTTP/1.1 404 Not Found
            type: NotFoundError

  put:
    description:
    body:
      application/json:
        type: ProposeTransferRequest | PrepareTransferRequest
    responses:
      200:  #prepared success
        body:
          application/json:
            type: PreparedTransferResponse

      201:  #proposed success
        body:
          application/json:
            type: ProposedTransferResponse

      400:  #InvalidUriParameterError, InvalidBodyError
        body:
          application/json:
            description: HTTP/1.1 400 Bad Request - InvalidUriParameterError, InvalidBodyError
            type: InvalidUriParameterError | InvalidBodyError

      422:  #InsufficientFundsError, UnprocessableEntityError, AlreadyExistsError
        body:
          application/json:
            description: HTTP/1.1 422 Unprocessable Entity - InsufficientFundsError, UnprocessableEntityError, AlreadyExistsError
            type: InsufficientFundsError | UnprocessableEntityError | AlreadyExistsError

  /fulfillment:
      displayName: GET /v1/transfers/:id/fulfillment
      put:
        description: Transfer - Execute a prepared transfer
        body:
          text/plain:
            type:  string
            example: "cf:0:_v8"
          application/json:
            type:  string
            example: "cf:0:_v8"

        responses:
          200:
            body:
              text/plain:
                type:  string
                example: "cf:0:_v8"

          400:  #InvalidUriParameterError, InvalidBodyError
            body:
              application/json:
                description: HTTP/1.1 400 Bad Request - InvalidUriParameterError, InvalidBodyError
                type: InvalidUriParameterError | InvalidBodyError

          422:  #UnmetConditionError, UnprocessableEntityError
            body:
              application/json:
                description: HTTP/1.1 422 Unprocessable Entity - UnmetConditionError, UnprocessableEntityError
                type: UnmetConditionError | UnprocessableEntityError

/connectors:
      displayName: GET /v1/connectors
      get:
        description: Get all accounts of all connectors on this ledger.

        responses:
          200:
            body:
              application/json:
                type: array
                items: ConnectorDescriptor
                minItems: 1

```
