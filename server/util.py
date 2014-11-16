from party import Party
from user import User
from song import Song
import json

party_map = Party.party_map
user_map = User.user_map
email_map = User.email_map
song_map = Song.song_map

def create_party(u_id, party_name, party_location):
    try:
        new_party = Party(user_map[u_id], party_name, party_location)
        return json.dumps({'party_id': new_party.party_id})
    except Exception as e:
        return json.dumps({'err': str(e)})

def end(p_id):
    try:
        party_map[p_id].end()
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def next(p_id):
    try:
        next_song = party_map[p_id].next_song()
        return next_song.abrev_json()
    except Exception as e:
        return json.dumps({'err': str(e)})

def now_playing(p_id):
    try:
        cur_song = party_map[p_id].now_playing
        if cur_song:
            return cur_song.abrev_json()
        else:
            return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def join(p_id, u_id):
    try:
        party_map[p_id].add_user(user_map[u_id])
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def list_parties():
    try:
        return Party.jsonify_parties()
    except Exception as e:
        return json.dumps({'err': str(e)})

def leave(p_id, u_id):
    try:
        party_map[p_id].remove_user(user_map[u_id])
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def login(email, location):
    try:
        if email not in email_map:
            User(email)
        user = email_map[email]
        user.set_location(location)
        return user.abrev_json()
    except Exception as e:
        return json.dumps({'err': str(e)})

def add(u_id, spotify_id):
    try:
        cur_user = user_map[u_id]
        new_song = Song(spotify_id, cur_user)
        cur_user.queue.add_song(new_song)
        return json.dumps({'song_id': new_song.song_id})
    except Exception as e:
        return json.dumps({'err': str(e)})

def remove(u_id, s_id):
    try:
        user_map[u_id].queue.remove_song(song_map[s_id])
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def clear(u_id):
    try:
        user_map[u_id].queue.clear()        
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def move(u_id, s_id, disp):
    try:
        user_map[u_id].queue.move(song_map[s_id], disp)
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def list_user_queue(u_id):
    try:
        user_queue = [song.get_dict() for song in user_map[u_id].queue.list()]
        return json.dumps({'user_queue': user_queue})
    except Exception as e:
        return json.dumps({'err': str(e)})

def list_party_queue(p_id):
    try:
        party_queue = [song.get_dict() for song in party_map[p_id].queue.list()]
        return json.dumps({'party_queue': party_queue})
    except Exception as e:
        return json.dumps({'err': str(e)})

def user_info(u_id):
    try:
        return user_map[u_id].abrev_json()
    except Exception as e:
        return json.dumps({'err': str(e)})

def set_alias(u_id, alias):
    try:
        cur_user = user_map[u_id]
        cur_user.set_alias(alias)
        return cur_user.abrev_json()
    except Exception as e:
        return json.dumps({'err': str(e)})

def set_location(u_id, location):
    try:
        cur_user = user_map[u_id]
        cur_user.set_location(location)
        return cur_user.abrev_json()
    except Exception as e:
        return json.dumps({'err': str(e)})
