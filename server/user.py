import queues

class User(object):
	user_map = {}
	email_map = {}
	cur_user_id = 0

	def __init__(self, email):
		self.user_id = User.cur_user_id
		self.email = email
		self.alias = email
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