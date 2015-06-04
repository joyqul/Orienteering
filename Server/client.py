class Client:
    def __init__(self):
        self.name = ""
        self.latitude = 0.0
        self.longitude = 0.0
        self.others_msg = []
        self.msg = []
        self.game_id = -1

    def init(self, game_id):
        self.msg[:] = []
        self.others_msg[:] = []
        self.game_id = game_id

    def set_name(self, name):
        self.name = name

    def set_lat(self, latitude):
        self.latitude = latitude

    def set_long(self, longitude):
        self.longitude = longitude

