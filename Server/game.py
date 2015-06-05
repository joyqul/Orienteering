from message import *
from key import *

class Game:
    def __init__(self, name, finit_hint, fhint, answer, goal_lat, goal_long, total_key, fkey):
        self.name = name
        self.finit_hint = finit_hint
        self.fhint = fhint
        self.answer = answer
        self.goal_lat = float(goal_lat)
        self.goal_long = float(goal_long)
        self.total_key = int(total_key)
        self.fkey = fkey

        self.init_hints = []
        self.set_init_hint(finit_hint)
        self.hints = []
        self.set_hint(fhint)
        self.keys = []
        self.set_key(fkey)

    def set_init_hint(self, fname):
        with open(fname) as f:
            for line in f:
                self.init_hints.append(line)
        f.close()

    def set_hint(self, fname):
        with open(fname) as f:
            for line in f:
                latitude, longitude, content = line.split(',')
                self.hints.append(Message(latitude, longitude, content))
        f.close()

    def set_key(self, fname):
        with open(fname) as f:
            for line in f:
                latitude, longitude, index, content = line.split(',')
                self.keys.append(Key(latitude, longitude, index, content[0]))
        f.close()

    def add_hint(self, hint):
        self.hints.append(hint)
        with open(self.fhint, "a") as f:
            f.write(str(hint.latitude)+","+str(hint.longitude)+","+hint.content+"\n")
        f.close()
            
    def add_key(self, key):
        self.keys.append(key)
        with open(self.fkey, "a") as f:
            f.write(str(key.latitude)+","+str(key.longitude)+","+str(key.index)+","+key.content+"\n")
        f.close()
