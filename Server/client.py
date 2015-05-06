class Client:
    def __init__(self):
        self.name = ""
        self.latitude = 0.0
        self.longitude = 0.0
        self.others_msg = []
        self.msg = []

    def set_name(self, name):
        self.name = name

    def set_lat(self, latitude):
        self.latitude = latitude

    def set_long(self, longitude):
        self.longitude = longitude

