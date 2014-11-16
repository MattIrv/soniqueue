import queues
import song
import user
import json

class Party(object):
	party_map = {}
	cur_party_id = 0

	def __init__(self, host, name, location):
		self.name = name
		self.host = host
		self.location = location
		self.users = set()
		self.queue = queues.PartyQueue()
		self.now_playing = None
		self.party_id = Party.cur_party_id
		Party.party_map[self.party_id] = self
		Party.cur_party_id += 1

	def __str__(self):
		return "Party(%s, %s, %s) : %s" % (str(self.name), str(self.host), str(self.location), str(self.party_id))

	def __repr__(self):
		return "Party(%s, %s, %s) : %s" % (str(self.name), str(self.host), str(self.location), str(self.party_id))

	def add_user(self, user):
		self.users.add(user)
		self.queue.add_user(user)
		user.set_party(self)

	def remove_user(self, user):
		self.users.remove(user)
		self.queue.remove_user(user)
		user.set_party(None)

	def end(self):
		pass

	def next_song(self):
		user = self.queue.pop()
		song = user.queue.pop()
		self.queue.add_user(user)
		now_playing = song

	def jsonify(self):
		return json.dumps({'party_id': self.party_id, 'name': self.name, 'queue': self.queue.list_as_dicts(),
			'users': self.users, 'now_playing': self.now_playing})

	@staticmethod
	def jsonify_parties_ids():
		party_list = [value.jsonify() for key, value in party_map.iteritems()]
		return json.dumps({'party_list': party_list})
