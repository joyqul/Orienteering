Player name
===

Client --> Server
---
1. {"jsonType": 0}
2. {"playerName": string (player's name)}

Server --> Client
---
1. {"success": [true, false]}

Initial hints
===

Client --> Server
---
1. {"jsonType": 1}

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

Server --> Client
---
1. {"playerCnt": int (number of player)}
2. {"player0" to "player(cnt-1)": {"lat" : float, "long": float}}
3. {"hintCnt": int (number of hint)}
4. {"hint0" to "hint(cnt-1)": string (content)}
5. {"msgCng": int (number of message)}
6. {"msg0" to "msg(cnt-1)": {"lat": float, "long": float, "content": string}}

Leave message
===

Client --> Server
---
1. {"jsonType": 3}
2. {"msg": string (content)}

Server --> Client
---
1. {"success": [true, false]}
