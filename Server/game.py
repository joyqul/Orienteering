from message import *

class Key:
    def __init__(self, latitude, longitude, index, content):
        self.latitude = float(latitude)
        self.longitude = float(longitude)
        self.index = int(index)
        self.content = content

class Game:
    def __init__(self, name, finit_hint, fhint, answer, goal_lat, goal_long, total_key, fkey):
        self.name = name
        self.init_hints = []
        self.hints = []
        self.answer = answer
        self.goal_lat = float(goal_lat)
        self.goal_long = float(goal_long)
        self.total_key = int(total_key)
        self.keys = []
        self.set_init_hint(finit_hint)
        self.set_hint(fhint)
        self.set_key(fkey)

    def set_init_hint(self, fname):
        with open(fname) as f:
            for line in f:
                self.init_hints.append(line)

    def set_hint(self, fname):
        with open(fname) as f:
            for line in f:
                latitude, longitude, content = line.split(',')
                self.hints.append(Message(latitude, longitude, content))

    def set_key(self, fname):
        with open(fname) as f:
            for line in f:
                latitude, longitude, index, content = line.split(',')
                self.keys.append(Key(latitude, longitude, index, content[0]))
