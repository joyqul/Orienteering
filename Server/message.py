class Message:
    def __init__(self, latitude, longitude, content):
        self.latitude = float(latitude)
        self.longitude = float(longitude)
        self.content = content
        self.sent = []

