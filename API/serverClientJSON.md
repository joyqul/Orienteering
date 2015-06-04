Init
===
- 140.113.27.45
- 7123
- Only when user first using this app

Client --> Server
---
1. {"jsonType": -1} 

Server --> Client
---
1. {"token": string (md5 hash)}

Choosing new game
===
- Only when playing new game
- If continue, app will not enter this phase

Client --> Server
---
1. {"jsonType": 4}
2. {"token": string (md5 hash)}

Server --> Client
---
1. {"gameCnt": int (number of availible game)}
2. {"game0" to "game(cnt-1)": string (game's name)}

Player name
===
- Only when playing new game
- If continue, app will not enter this phase

Client --> Server
---
1. {"jsonType": 0}
2. {"playerName": string (player's name)}
3. {"token": string (md5 hash)}
4. {"gameId": int}

Server --> Client
---
1. {"success": [true, false]}
2. {"answer": string (answer of goal)}
3. {"goalLat": float}
4. {"goalLong": float}
5. {"totalKey": int (number of key)}

Initial hints
===

Client --> Server
---
1. {"jsonType": 1}
2. {"token": string (md5 hash)}

Server --> Client
---
1. {"hintCnt": int (number of hint)}
2. {"hint0" to "hint(cnt-1)": string (hint's body)}

Playing (transmit every 5 sec)
===

Client --> Server
---
1. {"jsonType": 2}
2. {"lat": float (latitude)}
3. {"long": float (longitude)}
4. {"token": string (md5 hash)}

Server --> Client
---
1. {"playerCnt": int (number of player)}
2. {"player0" to "player(cnt-1)": {"lat" : float, "long": float}}
3. {"hintCnt": int (number of hint)}
4. {"hint0" to "hint(cnt-1)": string (content)}
5. {"msgCng": int (number of message)}
6. {"msg0" to "msg(cnt-1)": {"lat": float, "long": float, "content": string}}
7. {"keyCnt": int (number of key)}
8. {"key0" to "key(cnt-1)": {"index": int (index of key, 0-base), "pwd": char (final string that index's char} }

Leave message
===

Client --> Server
---
1. {"jsonType": 3}
2. {"msg": string (content)}
3. {"token": string (md5 hash)}

Server --> Client
---
1. {"success": [true, false]}

Ranking
===

Client --> Server
---
1. {"jsonType": 5}
2. {"token": string (md5 hash)}

Server --> Client
---
1. {"rankCnt": int}
2. {"rank0" to "rank(cnt-1)": {"time": string, "name": string}}

