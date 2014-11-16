import user
import queues
import json

class Song(object):
	song_map = {}
	cur_song_id = 0

	def __init__(self, spotify_id, user, song_name, artist_name, album_name, album_cover_url):
		self.spotify_id = spotify_id
		self.song_id = Song.cur_song_id
		self.song_name = song_name
		self.artist_name = artist_name
		self.album_name = album_name
		self.album_cover_url = album_cover_url
		self.user = user
		Song.song_map[self.song_id] = self
		Song.cur_song_id += 1

	def __repr__(self):
		return "Song(%s, %s)" % (str(self.song_id), str(self.spotify_id))

	def __str__(self):
		return "Song(%s, %s)" % (str(self.song_id), str(self.spotify_id))

	def abrev_json(self):
		return json.dumps(self.get_dict())

	def get_dict(self):
		return {'song_id': self.song_id, 'spotify_id': self.spotify_id,
			'user_id': self.user.user_id, 'user_alias': self.user.alias,
			'song_name' : self.song_name, 'artist_name': self.artist_name,
			'album_name' : self.album_name, 'album_cover_url' : self.album_cover_url}
