#!/usr/bin/expect -f
spawn telnet 127.0.0.1 1298
sleep 1
send "{\"jsonType\": -1}\n"
expect -re "feedback"
send "{\"jsonType\": 4, \"token\": \"d41d8cd98f00b204e9800998ecf8427e\"}\n"
expect -re "feedback"
send "{\"jsonType\": 0, \"playerName\": \"joyqul\", \"token\": \"d41d8cd98f00b204e9800998ecf8427e\", \"gameId\": 0}\n"
expect -re "feedback"
send "{\"jsonType\": 1, \"token\": \"d41d8cd98f00b204e9800998ecf8427e\"}\n"
expect -re "feedback"
send "{\"jsonType\": 2, \"token\": \"d41d8cd98f00b204e9800998ecf8427e\", \"lat\": 24.7837997, \"long\": 120.9952618}\n"
expect -re "feedback"
send "{\"jsonType\": 2, \"token\": \"d41d8cd98f00b204e9800998ecf8427e\", \"lat\": 24.7853808, \"long\": 120.9991967}\n"
expect -re "feedback"
send "{\"jsonType\": 2, \"token\": \"d41d8cd98f00b204e9800998ecf8427e\", \"lat\": 10, \"long\": 100}\n"
expect -re "feedback"
send "{\"jsonType\": 3, \"token\": \"d41d8cd98f00b204e9800998ecf8427e\", \"msg\": \"qwqq\"}\n"
send "exit\n"
