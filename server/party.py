import queues
import song
import user

class Party(object):
	party_map = {}
	cur_party_id = 0

	def __init__(self, host, name, location):
		self.name = name
		self.host = host
		self.location = location
		self.users = set()
		self.queue = queues.PartyQueue()
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
	def remove_user(self, user):
		self.users.remove(user)
		self.queue.remove_user(user)
