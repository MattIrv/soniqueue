import song
import user
import copy

class UserQueue(object):
	def __init__(self):
		self.queue = []
	def add_song(self, s):
		self.queue.append(s)
	def remove_song(self, s):
		self.queue.remove(s) #does not remove the sID from the song_map dictionary
	def clear_queue(self):
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
	def move(self, song, displacement):
		index = self.queue.index(song)
		self.queue.remove(song)
		new_pos = index + displacement
		if new_pos > len(self.queue):
			self.queue.append(song)
		elif new_pos < 0:
			self.queue.insert(0,song)
		else:
			self.queue.insert(new_pos, song)
	def __repr__(self):
		return "UserQueue(%s)" %  (str(self.queue))
	def __str__(self):
		return str(self.queue)


class PartyQueue(object):
	def __init__(self):
		self.queue = []
	def add_user(self, user):
		if user.queue.list() and user not in self.queue:
			self.queue.append(user)
	def remove_user(self, user):
		if user.queue: #if the user has a queue, then they are in the partyqueue so we remove them
			self.queue.remove(user)
	def top(self): #returns the top user in the user queue, does not pop them from queue
		if self.queue:
			return self.queue[0]
		else:
			return None
	def pop(self): #removes the top user from the queue and returns it
		if self.queue: #if there is a user on the queue
			return self.queue.pop(0)
	def list(self): #returns a list of songs
		temp_list = [copy.deepcopy(user.queue.list()) for user in self.queue]
		ret_list = []
		while temp_list:
			temp_list = [queue for queue in temp_list if queue] #removes all empty list elements from temp_list
			for queue in temp_list:
				ret_list.append(queue.pop(0))		
		return ret_list
	def __repr__(self):
		return "PartyQueue(%s)" %  (str(self.queue))
	def __str__(self):
		return str(self.queue)