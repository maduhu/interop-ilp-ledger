**This document provides a general overview of how the notification process shall work.**


***Logical flow of Notifications (This could become a sequence diagram)***

1. (Server) When ILP Ledger (Mule) starts up, it starts up the Java Web Socket Server and is ready for clients to start registering Accounts for notifications.
2. (Client) A client that wants to receive notifications for an account, must register against the Web Socket in  interop_ilp_ledger adapter (Mule application). This will probably happen during the Setup phase of SPSP.
3. (Server) An entry is made into the notification list on the Web Socket server.
4. (Client) A payment sends either (Prepare) or (Execute).
5. (Server) interop-ilp-ledger app receives the request and processes as normal.
    1. When API calls receive responses from ILP Connector or ILP Ledger, it attempts to send notification(s)
    2. It checks the "Notification Array" for a registered account, if it finds one or more for the same account, it sends a notification to all registered clients.
    3. If no client is registered on the account, the message is thrown away.

Below is an overview of the Notification process.  For the full details, see the following link: 
https://github.com/LevelOneProject/Docs/blob/master/ILP/ledger-adapter.md#subscribe-to-account

**Receivers / Subscribers of notifications**

**Potential Subscribers (Interested Parties):**

From Evan:
“Any account holder + admins should be able to subscribe to notifications. In practice some of the parties that definitely will subscribe are the ILP Connectors, the ILP/SPSP Client and Server (using admin credentials)”

**Subscribers of notifications**
Below is the overview of the basic functionality  

How to subscribe:
Clients can subscribe to live, read-only notifications of ledger activity by opening a WebSocket connection to the /websocket path and sending a subscription request.

**WebSocket Authentication**

Clients MUST authenticate themselves using a token in the token query parameter. The authentication you use when opening the connection applies to all subscriptions made in the connection. Non-admin clients can only subscribe to changes to the account they own.

A ledger MUST support multiple independent WebSocket connections with the same authentication. (This provides connection redundancy for notifications, which is important for ILP connectors.)


**WebSocket Messages**

After the connection is established, the client and ledger communicate by passing messages back and forth in JSON-RPC 2.0 format. Both the client and ledger can take the roles of "client" and "server" in JSON-RPC terms. The client can submit requests to subscribe or unsubscribe from specific categories of message. The ledger responds directly to the client's requests with confirmation messages, and also sends "notification" requests to the client. (Notifications are identified by the id value null.)

The only subscription type defined at this time is "Subscribe to Account."

Reference to ILP Ledger Web Socket Subscribe

**Message format**

For output.  Is the transfer object that is all at this point for the return message (on successful execution)
Error message return 



**Some useful information on Web Sockets jsonrcp**

** hyperlink http://www.jsonrpc.org/specification  **


http://stackoverflow.com/questions/10882370/websocket-request-response-subprotocol
"The WebSocket Application Messaging Protocol (WAMP) http://wamp.ws/ provides RPC (Remote Procedure Call) and PubSub (Publish & Subscribe) messaging patterns on top of raw WebSocket for that purpose.
WAMP is a proper WebSocket subprotocol, uses WebSocket as transport and JSON as a payload format. RPC is implemented using 3 messages, and those messages contain a "Call ID" to correlate asynchronous RPC server responses to client initiated procedure calls.
Disclaimer: I am author of WAMP and some (open-source) WAMP implementations. Its an open initiative, with others already started to get on the boat. Ultimately, there should be a WAMP RFC properly defining the protocol .. but its still in the early stages."