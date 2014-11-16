import tornado.ioloop
import tornado.web
import json
import util

class CreatePartyHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        data = json.loads(self.request.body)
        party_name = data['party_name']
        party_location = data['location']
        self.write(util.create_party(u_id, party_name, party_location))

class EndPartyHandler(tornado.web.RequestHandler):
    def post(self, party_id):
        p_id = int(party_id)
        self.write(util.end(p_id))

class NextSongHandler(tornado.web.RequestHandler):
    def post(self, party_id):
        p_id = int(party_id)
        self.write(util.next(p_id))

class NowPlayingHandler(tornado.web.RequestHandler):
    def post(self, party_id):
        p_id = int(party_id)
        self.write(util.now_playing(p_id))

class JoinPartyHandler(tornado.web.RequestHandler):
    def post(self, party_id, user_id):
        p_id = int(party_id)
        u_id = int(user_id)
        self.write(util.join(p_id, u_id))

class ListPartiesHandler(tornado.web.RequestHandler):
    def post(self):
        self.write(util.list_parties())

class LeavePartyHandler(tornado.web.RequestHandler):
    def post(self, party_id, user_id):
        p_id = int(party_id)
        u_id = int(user_id)
        self.write(util.leave(p_id, u_id))

class LoginHandler(tornado.web.RequestHandler):
    def post(self):
        data = json.loads(self.request.body)
        email = data['email']
        location = data['location']
        self.write(util.login(email, location))

class AddSongHandler(tornado.web.RequestHandler):
    def post(self, user_id, spotify_id):
        u_id = int(user_id)
        self.write(util.add(u_id, spotify_id))

class RemoveSongHandler(tornado.web.RequestHandler):
    def post(self, user_id, song_id):
        u_id = int(user_id)
        s_id = int(song_id)
        self.write(util.remove(u_id, s_id))

class ClearUserQueueHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        self.write(util.clear(u_id))

class MoveSongHandler(tornado.web.RequestHandler):
    def post(self, user_id, song_id, displacement):
        u_id = int(user_id)
        s_id = int(song_id)
        disp = int(displacement)
        self.write(util.move(u_id, s_id, disp))

class ListUserQueueHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        self.write(util.list_user_queue(u_id))

class ListPartyQueueHandler(tornado.web.RequestHandler):
    def post(self, party_id):
        p_id = int(party_id)
        self.write(util.list_party_queue(p_id))

class UserInfoHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        self.write(util.user_info(u_id))

class SetAliasHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        data = json.loads(self.request.body)
        alias = data['alias']
        self.write(util.set_alias(u_id, alias))

class SetLocationHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        data = json.loads(self.request.body)
        location = data['location']
        self.write(util.set_location(u_id, location))



application = tornado.web.Application([

    (r'/lobby/create/([0-9]+)', CreatePartyHandler), #yeah
    (r'/party/([0-9]+)/end', EndPartyHandler),
    (r'/party/([0-9]+)/next', NextSongHandler),
    (r'/party/([0-9]+)/nowplaying', NowPlayingHandler),

    (r'/party/([0-9]+)/join/([0-9]+)', JoinPartyHandler),
    (r'/lobby/list', ListPartiesHandler), #yeah
    (r'/party/([0-9]+)/leave/([0-9]+)', LeavePartyHandler),
    (r'/lobby/login', LoginHandler), #yeah

    (r'/user/([0-9]+)/add/(.+)', AddSongHandler), #yeah
    (r'/user/([0-9]+)/remove/([0-9]+)', RemoveSongHandler), #yeah
    (r'/user/([0-9]+)/clear', ClearUserQueueHandler),
    (r'/user/([0-9]+)/move/([0-9]+)/([0-9-]+)', MoveSongHandler),
    (r'/user/([0-9]+)/list', ListUserQueueHandler),
    (r'/party/([0-9]+)/list', ListPartyQueueHandler),
    (r'/user/([0-9]+)/info', UserInfoHandler),
    (r'/user/([0-9]+)/setalias', SetAliasHandler),
    (r'/user/([0-9]+)/setlocation', SetLocationHandler),

])

if __name__ == '__main__':
    application.listen(80)
    tornado.ioloop.IOLoop.instance().start()
