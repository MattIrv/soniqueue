import tornado.ioloop
import tornado.web

class CreatePartyHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        data = json.loads(self.request.body)
        party_name = data['party_name']
        party_location = data['location']
        return util.create_party(u_id, party_name, party_location)

class EndPartyHandler(tornado.web.RequestHandler):
    def post(self, party_id):
        p_id = int(party_id)
        return util.end(p_id)

class NextSongHandler(tornado.web.RequestHandler):
    def post(self, party_id):
        p_id = int(party_id)
        return util.next(p_id)

class NowplayingHandler(tornado.web.RequestHandler):
    def post(self, party_id):
        p_id = int(party_id)
        return util.nowplaying(p_id)

class JoinPartyHandler(tornado.web.RequestHandler):
    def post(self, party_id, user_id):
        p_id = int(party_id)
        u_id = int(user_id)
        return util.join(p_id, u_id)

class ListPartiesHandler(tornado.web.RequestHandler):
    def post(self):
        return util.list_parties()

class LeavePartyHandler(tornado.web.RequestHandler):
    def post(self, party_id, user_id):
        p_id = int(party_id)
        u_id = int(user_id)
        return util.leave(p_id, u_id)

class LoginHandler(tornado.web.RequestHandler):
    def post(self):
        data = json.loads(self.request.body)
        email = data['email']
        return util.login(email)

class AddSongHandler(tornado.web.RequestHandler):
    def post(self, user_id, spotify_id):
        u_id = int(user_id)
        return util.add(u_id, spotify_id)

class RemoveSongHandler(tornado.web.RequestHandler):
    def post(self, user_id, song_id):
        u_id = int(user_id)
        s_id = int(song_id)
        return util.remove(u_id, s_id)

class ClearUserQueueHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        return util.clear(u_id)

class MoveSongHandler(tornado.web.RequestHandler):
    def post(self, user_id, song_id, displacement):
        u_id = int(user_id)
        s_id = int(song_id)
        disp = int(displacement)
        return util.move(u_id, s_id, disp)

class ListUserQueueHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        return util.list_user_queue(u_id)

class ListPartyQueueHandler(tornado.web.RequestHandler):
    def post(self, party_id):
        p_id = int(party_id)
        return util.list_party_queue(p_id)

class UserInfoHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        return util.user_info(u_id)

class SetAliasHandler(tornado.web.RequestHandler):
    def post(self, user_id):
        u_id = int(user_id)
        data = json.loads(self.request.body)
        alias = data['alias']
        return util.set_alias(u_id, alias)



application = tornado.web.Application([

    (r'/lobby/create/([0-9]+)', CreatePartyHandler),
    (r'/party/([0-9]+)/end', EndPartyHandler),
    (r'/party/([0-9]+)/next', NextSongHandler),
    (r'/party/([0-9]+)/nowplaying', NowplayingHandler),

    (r'/party/([0-9]+)/join/([0-9]+)', JoinPartyHandler),
    (r'/lobby/list', ListPartiesHandler),
    (r'/party/([0-9]+)/leave/([0-9]+)', LeavePartyHandler),
    (r'/lobby/login', LoginHandler),

    (r'/user/([0-9]+)/add/(.+)', AddSongHandler),
    (r'/user/([0-9]+)/remove/([0-9]+)', RemoveSongHandler),
    (r'/user/([0-9]+)/clear', ClearUserQueueHandler),
    (r'/user/([0-9]+)/move/([0-9]+)/([0-9-]+)', MoveSongHandler),
    (r'/user/([0-9]+)/list', ListUserQueueHandler),
    (r'/party/([0-9]+)/list', ListPartyQueueHandler),
    (r'/user/([0-9]+)/info', UserInfoHandler),
    (r'/user/([0-9]+)/setalias', SetAliasHandler),

])

if __name__ == '__main__':
    application.listen(80)
    tornado.ioloop.IOLoop.instance().start()
