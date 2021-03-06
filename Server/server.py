from settings import *
PACKET_SIZE = 1024

class Server:
    def __init__(self):
        self.games = []
        self.client = {}
        self.set_game("files/games")

    def handle_host(self, data):
        blocks = data.split(',')
        if len(blocks) < 5:
            return "BAD REQUEST"

        game_id = int(blocks[1])
        latitude = float(blocks[2])
        longitude = float(blocks[3])

        #########################################
        # Add hint to certain game id           #
        # ------------------------------------- #
        # hint,(game id),(lat),(long),(content) #
        #########################################
        if blocks[0] == "hint":
            self.games[game_id].add_hint(Message(latitude, longitude, blocks[4]))
            return "OK"

        ###########################################
        # Add key to certain game id              #
        # --------------------------------------- #
        # key,(key id),(lat),(long),(index),(pwd) #
        ###########################################
        if blocks[0] == "key":
            self.games[game_id].add_key(Key(latitude, longitude, blocks[4], blocks[5]))
            return "OK"

        return "BAD REQUEST"

    def near(self, latitude, longitude, hint):
#print type(latitude), type(longitude), type(hint.latitude)
        if ((latitude-hint.latitude)**2+(longitude-hint.longitude)**2)**0.5 < 0.005:
            return True
        return False

    def handle_json(self, data):
        try:
            json_data = json.loads(data)
        except ValueError, e:
            return self.handle_host(data)

        try:
            json_type = json_data['jsonType']
        except ValueError, e:
            print >>sys.stderr, e
            return 'ERROR'
        except TypeError, e:
            print >>sys.stderr, e
            return 'ERROR'

        ##############################
        ### Fist time use this app ###
        ##############################
        if json_type == -1:
            m = md5.new()
            mykey = m.hexdigest()
            while mykey in self.client:
                m.update("wala")
                mykey = m.hexdigest()
            self.client[mykey] = Client()
            response = {}
            response["token"] = mykey
            response = json.dumps(response)
            print >>sys.stderr, "[[[[[[get]]]]]"+mykey
            return response

        ##############################################
        ### Not the first time, so there's a token ###
        ### Then get the token                     ###
        ##############################################
        try:
            my_token = json_data["token"]
        except KeyError, e:
            print >>sys.stderr, e, 'no key token'
            return 'ERROR'
            
        ###################################################################
        ### Now we have token, then we can do things via token          ###
        ### Choosing a new game, if continue, it won't enter this phase ###
        ###################################################################
        if json_type == 4:
            response = {}
            response["gameCnt"] = len(self.games)
            gid = 0
            for g in self.games:
                label = "game"+str(gid)
                response[label] = g.name
                gid = gid + 1
            response = json.dumps(response)
            print >>sys.stderr, 'retrun games'
            return response
        
        ########################################
        ### User set his/her name and gameId ###
        ########################################
        if json_type == 0:
            try:
                name = json_data["playerName"]
            except KeyError, e:
                print >>sys.stderr, e, 'no key playerName'
                return 'ERROR'
            try:
                game_id = json_data["gameId"]
            except KeyError, e:
                print >>sys.stderr, e, 'no key gameId'
                return 'ERROR'

            response = {}
            if self.set_client_name(name, my_token):
                self.client[my_token].init(game_id)
                print >>sys.stderr, 'set name: ', name, ' & set game_id: ', game_id
                response["success"] = "true"
                for c in self.client:
                    self.client[my_token].others_msg.extend(self.client[c].msg)
            else:
                response["success"] = "false"

            response["answer"] = self.games[game_id].answer
            response["goalLat"] = self.games[game_id].goal_lat
            response["goalLong"] = self.games[game_id].goal_long
            response["totalKey"] = self.games[game_id].total_key
            response = json.dumps(response)
            return response

        ##################################
        ### User get the initial hints ###
        ##################################
        playing_game_id = self.client[my_token].game_id
        if json_type == 1:
            response = {}
            self.write_cnt_response(response, "hint", self.games[playing_game_id].init_hints)
            response = json.dumps(response)
            print >>sys.stderr, 'retrun hints'
            return response

        ##################################
        ### User send his/her position ###
        ##################################
        if json_type == 2:
            try:
                latitude = json_data["lat"]
                longitude = json_data["long"]
            except ValueError, e:
                print >>sys.stderr, e, 'no lat or long obj'
                return 'ERROR'
            else:
                response = {}
                self.client[my_token].set_lat(latitude)
                self.client[my_token].set_long(longitude)
                print >>sys.stderr, 'lat: ', json_data["lat"], 'long: ', json_data["long"]

                player_id = 0
                for s in self.client:
                    if s == my_token or self.client[s].game_id != self.client[my_token].game_id:
                        continue
                    player = "player"+str(player_id)
                    player_id = player_id + 1
                    position = {}
                    position["lat"] = self.client[s].latitude
                    position["long"] = self.client[s].longitude
                    response[player] = position
                
                response["playerCnt"] = player_id

                hint_id = 0
                for h in self.games[playing_game_id].hints:
                    if my_token in h.sent:
                        continue
                    if self.near(latitude, longitude, h):
                        hint = "hint"+str(hint_id)
                        response[hint] = h.content
                        hint_id = hint_id + 1
                        h.sent.append(my_token)

                response["hintCnt"] = hint_id

                msg_id = 0
                for m in self.client[my_token].others_msg:
                    data = {}
                    data["lat"] = m.latitude
                    data["long"] = m.longitude
                    data["content"] = m.content
                    msg = "msg"+str(msg_id)
                    response[msg] = data
                    msg_id = msg_id + 1

                response["msgCnt"] = msg_id
                del self.client[my_token].others_msg[:]

                key_id = 0
                for k in self.games[self.client[my_token].game_id].keys:
                    if self.near(latitude, longitude, k):
                        data = {}
                        key = "key"+str(key_id)
                        data["index"] = k.index
                        data["pwd"] = k.content
                        response[key] = data
                        key_id = key_id + 1
                        
                response["keyCnt"] = key_id
                response = json.dumps(response)
                return response
                
        ##########################
        ### User leave message ###
        ##########################
        if json_type == 3:
            content = json_data["msg"]
            latitude = self.client[my_token].latitude
            longitude = self.client[my_token].longitude
            message = Message(latitude, longitude, content)

            for c in self.client:
                if c == my_token:
                    self.client[c].msg.append(message)
                self.client[c].others_msg.append(message)

            print >>sys.stderr, 'get msg: ', json_data["msg"]
            response = {}
            response["success"] = "true"
            response = json.dumps(response)
            return response

        #############################
        ### User finish this game ###
        #############################
        if json_type == 5:
            self.client[my_token].write_rank()
            rank_list = self.client[my_token].get_rank()
            response = {}

            rank_id = 0
            for r in rank_list:
                data = {}
                rank = "rank"+str(rank_id)
                data["time"] = r[1]
                data["name"] = r[0]
                response[rank] = data
                rank_id = rank_id + 1

            response["rankCnt"] = rank_id
            response = json.dumps(response)
            return response

        print >>sys.stderr, 'none of above'
        return "test"
        
    def set_client_name(self, name, my_token):
        for client in self.client:
            if name == self.client[client].name:
                return False
        self.client[my_token].set_name(name)
        return True

    def write_cnt_response(self, response, base_str, target_list):
        response[base_str+"Cnt"] = len(target_list)
        my_id = 0
        for data in target_list:
            label = base_str+str(my_id)
            response[label] = data
            my_id = my_id + 1

    def set_game(self, fname):
        with open(fname) as f:
            for line in f:
                game_name, init_hints_file, hints_file, answer, goal_lat, goal_long, total_key, key_file = line.split(',')
                key_file = key_file.split('\n')[0]
                self.games.append(Game(game_name, init_hints_file, hints_file, answer, goal_lat, goal_long, total_key, key_file))
        f.close()

class PostHandler(BaseHTTPRequestHandler):
    global server
    server = Server()

    def do_POST(self):

        # Parse the form data posted
        length = int(self.headers.getheader('Content-Length'))
        print self.headers
        print "length", length
        data = self.rfile.read(length)
        #data = cgi.parse_qs(self.rfile.read(length), keep_blank_values=1)
        print "[[[Get]]]", data
        response = ""
        response = server.handle_json(data)
        print "[[[Send]]]", response
        
        # Begin the response
        self.send_response(200)
        self.end_headers()
        self.wfile.write(response)
        self.wfile.close()
        return
