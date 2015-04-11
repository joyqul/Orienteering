import sys
import json
import select
import socket
import sys
import argparse
import random
import Queue

PACKET_SIZE = 1024

class Message:
    def __init__(self, latitude, longitude, content):
        self.latitude = latitude
        self.longitude = longitude
        self.content = content
        self.sent = []

class Client:
    def __init__(self):
        self.name = ""
        self.latitude = 0.0
        self.longitude = 0.0
        self.others_msg = []
        self.msg = []

    def set_name(self, name):
        self.name = name

    def set_lat(self, latitude):
        self.latitude = latitude

    def set_long(self, longitude):
        self.longitude = longitude

class Server:
    def __init__(self, ip='127.0.0.1', port=10001):
        port = 1024+random.randint(1, 1000)
        self.address = (ip, port)
        self.client = {}
        self.init_hints = []
        self.hints = []

    def near(self, latitude, longitude, hint):
        return True

    def handle_json(self, data, my_socket):
        try:
            json_data = json.loads(data)
        except ValueError, e:
            print >>sys.stderr, e
            return 'ERROR'
        except TypeError, e:
            print >>sys.stderr, e
            return 'ERROR'

        try:
            json_type = json_data['jsonType']
        except ValueError, e:
            print >>sys.stderr, e
            return 'ERROR'
        except TypeError, e:
            print >>sys.stderr, e
            return 'ERROR'

        if json_type == 0:
            try:
                name = json_data["playerName"]
            except ValueError, e:
                print >>sys.stderr, e, 'no name obj'
                return 'ERROR'
            else:
                response = {}
                if self.set_client_name(name, my_socket):
                    print >>sys.stderr, 'set name: ', name
                    response["success"] = "true"
                    for c in self.client:
                        self.client[my_socket].others_msg.extend(self.client[c].msg)
                else:
                    response["success"] = "false"
                response = json.dumps(response)
                return response
                
        elif json_type == 1:
            response = {}
            response["hintCnt"] = len(self.init_hints)
            hint_id = 0
            for h in self.init_hints:
                hint = "hint"+str(hint_id)
                response[hint] = h
                hint_id = hint_id + 1
            response = json.dumps(response)
            print >>sys.stderr, 'retrun hints'
            return response

        elif json_type == 2:
            try:
                latitude = json_data["lat"]
                longitude = json_data["long"]
            except ValueError, e:
                print >>sys.stderr, e, 'no lat or long obj'
                return 'ERROR'
            else:
                response = {}
                self.client[my_socket].set_lat(latitude)
                self.client[my_socket].set_long(longitude)
                print >>sys.stderr, 'lat: ', json_data["lat"], 'long: ', json_data["long"]

                player_id = 0
                for s in self.client:
                    if s == my_socket:
                        continue
                    player = "player"+str(player_id)
                    player_id = player_id + 1
                    position = {}
                    position["lat"] = self.client[s].latitude
                    position["long"] = self.client[s].longitude
                    response[player] = position
                
                response["playerCnt"] = player_id

                hint_id = 0
                for h in self.hints:
                    if my_socket in h.sent:
                        continue
                    if self.near(latitude, longitude, h):
                        hint = "hint"+str(hint_id)
                        response[hint] = h.content
                        hint_id = hint_id + 1
                        h.sent.append(my_socket)

                response["hintCnt"] = hint_id

                msg_id = 0
                for m in self.client[my_socket].others_msg:
                    data = {}
                    data["lat"] = m.latitude
                    data["long"] = m.longitude
                    data["content"] = m.content
                    msg = "msg"+str(msg_id)
                    response[msg] = data
                    msg_id = msg_id + 1

                response["msgCnt"] = msg_id
                        
                response = json.dumps(response)
                return response
                
        elif json_type == 3:
            content = json_data["msg"]
            latitude = self.client[my_socket].latitude
            longitude = self.client[my_socket].longitude
            message = Message(latitude, longitude, content)

            for c in self.client:
                if c == my_socket:
                    self.client[c].msg.append(message)
                else: 
                    self.client[c].others_msg.append(message)
            print >>sys.stderr, 'get msg: ', json_data["msg"]
            response = {}
            response["success"] = "true"
            response = json.dumps(response)
            return response
        else:
            print >>sys.stderr, 'none of above'
            
        return "test"
        
    def set_client_name(self, name, my_socket):
        for client in self.client:
            if name == self.client[client].name:
                return False
        self.client[my_socket].set_name(name)
        return True

    def listen(self):
        # Create a TCP/IP socket
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setblocking(0)
        
        # Bind the socket to the port
        print >>sys.stderr, 'starting up on %s port %s' % self.address
        self.sock.bind(self.address)
        
        # Listen for incoming connections
        self.sock.listen(5)

        # Sockets from which we expect to read
        inputs = [self.sock]
        # Sockets to which we expect to write
        outputs = [self.sock]
        # Outgoing message queues (socket:Queue)
        message_queues = {}
        
        while inputs:
            # Wait for at least one of the sockets to be ready for processing
            print >>sys.stderr, '\nwaiting for the next event'
            readable, writable, exceptional = select.select(inputs, outputs, inputs)

            # Handle inputs
            for s in readable:
                if s is self.sock:
                    # A "readable" server socket is ready to accept a connection
                    connection, client_address = s.accept()
                    print >>sys.stderr, 'new connection from', client_address
                    new_client = Client()
                    self.client[connection] = new_client
                    connection.setblocking(0)
                    inputs.append(connection)
    
                    # Give the connection a queue for data we want to send
                    message_queues[connection] = Queue.Queue()
                else:
                    data = s.recv(PACKET_SIZE)
                    print "get: ", data
                    if data:
                        # A readable client socket has data
                        print >>sys.stderr, 'received "%s" from %s' % (data, s.getpeername())
                        response = self.handle_json(data, s)
                        message_queues[s].put(response)
                        # Add output channel for response
                        if s not in outputs:
                            outputs.append(s)
                    else:
                        # Interpret empty result as closed connection
                        print >>sys.stderr, 'closing', client_address, 'after reading no data'
                        # Stop listening for input on the connection
                        if s in outputs:
                            outputs.remove(s)
                        inputs.remove(s)
                        del self.client[s]
                        print 'remove client: ', self.client
                        s.close()

                        # Remove message queue
                        del message_queues[s]

            # Handle outputs
            for s in writable:
                try:
                    next_msg = message_queues[s].get_nowait()
                except Queue.Empty:
                    # No messages waiting so stop checking for writability.
                    print >>sys.stderr, 'output queue for', s.getpeername(), 'is empty'
                    outputs.remove(s)
                else:
                    print >>sys.stderr, 'sending "%s" to %s' % (next_msg, s.getpeername())
                    s.send(next_msg)

            # Handle "exceptional conditions"
            for s in exceptional:
                print >>sys.stderr, 'handling exceptional condition for', s.getpeername()
                # Stop listening for input on the connection
                inputs.remove(s)
                if s in outputs:
                    outputs.remove(s)
                s.close()

                # Remove message queue
                del message_queues[s]

    def set_init_hint(self, fname):
        with open(fname) as f:
            for line in f:
                self.init_hints.append(line)

    def set_hint(self, fname):
        with open(fname) as f:
            for line in f:
                latitude, longitude, content = line.split(',')
                self.hints.append(Message(latitude, longitude, content))

if __name__ == '__main__':
    server = Server()
    server.set_hint("hints")
    server.set_init_hint("init_hints")
    server.listen()
