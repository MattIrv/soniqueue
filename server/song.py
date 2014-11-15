
class Song(object):
	song_map = {}
	cur_sID = 0
	def __init__(self, URI, sID):
		self.URI = URI
		self.sID = sID
		cur_sID += 1
		song_map[sID] = self

