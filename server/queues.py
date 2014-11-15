import song

class UserQueue(object):
	def __init__(self):
		self.queue = []
	def addSong(self, URI):
		self.queue.append(Song(URI,cur_sID))
	def removeSong(self, sID):
		self.queue.remove(song_map[sID]) #does not remove the sID from the song_map dictionary
	def clearQueue(self):
		self.queue = []
	def top(self): #only shows what top song is, does not remove it
		if self.queue:
			return self.queue[0]
		else:
			return None
	def pop(self): #only removes top song, does not show what top song is
		if self.queue:
			self.removeSong(self.top().sID)
class PartyQueue(object):
	def __init__(self):
		pass