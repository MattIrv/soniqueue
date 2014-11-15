import queues

class Party(object):
	def __init__(self, name, host, location):
		self.name = name
		self.host = host
		self.location = location
		self.userDict = {}
		self.queue = queues.PartyQueue()
	def __str__(self):
		return "Party(%s, %s, %s)" % (str(name), str(host), str(location))
	def __repr__(self):
		return "Party(%s, %s, %s)" % (str(name), str(host), str(location))