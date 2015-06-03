from message import *

class Game:
    def __init__(self, name, finit_hint, fhint, answer, goal_lat, goal_long, total_key):
        self.name = name
        self.init_hints = []
        self.hints = []
        self.answer = answer
        self.goal_lat = float(goal_lat)
        self.goal_long = float(goal_long)
        self.total_key = int(total_key)
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
