from urllib import urlopen
import json

data = {"jsonType":-1}
response = urlopen("http://140.113.27.45:7123", json.dumps(data).encode())
print(response.read().decode())
