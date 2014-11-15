import queues
import song
import user

class Party(object):
	party_map = {}
	cur_pID = 0

	def __init__(self, host, name, location):
		self.name = name
		self.host = host
		self.location = location
		self.userDict = {}
		self.queue = queues.PartyQueue()
		self.pID = Party.cur_pID
		Party.party_map[self.pID] = self
		Party.cur_pID += 1
	def __str__(self):
		return "Party(%s, %s, %s) : %s" % (str(self.name), str(self.host), str(self.location), str(self.pID))
	def __repr__(self):
		return "Party(%s, %s, %s) : %s" % (str(self.name), str(self.host), str(self.location), str(self.pID))
	def add_user(self, user):
		self.userDict[user.uID] = user
		self.queue.add_user(user)


print "Tests:"
u0 = user.User("Chris")
u1 = user.User("Zane")
u2 = user.User("Kate")
p0 = Party("cool party",u1,"VA")
p0.add_user(u0)
p0.add_user(u1)
p0.add_user(u2)
#print p0
#print p0.queue
print p0.queue.list()
#print u0
#print u1
#print u2
u0.set_alias("C-Dog")
u1.set_alias("Smurfinator")
u2.set_alias("Kat")
s0 = song.Song("11xo9d9",u0)
s1 = song.Song("f8u7dd6",u1)
s2 = song.Song("dkk4k4k",u2)
s3 = song.Song("4k5k6l1",u2)
s4 = song.Song("kldakk3",u0)
u0.queue.add_song(s0)
u0.queue.add_song(s4)
u1.queue.add_song(s1)
u2.queue.add_song(s2)
u2.queue.add_song(s3)
p0.add_user(u0)
p0.add_user(u1)
p0.add_user(u2)
p0.add_user(u0)
p0.add_user(u1)
p0.add_user(u2)
#print u0.queue.list()
#print u1.queue.list()
#print u2.queue.list()
print p0.queue.list()
print p0.queue.pop()
print p0.queue.list()
p0.add_user(u0)
print p0.queue.list()
#print p0
#print u0 
#print u1
#print u2
#print p0.queue