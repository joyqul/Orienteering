from server import *

if __name__ == '__main__':
#server = Server("140.113.27.45", 7123)
    server = Server()
    server.set_hint("hints")
    server.set_init_hint("init_hints")
    server.set_game("games")
    server.listen()
