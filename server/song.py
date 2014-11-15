import user
import queues

class Song(object):
	song_map = {}
	cur_song_id = 0
	def __init__(self, spotify_id, user):
		self.spotify_id = spotify_id
		self.song_id = Song.cur_song_id
		self.user = user
		Song.song_map[self.song_id] = self
		Song.cur_song_id += 1
	def __repr__(self):
		return "Song(%s, %s)" % (str(self.song_id), str(self.spotify_id))
	def __str__(self):
		return "Song(%s, %s)" % (str(self.song_id), str(self.spotify_id))
