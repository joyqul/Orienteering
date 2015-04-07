import sys
import json
import select
import socket
import sys
import argparse
import random
import Queue

PACKET_SIZE = 1024

class Client:
    def __init__(self, address):
        self.address = address
        self.name = ""

    def set_name(self, name):
        self.name = name

class Server:
    def __init__(self, ip='127.0.0.1', port=10001):
        port = 1024+random.randint(1, 1000)
        self.address = (ip, port)
        self.client = {}

    def handle_json(self, data, my_socket):
        try:
            json_data = json.loads(data)
        except ValueError, e:
            print >>sys.stderr, e
            return 'ERROR\n'
        except TypeError, e:
            print >>sys.stderr, e
            return 'ERROR\n'

        try:
            json_type = json_data['jsonType']
        except ValueError, e:
            print >>sys.stderr, e
            return 'ERROR\n'
        except TypeError, e:
            print >>sys.stderr, e
            return 'ERROR\n'

        if json_type == 0:
            print >>sys.stderr, 'set name: ', json_data["playerName"]
        elif json_type == 1:
            print >>sys.stderr, 'retrun hints'
        elif json_type == 2:
            print >>sys.stderr, 'lat: ', json_data["lat"], 'long: ', json_data["long"]
        elif json_type == 3:
            print >>sys.stderr, 'get msg: ', json_data["msg"]
        else:
            print >>sys.stderr, 'none of above'
            
        return "test\n"
        

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
                    new_client = Client(client_address)
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


if __name__ == '__main__':
    server = Server()
    server.listen()
