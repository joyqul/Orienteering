#!/usr/bin/expect -f
spawn telnet 127.0.0.1 1035
sleep 1
send "{\"jsonType\":-1}\n"
expect -re "feedback"
send "{\"jsonType\":4, \"token\": \"d41d8cd98f00b204e9800998ecf8427e\"}\n"
expect -re "feedback"
send "{\"jsonType\": 0, \"playerName\": \"joyqul\", \"token\": \"d41d8cd98f00b204e9800998ecf8427e\", \"gameId\": 0}\n"
expect -re "feedback"
send "exit\n"
