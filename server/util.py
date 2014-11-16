from party import Party
from user import User
from song import Song
import json

party_map = Party.party_map
user_map = User.user_map
email_map = User.email_map
song_map = Song.song_map

def create_party(u_id, party_name, party_location):
    # try:
        new_party = Party(user_map[u_id], party_name, party_location)
        return json.dumps({'party_id': new_party.party_id})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def end(p_id):
    # try:
        party_map[p_id].end()
        return json.dumps({})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def next_song(p_id):
    # try:
        next_song = party_map[p_id].next_song()
        if not next_song:
            return json.dumps({})
        else:
            return next_song.abrev_json()
    # except Exception as e:
        return json.dumps({'err': str(e)})

def now_playing(p_id):
    # try:
        cur_song = party_map[p_id].now_playing
        if cur_song:
            return cur_song.abrev_json()
        else:
            return json.dumps({})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def join(p_id, u_id):
    # try:
        party_map[p_id].add_user(user_map[u_id])
        return json.dumps({})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def list_parties():
    # try:
        return Party.jsonify_parties()
    # except Exception as e:
        return json.dumps({'err': str(e)})

def leave(p_id, u_id):
    # try:
        party_map[p_id].remove_user(user_map[u_id])
        return json.dumps({})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def login(email, location):
    # try:
        if email not in email_map:
            User(email)
        user = email_map[email]
        user.set_location(location)
        return user.abrev_json()
    # except Exception as e:
        return json.dumps({'err': str(e)})

def add(u_id, spotify_id, song_name, artist_name, album_name, album_cover_url):
    # try:
        user = user_map[u_id]
        new_song = Song(spotify_id, user, song_name, artist_name, album_name, album_cover_url)
        user.add_song(new_song)
        party = user.party
        if party:
            party.add_user(user)
        return json.dumps({'song_id': new_song.song_id})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def remove(u_id, s_id):
    # try:
        user = user_map[u_id]
        user.remove_song(song_map[s_id])
        party = user.party
        if party and not user.list():
            party.remove_user(user)
        return json.dumps({})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def clear(u_id):
    # try:
        user = user_map[u_id]
        user.clear()
        party = user.party
        if party:
            party.remove_user_from_queue(user)
        return json.dumps({})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def move(u_id, s_id, disp):
    # try:
        user_map[u_id].move(song_map[s_id], disp)
        return json.dumps({})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def list_user_queue(u_id):
    # try:
        user_queue = [song.get_dict() for song in user_map[u_id].list()]
        return json.dumps({'user_queue': user_queue})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def list_party_queue(p_id):
    # try:
        party_queue = [song.get_dict() for song in party_map[p_id].list()]
        return json.dumps({'party_queue': party_queue})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def user_info(u_id):
    # try:
        return user_map[u_id].abrev_json()
    # except Exception as e:
        return json.dumps({'err': str(e)})

def set_alias(u_id, alias):
    # try:
        cur_user = user_map[u_id]
        cur_user.set_alias(alias)
        return cur_user.abrev_json()
    # except Exception as e:
        return json.dumps({'err': str(e)})

def set_location(u_id, location):
    # try:
        cur_user = user_map[u_id]
        cur_user.set_location(location)
        return cur_user.abrev_json()
    # except Exception as e:
        return json.dumps({'err': str(e)})

def get_position(p_id, u_id):
    # try:
        cur_user = user_map[u_id]
        cur_party = party_map[p_id]
        return json.dumps({'user_pos': str(cur_party.get_user_position(cur_user))})
    # except Exception as e:
        return json.dumps({'err': str(e)})

def main():
    print login('zam4ke@virginia.edu', 'mars')
    print login('ME@virginia.edu', 'mars')
    print create_party(0, 'cool party', 'earth')
    print join(0, 0)
    print list_parties()
    print user_info(0)
    print add(0, 'COOLSONGBRO',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print list_user_queue(0)
    print list_party_queue(0)
    print add(0, 'SECONDSONG',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print list_user_queue(0)
    print list_party_queue(0)
    print '##CLEARING'
    print clear(0)
    print '##DONE'
    print list_party_queue(0)
    print leave(0, 0)
    print add(0, 'THIRDSONG',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print list_party_queue(0)
    print join(0, 0)
    print list_party_queue(0)
    print add(0, 'THIRDSONG',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print list_party_queue(0)
    print remove(0, 3)
    print list_party_queue(0)
    #print remove(0, 3)
    print remove(0, 2)
    print list_party_queue(0)
    print join(0, 1)
    print add(1, 'MYCOOLSONG',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print list_party_queue(0)
    print add(0, 'MYCOOLSONG',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print list_party_queue(0)
    print next_song(0)
    print list_party_queue(0)
    print next_song(0)
    print list_party_queue(0)
    print next_song(0)
    print now_playing(0)
    print next_song(0)
    print add(1, 'Song1',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print add(1, 'Song2',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print add(1, 'Song3',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print add(1, 'Song4',"sweet kiwi", "maroon 5", "songs about jane", "hips.com/hips.jpeg")
    print list_party_queue(0)
    print move(1, 8, 25)
    print list_party_queue(0)
    print set_alias(1, 'MEEEEE')
    print set_location(1, 'MOON')
    print leave(0, 1)
    print list_party_queue(0)
    print join(0, 1)
    print list_party_queue(0)
    print list_parties()
    print end(0)
    print user_info(1)
    print user_info(0)
    print list_parties()

if __name__ == '__main__':
    main()
