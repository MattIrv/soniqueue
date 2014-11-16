import queues
import json

class User(object):
	user_map = {}
	email_map = {}
	cur_user_id = 0

	def __init__(self, email):
		self.user_id = User.cur_user_id
		self.email = email
		self.alias = email
		self.location = location
		self.party = None
		self.queue = queues.UserQueue()
		User.email_map[self.email] = self
		User.user_map[self.user_id] = self
		User.cur_user_id += 1

	def __repr__(self):
		return "User(%s, %s, %s) : %s" %  (str(self.user_id), str(self.email), str(self.queue), str(self.alias))

	def __str__(self):
		return "User(%s, %s, %s) : %s" %  (str(self.user_id), str(self.email), str(self.queue), str(self.alias))

	def set_alias(self, alias):
		self.alias = alias

	def set_location(self, location):
		self.location = location

	def set_party(self, party):
		self.party = party

	def jsonify(self):
		return json.dumps({'user_id': self.user_id, 'email': self.email, 'queue': self.queue.list(),
			'alias': self.alias, 'location': self.location, 'party': self.party})

	def abrev_json(self):
		return json.dumps({'user_id': self.user_id, 'email': self.email, 'alias': self.alias,
			'location': self.location, 'party_id': self.party.party_id})
