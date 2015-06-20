from server import *

if __name__ == '__main__':
    from BaseHTTPServer import HTTPServer
#server = HTTPServer(('140.113.27.45', 7123), PostHandler)
    server = HTTPServer(('localhost', 8000), PostHandler)
    print 'Starting server, use <Ctrl-C> to stop'
    server.serve_forever()
