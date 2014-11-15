import user
import queues

class Song(object):
	song_map = {}
	cur_sID = 0
	def __init__(self, URI, user):
		self.URI = URI
		self.sID = Song.cur_sID
		self.user = user
		Song.song_map[self.sID] = self
		Song.cur_sID += 1
	def __repr__(self):
		return "Song(%s, %s)" % (str(self.sID), str(self.URI))
	def __str__(self):
		return "Song(%s, %s)" % (str(self.sID), str(self.URI))
