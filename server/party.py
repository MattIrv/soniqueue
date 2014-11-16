import queues
import song
import user
import json
import copy

class Party(object):
	party_map = {}
	cur_party_id = 0

	def __init__(self, host, name, location):
		self.name = name
		self.host = host
		self.location = location
		self.users = set()
		self.queue = []
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
		if user.queue.list() and user not in self.queue:
			self.queue.append(user)
		user.set_party(self)

	def remove_user(self, user):
		self.users.remove(user)
		if user in self.queue: #if the user has a queue, then they are in the partyqueue so we remove them
			self.queue.remove(user)
		user.set_party(None)

	def end(self):
		for user in self.users:
			user.party = None
		self.queue = []
		Party.party_map.pop(self.party_id)

	def next_song(self):
		user = self.queue.pop()
		if not user:
			return None
		song = user.queue.pop()
		self.queue.add_user(user)
		now_playing = song
		return now_playing

	def abrev_json(self):
		return json.dumps(self.get_dict())

	def get_dict(self):
		return {'party_id': self.party_id, 'name': self.name, 
			'location': self.location, 'host_id': self.host.user_id, 'host_alias': self.host.alias}

	def top(self):
		if self.queue:
			return self.queue[0]
		else:
			return None

	def pop(self): #removes the top user from the queue and returns it
		if self.queue: #if there is a user on the queue
			return self.queue.pop(0)
		else:
			return None

	def list(self): #returns a list of songs
		temp_list = [copy.deepcopy(user.queue.list()) for user in self.queue]
		ret_list = []
		while temp_list:
			temp_list = [queue for queue in temp_list if queue] #removes all empty list elements from temp_list
			for queue in temp_list:
				ret_list.append(queue.pop(0))		
		return ret_list

	@staticmethod
	def jsonify_parties():
		party_list = [value.get_dict() for key, value in Party.party_map.iteritems()]
		return json.dumps({'party_list': party_list})
