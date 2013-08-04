
# Mustard - Event/Messaging Server

A WebSocket server designed for broadcasting and exchanging
event related messages between connected users.  The messages
parsed should be JSON, with the targetted users specified.

## Usage

Clone the repo then run:

```
lein run
```

## Client Connect

After the server has been started clients can connect.  The first
message they need to send is their ID.

```javascript
var ws = new WebSocket('ws://localhost:8080');
ws.send('ID');
```

## Sending Messages

Messages can be sent by using a JSON object, which specified the
connected user IDs to receive the message.

```javascript
ws.send(
  JSON.stringify({users: [1,2,3], message: 'hello'})
);
```

The above message will be sent to users _1_, _2_, and _3_.  The
user will receive the entire string of JSON, including the other
recipient IDs.

