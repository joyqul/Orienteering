import time

class Client:
    def __init__(self):
        self.name = ""
        self.fname = ""
        self.latitude = 0.0
        self.longitude = 0.0
        self.others_msg = []
        self.msg = []
        self.game_id = -1

    def init(self, game_id):
        self.msg[:] = []
        self.others_msg[:] = []
        self.game_id = game_id
        self.fname = "files/ranks/"+str(self.game_id)

    def set_name(self, name):
        self.name = name

    def set_lat(self, latitude):
        self.latitude = latitude

    def set_long(self, longitude):
        self.longitude = longitude

    def write_rank(self):
        with open(self.fname, "a") as f:
            f.write(self.name+","+time.strftime("%d %b %Y %H:%M:%S", time.gmtime())+"\n")
        f.close()
    
    def get_rank(self):
        rank_list = []
        with open(self.fname) as f:
            for line in f:
                my_name, my_time = line.split(',')
                my_time = my_time.split('\n')[0]
                rank_list.append([my_name, my_time])
        f.close()

        return rank_list
