import queues

class User(object):
	user_map = {}
	cur_uID = 0

	def __init__(self, googleID):
		self.uID = User.cur_uID
		self.googleID = googleID
		self.alias = googleID
		self.queue = queues.UserQueue()
		User.user_map[self.uID] = self
		User.cur_uID += 1
	def __repr__(self):
		return "User(%s, %s, %s) : %s" %  (str(self.uID), str(self.googleID), str(self.queue), str(self.alias))
	def __str__(self):
		return "User(%s, %s, %s) : %s" %  (str(self.uID), str(self.googleID), str(self.queue), str(self.alias))
	def set_alias(self, alias):
		self.alias = alias