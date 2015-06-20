from urllib import urlopen
import json

data = {"jsonType":-1}
response = urlopen("http://localhost:8000", json.dumps(data).encode())
print(response.read().decode())
