import json

class User(object):
	user_map = {}
	email_map = {}
	cur_user_id = 0

	def __init__(self, email):
		self.user_id = User.cur_user_id
		self.email = email
		self.alias = email
		self.party = None
		self.queue = []
		User.email_map[self.email] = self
		User.user_map[self.user_id] = self
		User.cur_user_id += 1

	def __repr__(self):
		return "User(%s, %s, %s) : %s" %  (str(self.user_id), str(self.email), str(self.queue), str(self.alias))

	def __str__(self):
		return "User(%s, %s, %s) : %s" %  (str(self.user_id), str(self.email), str(self.queue), str(self.alias))

	def add_song(self, song):
		if song not in self.queue:
			self.queue.append(song)

	def remove_song(self, song):
		if song in self.queue:
			self.queue.remove(song) #does not remove the sID from the song_map dictionary

	def clear(self):
		self.queue = []

	def top(self): #only shows what top song is, does not remove it
		if self.queue:
			return self.queue[0]
		else:
			return None

	def pop(self): #only removes top song, does not show what top song is
		if self.queue:
			return self.queue.pop(0)
		else:
			return None

	def list(self): #returns list of songs
	 	return self.queue

	# def list_as_dicts(self):
	# 	return [song.to_dict() for song in self.queue]

	def move(self, song, displacement):
		if song in self.queue:
			index = self.queue.index(song)
			self.queue.remove(song)
			new_pos = index + displacement
			if new_pos > len(self.queue):
				self.queue.append(song)
			elif new_pos < 0:
				self.queue.insert(0,song)
			else:
				self.queue.insert(new_pos, song)

	def set_alias(self, alias):
		self.alias = alias

	def set_location(self, location):
		self.location = location

	def set_party(self, party):
		self.party = party

	def abrev_json(self):
		if self.party:
			return json.dumps({'user_id': self.user_id, 'email': self.email, 'alias': self.alias,
				'location': self.location, 'party_id': self.party.party_id})
		else:
			return json.dumps({'user_id': self.user_id, 'email': self.email, 'alias': self.alias,
				'location': self.location, 'party_id': None})
