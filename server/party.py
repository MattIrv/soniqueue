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

	def abrev_json(self):
		return json.dumps(self.get_dict())

	def get_dict(self):
		return {'party_id': self.party_id, 'name': self.name, 
			'location': self.location, 'host_id': self.host.user_id, 'host_alias': self.host.alias}

	@staticmethod
	def jsonify_parties():
		party_list = [value.get_dict() for key, value in Party.party_map.iteritems()]
		return json.dumps({'party_list': party_list})
