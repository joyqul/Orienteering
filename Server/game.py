from message import *

class Game:
    def __init__(self, name, finit_hint, fhint):
        self.name = name
        self.init_hints = []
        self.hints = []
        self.set_init_hint(finit_hint)
        self.set_hint(fhint)

    def set_init_hint(self, fname):
        with open(fname) as f:
            for line in f:
                self.init_hints.append(line)

    def set_hint(self, fname):
        with open(fname) as f:
            for line in f:
                latitude, longitude, content = line.split(',')
                self.hints.append(Message(latitude, longitude, content))
