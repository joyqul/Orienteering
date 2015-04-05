import select
import socket
import sys
import argparse
import random
import Queue

class Server:
    def __init__(self, ip='127.0.0.1', port=10001):
        port = 1024+random.randint(1, 1000)
        self.server_address = (ip, port)

    def listen(self):
        # Create a TCP/IP socket
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.setblocking(0)
        
        # Bind the socket to the port
        print >>sys.stderr, 'starting up on %s port %s' % self.server_address
        self.sock.bind(self.server_address)
        
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
                    connection.setblocking(0)
                    inputs.append(connection)
    
                    # Give the connection a queue for data we want to send
                    message_queues[connection] = Queue.Queue()
                else:
                    data = s.recv(1024)
                    print "get: ", data
                    if data:
                        # A readable client socket has data
                        print >>sys.stderr, 'received "%s" from %s' % (data, s.getpeername())
                        message_queues[s].put(data)
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
